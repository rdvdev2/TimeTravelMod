package tk.rdvdev2.TimeTravelMod.common.networking;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.fml.network.NetworkEvent;
import tk.rdvdev2.TimeTravelMod.ModRegistries;
import tk.rdvdev2.TimeTravelMod.TimeTravelMod;
import tk.rdvdev2.TimeTravelMod.api.dimension.TimeLine;
import tk.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;
import tk.rdvdev2.TimeTravelMod.common.timemachine.TimeMachineTeleporter;
import tk.rdvdev2.TimeTravelMod.common.timemachine.exception.IncompatibleTimeMachineHooksException;
import tk.rdvdev2.TimeTravelMod.common.util.TimeMachineUtils;
import tk.rdvdev2.TimeTravelMod.common.world.corruption.ICorruption;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DimensionTpPKT {

    @CapabilityInject(ICorruption.class)
    static Capability<ICorruption> CORRUPTION_CAPABILITY = null;

    public DimensionTpPKT() {
        additionalEntities = new HashSet<>();
    }

    private tk.rdvdev2.TimeTravelMod.common.world.dimension.TimeLine tl;
    private TimeMachine tm;
    private BlockPos pos;
    private Direction side;
    private Set<UUID> additionalEntities;

    public DimensionTpPKT(tk.rdvdev2.TimeTravelMod.common.world.dimension.TimeLine tl, TimeMachine tm, BlockPos pos, Direction side, UUID... additionalEntities) {
        this();
        this.tl = tl;
        this.tm = tm.removeHooks();
        this.pos = pos;
        this.side = side;
        if (additionalEntities != null && additionalEntities.length != 0) this.additionalEntities = Arrays.stream(additionalEntities).collect(Collectors.toSet());
    }

    public static void encode(DimensionTpPKT pkt, PacketBuffer buf) {

        buf.writeRegistryId(pkt.tl);
        buf.writeRegistryId(pkt.tm);
        buf.writeBlockPos(pkt.pos);
        buf.writeEnumValue(pkt.side);
        buf.writeInt(pkt.additionalEntities.size());
        pkt.additionalEntities.forEach(buf::writeUniqueId);
    }

    public static DimensionTpPKT decode(PacketBuffer buf) {
        DimensionTpPKT pkt = new DimensionTpPKT();
        pkt.tl = (tk.rdvdev2.TimeTravelMod.common.world.dimension.TimeLine) buf.readRegistryIdSafe(TimeLine.class);
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

    public static void handle(DimensionTpPKT message, Supplier<NetworkEvent.Context> ctx) {
        ServerPlayerEntity serverPlayer = ctx.get().getSender();
        DimensionType dim = message.tl.getDimension();
        BlockPos pos = message.pos;
        Direction side = message.side;
        ServerWorld origin = serverPlayer.getServerWorld();
        TimeMachine tm = null;
        try {
            tm = message.tm.hook(serverPlayer.world, pos, side);
        } catch (IncompatibleTimeMachineHooksException e) {
            throw new RuntimeException("Time travel was triggered with invalid upgrade configuration");
        }
        TimeMachine finalTm = tm;
        ctx.get().enqueueWork(() -> {
            List<Entity> entities = finalTm.getEntitiesInside(origin, pos, side);
            AtomicBoolean entitiesFlag = new AtomicBoolean(true);
            message.additionalEntities.forEach( entity -> {
                if (!entities.contains(entity)) {
                    entitiesFlag.set(false);
                }
            });
            if (entitiesFlag.get() &&
                serverPlayer.world.isBlockLoaded(pos) &&
                TimeMachineUtils.serverCheck(serverPlayer.server, finalTm, serverPlayer.world, serverPlayer, pos, side)) {
                    if (finalTm.getTier() >= message.tl.getMinTier()) {
                        applyCorruption(finalTm, serverPlayer.dimension, dim, serverPlayer.server);
                        serverPlayer.changeDimension(dim, new TimeMachineTeleporter(finalTm, pos, side, true));
                        message.additionalEntities.stream()
                                .map(origin::getEntityByUuid)
                                .filter(Objects::nonNull)
                                .forEach(entity -> entity.changeDimension(dim, new TimeMachineTeleporter(finalTm, pos, side, false)));
                    } else {
                        Arrays.stream(serverPlayer.server.getPlayerList().getOppedPlayers().getKeys())
                                .map(op -> serverPlayer.server.getPlayerList().getPlayerByUsername(op))
                                .forEach(op -> {
                                    if (op != null)
                                        op.sendStatusMessage(TimeMachineUtils.Check.UNREACHABLE_DIM.getCheaterReport(serverPlayer), false);
                                });
                    }
            } else {
                if (!entitiesFlag.get()) {
                    serverPlayer.sendStatusMessage(TimeMachineUtils.Check.ENTITIES_ESCAPED.getClientError(), true);
                }
                TimeTravelMod.LOGGER.error("Time Travel canceled due to incorrect conditions");
            }
        });
    }

    public static void applyCorruption(TimeMachine tm, DimensionType origDim, DimensionType destDim, MinecraftServer server) {
        int origTier = -1, destTier = -1;
        for (TimeLine timeLine: ModRegistries.TIME_LINES) {
            tk.rdvdev2.TimeTravelMod.common.world.dimension.TimeLine current = (tk.rdvdev2.TimeTravelMod.common.world.dimension.TimeLine) timeLine;
            if (current.getDimension() == origDim) {
                origTier = current.getMinTier();
            } else if (current.getDimension() == destDim) {
                destTier = current.getMinTier();
            }
        }
        if (destTier == -1 || origTier == -1) throw new RuntimeException();
        int amount = Math.abs(destTier - origTier) * tm.getCorruptionMultiplier();
        server.getWorld(origDim).getCapability(CORRUPTION_CAPABILITY).orElseThrow(RuntimeException::new).increaseCorruptionLevel(amount);
        server.getWorld(destDim).getCapability(CORRUPTION_CAPABILITY).orElseThrow(RuntimeException::new).increaseCorruptionLevel(amount);
    }
}
