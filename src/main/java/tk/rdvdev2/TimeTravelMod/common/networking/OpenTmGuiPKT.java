package tk.rdvdev2.TimeTravelMod.common.networking;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import tk.rdvdev2.TimeTravelMod.TimeTravelMod;
import tk.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;
import tk.rdvdev2.TimeTravelMod.common.timemachine.TimeMachineHookRunner;

import java.util.function.Supplier;

public class OpenTmGuiPKT {
    public OpenTmGuiPKT(){}

    public TimeMachine tm;
    public BlockPos pos;
    public Direction side;

    public OpenTmGuiPKT(TimeMachine tm, BlockPos pos, Direction side) {
        this.tm = tm instanceof TimeMachineHookRunner ? ((TimeMachineHookRunner)tm).removeHooks() : tm;
        this.pos = pos;
        this.side = side;
    }

    public static OpenTmGuiPKT decode(PacketBuffer buf) {
        OpenTmGuiPKT pkt = new OpenTmGuiPKT();
        pkt.tm = buf.readRegistryIdSafe(TimeMachine.class);
        pkt.pos = buf.readBlockPos();
        pkt.side = buf.readEnumValue(Direction.class);
        return pkt;
    }

    public static void encode(OpenTmGuiPKT pkt, PacketBuffer buf) {
        buf.writeRegistryId(pkt.tm);
        buf.writeBlockPos(pkt.pos);
        buf.writeEnumValue(pkt.side);
    }

    public static class Handler {
        public static void handle(OpenTmGuiPKT message, Supplier<NetworkEvent.Context> ctx) {
            TimeTravelMod.proxy.handleOpenTMGUI(message, ctx.get());
        }
    }
}
