package tk.rdvdev2.TimeTravelMod.common.networking;

import com.google.common.base.Charsets;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
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
    public DimensionTpPKT(){}

    private TimeLine tl;
    private TimeMachine tm;
    private BlockPos pos;
    private Direction side;

    public DimensionTpPKT(TimeLine tl, TimeMachine tm, BlockPos pos, Direction side) {
        this.tl = tl;
        this.tm = tm instanceof TimeMachineHookRunner ? ((TimeMachineHookRunner)tm).removeHooks() : tm;
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
                    canTravel(tm, dim, serverPlayer)){
                        tm.teleporterTasks(serverPlayer, serverPlayer.getServer().getWorld(dim), serverPlayer.getServer().getWorld(serverPlayer.dimension), pos, side);
                        serverPlayer.changeDimension(dim); // TODO: Check that works
                } else TimeTravelMod.logger.error("Time Travel canceled due to incorrect conditions");
            });
        }

        private static boolean canTravel(TimeMachine tm, DimensionType dim, ServerPlayerEntity player) {
            if (tm instanceof TimeMachineCreative) {
                if (!ItemStack.areItemsEqual(player.inventory.getCurrentItem(), new ItemStack(ModItems.creativeTimeMachine, 1)))
                    return false;
            }
            if (dim == DimensionType.OVERWORLD) return true;
            for (TimeLine tl:ModRegistries.timeLinesRegistry.getSlaveMap(ModRegistries.TIERTOTIMELINE, TimeLine[][].class)[tm.getTier()]) {
                if (tl.getDimension() == dim.getModType()) {
                    return true;
                }
            }
            return false;
        }
    }
}
