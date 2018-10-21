package com.rdvdev2.TimeTravelMod.common.networking;

import com.rdvdev2.TimeTravelMod.TimeTravelMod;
import com.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;
import com.rdvdev2.TimeTravelMod.api.timemachine.upgrade.TimeMachineHookRunner;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static com.rdvdev2.TimeTravelMod.util.ByteBufHelper.*;
import static com.rdvdev2.TimeTravelMod.util.CastingHelper.intToEnumFacing;

public class OpenTMGUI implements IMessage {

    TimeMachine tm;
    BlockPos pos;
    EnumFacing side;

    public OpenTMGUI(TimeMachine tm, BlockPos pos, EnumFacing side) {
        this.tm = tm instanceof TimeMachineHookRunner ? ((TimeMachineHookRunner)tm).removeHooks() : tm;
        this.pos = pos;
        this.side = side;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        tm = readTimeMachine(buf);
        pos = readBlockPos(buf);
        side = intToEnumFacing(buf.readInt());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        writeTimeMachine(buf, tm);
        writeBlockPos(buf, pos);
        buf.writeInt(side.getIndex());
    }

    @SideOnly(Side.CLIENT)
    public static class OpenTMGUIHandler implements IMessageHandler<OpenTMGUI, IMessage> {
        @Override
        public IMessage onMessage(OpenTMGUI message, MessageContext ctx) {
            EntityPlayer player = Minecraft.getMinecraft().player;
            TimeTravelMod.proxy.displayTMGuiScreen(player, message.tm.hook(player.world, message.pos, message.side), message.pos, message.side);
            return null;
        }
    }
}
