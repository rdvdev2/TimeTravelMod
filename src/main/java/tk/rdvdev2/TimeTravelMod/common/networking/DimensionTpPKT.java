package tk.rdvdev2.TimeTravelMod.common.networking;

import com.google.common.base.Charsets;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ServerWorld;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.hooks.BasicEventHooks;
import net.minecraftforge.fml.network.NetworkEvent;
import tk.rdvdev2.TimeTravelMod.ModItems;
import tk.rdvdev2.TimeTravelMod.ModRegistries;
import tk.rdvdev2.TimeTravelMod.TimeTravelMod;
import tk.rdvdev2.TimeTravelMod.api.dimension.TimeLine;
import tk.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;
import tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.TimeMachineHookRunner;
import tk.rdvdev2.TimeTravelMod.common.timemachine.TimeMachineCreative;
import tk.rdvdev2.TimeTravelMod.common.world.dimension.TimeLinePresent;

import java.util.function.Supplier;

public class DimensionTpPKT {
    public DimensionTpPKT() {
    }

    private TimeLine tl;
    private TimeMachine tm;
    private BlockPos pos;
    private Direction side;

    public DimensionTpPKT(TimeLine tl, TimeMachine tm, BlockPos pos, Direction side) {
        this.tl = tl;
        this.tm = tm instanceof TimeMachineHookRunner ? ((TimeMachineHookRunner) tm).removeHooks() : tm;
        this.pos = pos;
        this.side = side;
    }

    public static void encode(DimensionTpPKT pkt, PacketBuffer buf) {

        String tl = pkt.tl.getRegistryName().toString();
        buf.writeInt(tl.length());
        buf.writeCharSequence(tl, Charsets.UTF_8);
        String key = ModRegistries.timeMachinesRegistry.getKey(pkt.tm).toString();
        buf.writeInt(key.length());
        buf.writeCharSequence(pkt.tm.toString(), Charsets.UTF_8);
        buf.writeInt(pkt.pos.getX());
        buf.writeInt(pkt.pos.getY());
        buf.writeInt(pkt.pos.getZ());
        buf.writeInt(pkt.side.getIndex());
    }

    public static DimensionTpPKT decode(PacketBuffer buf) {
        DimensionTpPKT pkt = new DimensionTpPKT();
        int size1 = buf.readInt();
        pkt.tl = ModRegistries.timeLinesRegistry.getValue(new ResourceLocation(buf.readCharSequence(size1, Charsets.UTF_8).toString()));
        int size2 = buf.readInt();
        pkt.tm = TimeMachine.fromString(buf.readCharSequence(size2, Charsets.UTF_8).toString());
        pkt.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        pkt.side = Direction.byIndex(buf.readInt());
        return pkt;
    }

    public static class Handler {

        public static void handle(DimensionTpPKT message, Supplier<NetworkEvent.Context> ctx) {
            ServerPlayerEntity serverPlayer = ctx.get().getSender();
            DimensionType dim = message.tl instanceof TimeLinePresent ? DimensionType.OVERWORLD : DimensionType.byName(message.tl.getDimension().getRegistryName());
            BlockPos pos = message.pos;
            Direction side = message.side;
            TimeMachine tm = message.tm.hook(serverPlayer.world, pos, side);
            ctx.get().enqueueWork(() -> {
                if (serverPlayer.world.isBlockLoaded(pos) &&
                    tm.isBuilt(serverPlayer.getServer().getWorld(serverPlayer.dimension), pos, side) &&
                    tm.isPlayerInside(serverPlayer.getServer().getWorld(serverPlayer.dimension), pos, side, serverPlayer) &&
                    !tm.isOverloaded(serverPlayer.getServer().getWorld(serverPlayer.dimension), pos, side) &&
                    canTravel(tm, dim, serverPlayer)) {
                        changeDim(serverPlayer, pos, dim, tm, side); // FIXME: Lighting isn't rendered first time visiting TimeLines
                } else TimeTravelMod.logger.error("Time Travel canceled due to incorrect conditions");
            });
        }

        private static boolean canTravel(TimeMachine tm, DimensionType dim, ServerPlayerEntity player) {
            if (tm instanceof TimeMachineCreative) {
                if (!ItemStack.areItemsEqual(player.inventory.getCurrentItem(), new ItemStack(ModItems.creativeTimeMachine, 1)))
                    return false;
            }
            if (dim == DimensionType.OVERWORLD) return true;
            for (TimeLine tl : ModRegistries.timeLinesRegistry.getSlaveMap(ModRegistries.TIERTOTIMELINE, TimeLine[][].class)[tm.getTier()]) {
                if (tl.getDimension() == dim.getModType()) {
                    return true;
                }
            }
            return false;
        }

        public static void changeDim(ServerPlayerEntity player, BlockPos pos, DimensionType destDim, TimeMachine tm, Direction side) { // copy from ServerPlayerEntity#changeDimension
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
            player.connection.sendPacket(new SPlayerAbilitiesPacket(player.playerAbilities));
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
    }
}
