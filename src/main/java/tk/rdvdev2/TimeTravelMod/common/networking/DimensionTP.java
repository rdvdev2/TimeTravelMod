package tk.rdvdev2.TimeTravelMod.common.networking;

import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import tk.rdvdev2.TimeTravelMod.ModItems;
import tk.rdvdev2.TimeTravelMod.ModRegistries;
import tk.rdvdev2.TimeTravelMod.api.dimension.TimeLine;
import tk.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;
import tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.TimeMachineHookRunner;
import tk.rdvdev2.TimeTravelMod.common.dimension.ITeleporterTimeMachine;
import tk.rdvdev2.TimeTravelMod.common.timemachine.TimeMachineCreative;

import static tk.rdvdev2.TimeTravelMod.util.CastingHelper.intToEnumFacing;

public class DimensionTP implements IMessage {
    public DimensionTP(){}

    private int dim;
    private TimeMachine tm;
    private BlockPos pos;
    private EnumFacing side;

    public DimensionTP(int dim, TimeMachine tm, BlockPos pos, EnumFacing side) {
        this.dim = dim;
        this.tm = tm instanceof TimeMachineHookRunner ? ((TimeMachineHookRunner)tm).removeHooks() : tm;
        this.pos = pos;
        this.side = side;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(dim);
        String key = ModRegistries.timeMachinesRegistry.getKey(tm).toString();
        buf.writeInt(key.length());
        buf.writeCharSequence(tm.toString(), Charsets.UTF_8);
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
        buf.writeInt(side.getIndex());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        dim = buf.readInt();
        int size = buf.readInt();
        tm = TimeMachine.fromString(buf.readCharSequence(size, Charsets.UTF_8).toString());
        pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        side = intToEnumFacing(buf.readInt());
    }

    public static class DimensionTPHandler implements IMessageHandler<DimensionTP, IMessage> {

        @Override
        public IMessage onMessage(DimensionTP message, MessageContext ctx) {
            EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
            int dim = message.dim;
            BlockPos pos = message.pos;
            EnumFacing side = message.side;
            TimeMachine tm = message.tm.hook(serverPlayer.world, pos, side);
            serverPlayer.getServerWorld().addScheduledTask(() -> {
                if (serverPlayer.world.isBlockLoaded(pos) &&
                    tm.isBuilt(serverPlayer.getServer().getWorld(serverPlayer.dimension), pos, side) &&
                    tm.isPlayerInside(serverPlayer.getServer().getWorld(serverPlayer.dimension), pos, side, serverPlayer) &&
                    !tm.isOverloaded(serverPlayer.getServer().getWorld(serverPlayer.dimension), pos, side) &&
                    canTravel(tm, dim, serverPlayer)){
                    serverPlayer.getServer().getPlayerList().transferPlayerToDimension(serverPlayer, dim, new ITeleporterTimeMachine(serverPlayer.getServer().getWorld(dim), serverPlayer.getServer().getWorld(serverPlayer.dimension), tm, pos, side));
                }
            });
            return null;
        }

        private boolean canTravel(TimeMachine tm, int dim, EntityPlayerMP player) {
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