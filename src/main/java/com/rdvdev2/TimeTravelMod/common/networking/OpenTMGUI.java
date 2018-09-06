package com.rdvdev2.TimeTravelMod.common.networking;

import com.google.common.base.Charsets;
import com.rdvdev2.TimeTravelMod.ModRegistries;
import com.rdvdev2.TimeTravelMod.TimeTravelMod;
import com.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class OpenTMGUI implements IMessage {

    TimeMachine tm;
    BlockPos pos;
    EnumFacing side;

    public OpenTMGUI(TimeMachine tm, BlockPos pos, EnumFacing side) {
        this.tm = tm;
        this.pos = pos;
        this.side = side;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int size = buf.readInt();
        tm = ModRegistries.timeMachinesRegistry.getValue(new ResourceLocation(buf.readCharSequence(size, Charsets.UTF_8).toString()));
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

    @Override
    public void toBytes(ByteBuf buf) {
        String key = ModRegistries.timeMachinesRegistry.getKey(tm).toString();
        buf.writeInt(key.length());
        buf.writeCharSequence(ModRegistries.timeMachinesRegistry.getKey(tm).toString(), Charsets.UTF_8);
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
        buf.writeInt(side.getIndex());
    }

    @SideOnly(Side.CLIENT)
    public static class OpenTMGUIHandler implements IMessageHandler<OpenTMGUI, IMessage> {
        @Override
        public IMessage onMessage(OpenTMGUI message, MessageContext ctx) {
            EntityPlayer player = Minecraft.getMinecraft().player;
            TimeTravelMod.proxy.displayTMGuiScreen(player, message.tm, message.pos, message.side);
            return null;
        }
    }
}
