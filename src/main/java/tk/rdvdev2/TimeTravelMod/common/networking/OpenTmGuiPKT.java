package tk.rdvdev2.TimeTravelMod.common.networking;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import tk.rdvdev2.TimeTravelMod.TimeTravelMod;
import tk.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class OpenTmGuiPKT {
    public OpenTmGuiPKT(){
        this.additionalEntities = new HashSet<>();
    }

    public TimeMachine tm;
    public BlockPos pos;
    public Direction side;
    public Set<UUID> additionalEntities;

    public OpenTmGuiPKT(TimeMachine tm, BlockPos pos, Direction side, UUID... aditionalEntities) {
        this();
        this.tm = tm.removeHooks();
        this.pos = pos;
        this.side = side;
        if (aditionalEntities != null && aditionalEntities.length != 0) this.additionalEntities = Arrays.stream(aditionalEntities).collect(Collectors.toSet());
    }

    public static OpenTmGuiPKT decode(PacketBuffer buf) {
        OpenTmGuiPKT pkt = new OpenTmGuiPKT();
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

    public static void encode(OpenTmGuiPKT pkt, PacketBuffer buf) {
        buf.writeRegistryId(pkt.tm);
        buf.writeBlockPos(pkt.pos);
        buf.writeEnumValue(pkt.side);
        buf.writeInt(pkt.additionalEntities.size());
        pkt.additionalEntities.forEach(buf::writeUniqueId);
    }

    public static void handle(OpenTmGuiPKT message, Supplier<NetworkEvent.Context> ctx) {
        TimeTravelMod.PROXY.handleOpenTMGUI(message, ctx.get());
    }
}
