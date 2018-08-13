package com.rdvdev2.TimeTravelMod;

import com.rdvdev2.TimeTravelMod.common.dimension.WorldProviderOldWest;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;

public class ModDimensions {

    public static final int OldWestId = WorldProviderOldWest.dimId;
    public static final DimensionType OLD_WEST = DimensionType.register("OLDWEST", "_oldwest", OldWestId, WorldProviderOldWest.class, false);

    public static void init() {
        registerDimensions();
    }

    private static void registerDimensions() {
        DimensionManager.registerDimension(OldWestId, OLD_WEST);

    }
}
