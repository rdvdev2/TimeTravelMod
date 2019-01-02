package tk.rdvdev2.TimeTravelMod;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import tk.rdvdev2.TimeTravelMod.common.networking.DimensionTP;
import tk.rdvdev2.TimeTravelMod.common.networking.OpenTMGUI;

public class ModPacketHandler {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("timetravelmod");

    public static void init() {
        int d = 0;
        INSTANCE.registerMessage(DimensionTP.DimensionTPHandler.class, DimensionTP.class, d++, Side.SERVER);
        INSTANCE.registerMessage(OpenTMGUI.OpenTMGUIHandler.class, OpenTMGUI.class, d++, Side.CLIENT );
    }
}
