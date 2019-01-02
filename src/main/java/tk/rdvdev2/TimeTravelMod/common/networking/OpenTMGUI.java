package tk.rdvdev2.TimeTravelMod.common.networking;

import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import tk.rdvdev2.TimeTravelMod.ModRegistries;
import tk.rdvdev2.TimeTravelMod.TimeTravelMod;
import tk.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;
import tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.TimeMachineHookRunner;
import tk.rdvdev2.TimeTravelMod.util.CastingHelper;

public class OpenTMGUI implements IMessage {
    public OpenTMGUI(){}

    public TimeMachine tm;
    public BlockPos pos;
    public EnumFacing side;

    public OpenTMGUI(TimeMachine tm, BlockPos pos, EnumFacing side) {
        this.tm = tm instanceof TimeMachineHookRunner ? ((TimeMachineHookRunner)tm).removeHooks() : tm;
        this.pos = pos;
        this.side = side;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int size = buf.readInt();
        tm = TimeMachine.fromString(buf.readCharSequence(size, Charsets.UTF_8).toString());
        pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        side = CastingHelper.intToEnumFacing(buf.readInt());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        String key = ModRegistries.timeMachinesRegistry.getKey(tm).toString();
        buf.writeInt(key.length());
        buf.writeCharSequence(tm.toString(), Charsets.UTF_8);
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
        buf.writeInt(side.getIndex());
    }

    public static class OpenTMGUIHandler implements IMessageHandler<OpenTMGUI, IMessage> {
        @Override
        public IMessage onMessage(OpenTMGUI message, MessageContext ctx) {
            TimeTravelMod.proxy.handleOpenTMGUI(message, ctx);
            return null;
        }
    }
}
