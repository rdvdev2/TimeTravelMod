package tk.rdvdev2.TimeTravelMod.common.networking;

import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import tk.rdvdev2.TimeTravelMod.ModRegistries;
import tk.rdvdev2.TimeTravelMod.TimeTravelMod;
import tk.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;
import tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.TimeMachineHookRunner;
import tk.rdvdev2.TimeTravelMod.util.CastingHelper;

import java.util.function.Supplier;

public class OpenTmGuiPKT {
    public OpenTmGuiPKT(){}

    public TimeMachine tm;
    public BlockPos pos;
    public EnumFacing side;

    public OpenTmGuiPKT(TimeMachine tm, BlockPos pos, EnumFacing side) {
        this.tm = tm instanceof TimeMachineHookRunner ? ((TimeMachineHookRunner)tm).removeHooks() : tm;
        this.pos = pos;
        this.side = side;
    }

    public static OpenTmGuiPKT decode(ByteBuf buf) {
        OpenTmGuiPKT pkt = new OpenTmGuiPKT();
        int size = buf.readInt();
        pkt.tm = TimeMachine.fromString(buf.readCharSequence(size, Charsets.UTF_8).toString());
        pkt.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        pkt.side = CastingHelper.intToEnumFacing(buf.readInt());
        return pkt;
    }

    public static void encode(OpenTmGuiPKT pkt, PacketBuffer buf) {
        String key = ModRegistries.timeMachinesRegistry.getKey(pkt.tm).toString();
        buf.writeInt(key.length());
        buf.writeCharSequence(pkt.tm.toString(), Charsets.UTF_8);
        buf.writeInt(pkt.pos.getX());
        buf.writeInt(pkt.pos.getY());
        buf.writeInt(pkt.pos.getZ());
        buf.writeInt(pkt.side.getIndex());
    }

    public static class Handler {
        public static void handle(OpenTmGuiPKT message, Supplier<NetworkEvent.Context> ctx) {
            TimeTravelMod.proxy.handleOpenTMGUI(message, ctx.get());
        }
    }
}
