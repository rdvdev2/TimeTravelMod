package tk.rdvdev2.TimeTravelMod;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import tk.rdvdev2.TimeTravelMod.common.networking.DimensionTP;

public class ModPacketHandler {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("timetravelmod");

    public static void init() {
        INSTANCE.registerMessage(DimensionTP.DimensionTPHandler.class, DimensionTP.class, 0, Side.SERVER);
    }
}
