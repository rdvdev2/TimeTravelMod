package tk.rdvdev2.TimeTravelMod.common.networking;

import com.google.common.base.Charsets;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.fml.network.NetworkEvent;
import tk.rdvdev2.TimeTravelMod.ModItems;
import tk.rdvdev2.TimeTravelMod.ModRegistries;
import tk.rdvdev2.TimeTravelMod.api.dimension.TimeLine;
import tk.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;
import tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.TimeMachineHookRunner;
import tk.rdvdev2.TimeTravelMod.common.dimension.ITeleporterTimeMachine;
import tk.rdvdev2.TimeTravelMod.common.timemachine.TimeMachineCreative;

import java.util.function.Supplier;

import static tk.rdvdev2.TimeTravelMod.util.CastingHelper.intToEnumFacing;

public class DimensionTpPKT {
    public DimensionTpPKT(){}

    private int dim;
    private TimeMachine tm;
    private BlockPos pos;
    private EnumFacing side;

    public DimensionTpPKT(int dim, TimeMachine tm, BlockPos pos, EnumFacing side) {
        this.dim = dim;
        this.tm = tm instanceof TimeMachineHookRunner ? ((TimeMachineHookRunner)tm).removeHooks() : tm;
        this.pos = pos;
        this.side = side;
    }

    public static void encode(DimensionTpPKT pkt, PacketBuffer buf) {
        buf.writeInt(pkt.dim);
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
        pkt.dim = buf.readInt();
        int size = buf.readInt();
        pkt.tm = TimeMachine.fromString(buf.readCharSequence(size, Charsets.UTF_8).toString());
        pkt.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        pkt.side = intToEnumFacing(buf.readInt());
        return pkt;
    }

    public static class Handler {

        public static void handle(DimensionTpPKT message, Supplier<NetworkEvent.Context> ctx) {
            EntityPlayerMP serverPlayer = ctx.get().getSender();
            int dim = message.dim;
            BlockPos pos = message.pos;
            EnumFacing side = message.side;
            TimeMachine tm = message.tm.hook(serverPlayer.world, pos, side);
            ctx.get().enqueueWork(() -> {
                if (serverPlayer.world.isBlockLoaded(pos) &&
                    tm.isBuilt(serverPlayer.getServer().getWorld(serverPlayer.dimension), pos, side) &&
                    tm.isPlayerInside(serverPlayer.getServer().getWorld(serverPlayer.dimension), pos, side, serverPlayer) &&
                    !tm.isOverloaded(serverPlayer.getServer().getWorld(serverPlayer.dimension), pos, side) &&
                    canTravel(tm, dim, serverPlayer)){
                        serverPlayer.getServer().getPlayerList().changePlayerDimension(serverPlayer, DimensionType.getById(dim), new ITeleporterTimeMachine(serverPlayer.getServer().getWorld(DimensionType.getById(dim)), serverPlayer.getServer().getWorld(serverPlayer.dimension), tm, pos, side));
                }
            });
        }

        private static boolean canTravel(TimeMachine tm, int dim, EntityPlayerMP player) {
            if (tm instanceof TimeMachineCreative) {
                if (!ItemStack.areItemsEqual(player.inventory.getCurrentItem(), new ItemStack(ModItems.creativeTimeMachine, 1)))
                    return false;
            }
            if (dim == 0) {
                return true;
            }
            for (TimeLine tl:ModRegistries.timeLinesRegistry.getSlaveMap(ModRegistries.TIERTOTIMELINE, TimeLine[][].class)[tm.getTier()]) {
                if (tl.getDimId() == dim) {
                    return true;
                }
            }
            return false;
        }
    }
}
