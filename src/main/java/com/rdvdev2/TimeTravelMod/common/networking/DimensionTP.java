package com.rdvdev2.TimeTravelMod.common.networking;

import com.rdvdev2.TimeTravelMod.ModRegistries;
import com.rdvdev2.TimeTravelMod.common.dimension.ITeleporterTimeMachine;
import com.rdvdev2.TimeTravelMod.util.TimeMachine;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class DimensionTP implements IMessage {
    public DimensionTP(){}

    private int dim;
    private TimeMachine tm;
    private BlockPos pos;
    private EnumFacing side;

    public DimensionTP(int dim, TimeMachine tm, BlockPos pos, EnumFacing side) {
        this.dim = dim;
        this.tm = tm;
        this.pos = pos;
        this.side = side;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(dim);
        buf.writeInt(tm.getId());
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
        buf.writeInt(side.getIndex());
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        dim = buf.readInt();
        tm = ModRegistries.timeMachines.getFromId(buf.readInt());
        pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        switch (buf.readInt()) {
            case 0:
                side = EnumFacing.DOWN;
                break;
            case 1:
                side = EnumFacing.UP;
                break;
            case 2:
                side = EnumFacing.NORTH;
                break;
            case 3:
                side = EnumFacing.SOUTH;
                break;
            case 4:
                side = EnumFacing.WEST;
                break;
            case 5:
                side = EnumFacing.EAST;
                break;
        }
    }

    public static class DimensionTPHandler implements IMessageHandler<DimensionTP, IMessage> {

        @Override
        public IMessage onMessage(DimensionTP message, MessageContext ctx) {
            EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
            int dim = message.dim;
            TimeMachine tm = message.tm;
            BlockPos pos = message.pos;
            EnumFacing side = message.side;
            serverPlayer.getServerWorld().addScheduledTask(() -> {
                if (serverPlayer.world.isBlockLoaded(pos)) {
                    serverPlayer.getServer().getPlayerList().transferPlayerToDimension(serverPlayer, dim, new ITeleporterTimeMachine(serverPlayer.getServer().getWorld(dim), serverPlayer.getServer().getWorld(serverPlayer.dimension), tm, pos, side));
                }
            });
            return null;
        }
    }
}
