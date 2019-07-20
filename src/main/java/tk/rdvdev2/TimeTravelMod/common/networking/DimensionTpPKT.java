package tk.rdvdev2.TimeTravelMod.common.networking;

import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.ServerWorld;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.hooks.BasicEventHooks;
import net.minecraftforge.fml.network.NetworkEvent;
import tk.rdvdev2.TimeTravelMod.ModItems;
import tk.rdvdev2.TimeTravelMod.ModRegistries;
import tk.rdvdev2.TimeTravelMod.TimeTravelMod;
import tk.rdvdev2.TimeTravelMod.api.dimension.TimeLine;
import tk.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;
import tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.IncompatibleTimeMachineHooksException;
import tk.rdvdev2.TimeTravelMod.common.timemachine.CreativeTimeMachine;
import tk.rdvdev2.TimeTravelMod.common.timemachine.TimeMachineHookRunner;
import tk.rdvdev2.TimeTravelMod.common.world.corruption.ICorruption;
import tk.rdvdev2.TimeTravelMod.common.world.dimension.PresentTimeLine;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DimensionTpPKT {

    @CapabilityInject(ICorruption.class)
    static Capability<ICorruption> CORRUPTION_CAPABILITY = null;

    public DimensionTpPKT() {
        additionalEntities = new HashSet<>();
    }

    private TimeLine tl;
    private TimeMachine tm;
    private BlockPos pos;
    private Direction side;
    private Set<UUID> additionalEntities;

    public DimensionTpPKT(TimeLine tl, TimeMachine tm, BlockPos pos, Direction side, Entity... additionalEntities) {
        this();
        this.tl = tl;
        this.tm = tm instanceof TimeMachineHookRunner ? ((TimeMachineHookRunner) tm).removeHooks() : tm;
        this.pos = pos;
        this.side = side;
        if (additionalEntities != null && additionalEntities.length != 0) this.additionalEntities = Arrays.stream(additionalEntities).map(Entity::getUniqueID).collect(Collectors.toSet());
    }

    public static void encode(DimensionTpPKT pkt, PacketBuffer buf) {

        buf.writeRegistryId(pkt.tl);
        buf.writeRegistryId(pkt.tm);
        buf.writeBlockPos(pkt.pos);
        buf.writeEnumValue(pkt.side);
        buf.writeInt(pkt.additionalEntities.size());
        pkt.additionalEntities.forEach(uuid -> {
            buf.writeUniqueId(uuid);
        });
    }

    public static DimensionTpPKT decode(PacketBuffer buf) {
        DimensionTpPKT pkt = new DimensionTpPKT();
        pkt.tl = buf.readRegistryIdSafe(TimeLine.class);
        pkt.tm = buf.readRegistryIdSafe(TimeMachine.class);
        pkt.pos = buf.readBlockPos();
        pkt.side = buf.readEnumValue(Direction.class);
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            UUID uuid = buf.readUniqueId();
            pkt.additionalEntities.add(uuid);
        }
        return pkt;
    }

    public static class Handler {

        public static void handle(DimensionTpPKT message, Supplier<NetworkEvent.Context> ctx) {
            ServerPlayerEntity serverPlayer = ctx.get().getSender();
            DimensionType dim = message.tl instanceof PresentTimeLine ? DimensionType.field_223227_a_ : DimensionType.byName(message.tl.getDimension().getRegistryName());
            BlockPos pos = message.pos;
            Direction side = message.side;
            TimeMachine tm = null;
            try {
                tm = message.tm.hook(serverPlayer.world, pos, side);
            } catch (IncompatibleTimeMachineHooksException e) {
                throw new RuntimeException("Time travel was triggered with invalid upgrade configuration");
            }
            TimeMachine finalTm = tm;
            ctx.get().enqueueWork(() -> {
                if (serverPlayer.world.isBlockLoaded(pos) &&
                    finalTm.isBuilt(serverPlayer.getServer().getWorld(serverPlayer.dimension), pos, side) &&
                    finalTm.isPlayerInside(serverPlayer.getServer().getWorld(serverPlayer.dimension), pos, side, serverPlayer) &&
                    !finalTm.isOverloaded(serverPlayer.getServer().getWorld(serverPlayer.dimension), pos, side) &&
                    canTravel(finalTm, dim, serverPlayer)) {
                        applyCorruption(finalTm, serverPlayer.dimension, dim, serverPlayer.server);
                        changePlayerDim(serverPlayer, pos, dim, finalTm, side);
                        message.additionalEntities.stream()
                                .map(uuid -> serverPlayer.getServerWorld().getEntityByUuid(uuid))
                                .forEach(entity -> changeEntityDim(entity, pos, dim, finalTm, side));
                } else TimeTravelMod.logger.error("Time Travel canceled due to incorrect conditions");
            });
        }

        private static void applyCorruption(TimeMachine tm, DimensionType origDim, DimensionType destDim, MinecraftServer server) {
            int origTier = -1, destTier = -1;
            Iterator<TimeLine> iterator = ModRegistries.timeLinesRegistry.iterator();
            while (iterator.hasNext()) {
                TimeLine current = iterator.next();
                if (current.getDimension() == origDim.getModType()) {
                    origTier = current.getMinTier();
                } else if (current.getDimension() == destDim.getModType()) {
                    destTier = current.getMinTier();
                }
            }
            if (destTier == -1 || origTier == -1) throw new RuntimeException();
            int amount = Math.abs(destTier - origTier) * tm.getCorruptionMultiplier();
            server.getWorld(origDim).getCapability(CORRUPTION_CAPABILITY).orElseThrow(RuntimeException::new).increaseCorruptionLevel(amount);
            server.getWorld(destDim).getCapability(CORRUPTION_CAPABILITY).orElseThrow(RuntimeException::new).increaseCorruptionLevel(amount);
        }

        private static boolean canTravel(TimeMachine tm, DimensionType dim, ServerPlayerEntity player) {
            if (tm instanceof CreativeTimeMachine) {
                if (!ItemStack.areItemsEqual(player.inventory.getCurrentItem(), new ItemStack(ModItems.creativeTimeMachine, 1)))
                    return false;
            }
            if (dim == DimensionType.field_223227_a_) return true;
            for (TimeLine tl : (HashSet<TimeLine>)ModRegistries.timeLinesRegistry.getSlaveMap(ModRegistries.TIERTOTIMELINE, ArrayList.class).get(tm.getTier())) {
                if (tl.getDimension() == dim.getModType()) {
                    return true;
                }
            }
            return false;
        }

        public static void changePlayerDim(ServerPlayerEntity player, BlockPos pos, DimensionType destDim, TimeMachine tm, Direction side) { // copy from ServerPlayerEntity#changeDimension
            if (!ForgeHooks.onTravelToDimension(player, destDim)) return;
            player.invulnerableDimensionChange = true;
            DimensionType currentDim = player.dimension;
            ServerWorld serverworld = player.server.getWorld(currentDim);
            player.dimension = destDim;
            ServerWorld serverworld1 = player.server.getWorld(destDim);
            WorldInfo worldinfo = player.world.getWorldInfo();
            player.connection.sendPacket(new SRespawnPacket(destDim, worldinfo.getGenerator(), player.interactionManager.getGameType()));
            player.connection.sendPacket(new SServerDifficultyPacket(worldinfo.getDifficulty(), worldinfo.isDifficultyLocked()));
            PlayerList playerlist = player.server.getPlayerList();
            playerlist.updatePermissionLevel(player);
            serverworld.removeEntity(player, true); //Forge: the player entity is moved to the new world, NOT cloned. So keep the data alive with no matching invalidate call.
            player.revive();
            double d0 = player.posX;
            double d1 = player.posY;
            double d2 = player.posZ;
            float f = player.rotationPitch;
            float f1 = player.rotationYaw;
            double d3 = 8.0D;
            float f2 = f1;
            serverworld.getProfiler().startSection("moving");
            double moveFactor = serverworld.getDimension().getMovementFactor() / serverworld1.getDimension().getMovementFactor();
            d0 *= moveFactor;
            d2 *= moveFactor;
            player.setLocationAndAngles(d0, d1, d2, f1, f);
            serverworld.getProfiler().endSection();
            serverworld.getProfiler().startSection("placing");
            double d7 = Math.min(-2.9999872E7D, serverworld1.getWorldBorder().minX() + 16.0D);
            double d4 = Math.min(-2.9999872E7D, serverworld1.getWorldBorder().minZ() + 16.0D);
            double d5 = Math.min(2.9999872E7D, serverworld1.getWorldBorder().maxX() - 16.0D);
            double d6 = Math.min(2.9999872E7D, serverworld1.getWorldBorder().maxZ() - 16.0D);
            d0 = MathHelper.clamp(d0, d7, d5);
            d2 = MathHelper.clamp(d2, d4, d6);
            player.setLocationAndAngles(d0, d1, d2, f1, f);

            tm.teleporterTasks(player, player.getServer().getWorld(destDim), player.getServer().getWorld(currentDim), pos, side);

            serverworld.getProfiler().endSection();
            player.setWorld(serverworld1);
            serverworld1.func_217447_b(player);
            player.connection.setPlayerLocation(player.posX, player.posY, player.posZ, f1, f);
            player.interactionManager.setWorld(serverworld1);
            player.connection.sendPacket(new SPlayerAbilitiesPacket(player.abilities));
            playerlist.sendWorldInfo(player, serverworld1);
            playerlist.sendInventory(player);

            for (EffectInstance effectinstance : player.getActivePotionEffects()) {
                player.connection.sendPacket(new SPlayEntityEffectPacket(player.getEntityId(), effectinstance));
            }

            player.connection.sendPacket(new SPlaySoundEventPacket(1032, BlockPos.ZERO, 0, false));
            player.lastExperience = -1;
            player.lastHealth = -1.0F;
            player.lastFoodLevel = -1;
            BasicEventHooks.firePlayerChangedDimensionEvent(player, currentDim, destDim);
            return;
        }

        public static void changeEntityDim(Entity entityIn, BlockPos pos, DimensionType destDim, TimeMachine tm, Direction side) {
            if (entityIn instanceof ServerPlayerEntity) {
                changePlayerDim((ServerPlayerEntity)entityIn, pos, destDim, tm, side);
                return;
            }
            if (!net.minecraftforge.common.ForgeHooks.onTravelToDimension(entityIn, destDim)) return;
            if (!entityIn.world.isRemote && !entityIn.removed) {
                entityIn.world.getProfiler().startSection("changeDimension");
                MinecraftServer minecraftserver = entityIn.getServer();
                DimensionType dimensiontype = entityIn.dimension;
                ServerWorld serverworld = minecraftserver.getWorld(dimensiontype);
                ServerWorld serverworld1 = minecraftserver.getWorld(destDim);
                entityIn.dimension = destDim;
                entityIn.detach();
                entityIn.world.getProfiler().startSection("reposition");
                Vec3d vec3d = entityIn.getMotion();
                float f = 0.0F;
                BlockPos blockpos;
                if (dimensiontype == DimensionType.field_223229_c_ && destDim == DimensionType.field_223227_a_) {
                    blockpos = serverworld1.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, serverworld1.getSpawnPoint());
                } else if (destDim == DimensionType.field_223229_c_) {
                    blockpos = serverworld1.getSpawnCoordinate();
                } else {
                    double movementFactor = serverworld.getDimension().getMovementFactor() / serverworld1.getDimension().getMovementFactor();
                    double d0 = entityIn.posX * movementFactor;
                    double d1 = entityIn.posZ * movementFactor;

                    double d3 = Math.min(-2.9999872E7D, serverworld1.getWorldBorder().minX() + 16.0D);
                    double d4 = Math.min(-2.9999872E7D, serverworld1.getWorldBorder().minZ() + 16.0D);
                    double d5 = Math.min(2.9999872E7D, serverworld1.getWorldBorder().maxX() - 16.0D);
                    double d6 = Math.min(2.9999872E7D, serverworld1.getWorldBorder().maxZ() - 16.0D);
                    d0 = MathHelper.clamp(d0, d3, d5);
                    d1 = MathHelper.clamp(d1, d4, d6);
                    Vec3d vec3d1 = entityIn.getLastPortalVec();
                    blockpos = new BlockPos(d0, entityIn.posY, d1);
                    BlockPattern.PortalInfo blockpattern$portalinfo = serverworld1.getDefaultTeleporter().func_222272_a(blockpos, vec3d, entityIn.getTeleportDirection(), vec3d1.x, vec3d1.y, entityIn instanceof PlayerEntity);
                    if (blockpattern$portalinfo == null) {
                        return;
                    }

                    blockpos = new BlockPos(blockpattern$portalinfo.field_222505_a);
                    vec3d = blockpattern$portalinfo.field_222506_b;
                    f = (float)blockpattern$portalinfo.field_222507_c;
                }

                entityIn.world.getProfiler().endStartSection("reloading");
                Entity entity = entityIn.getType().create(serverworld1);
                if (entity != null) {
                    entity.copyDataFromOld(entityIn);
                    entity.moveToBlockPosAndAngles(blockpos, entity.rotationYaw + f, entity.rotationPitch);
                    entity.setMotion(vec3d);
                    serverworld1.func_217460_e(entity);
                }

                entityIn.remove(false);
                entityIn.world.getProfiler().endSection();
                serverworld.resetUpdateEntityTick();
                serverworld1.resetUpdateEntityTick();
                entityIn.world.getProfiler().endSection();
                return;
            } else {
                return;
            }
        }
    }
}
