package com.rdvdev2.TimeTravelMod;

import com.rdvdev2.TimeTravelMod.common.networking.DimensionTP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class ModPacketHandler {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("timetravelmod");

    public static void init() {
        INSTANCE.registerMessage(DimensionTP.DimensionTPHandler.class, DimensionTP.class, 0, Side.SERVER);
    }
}
