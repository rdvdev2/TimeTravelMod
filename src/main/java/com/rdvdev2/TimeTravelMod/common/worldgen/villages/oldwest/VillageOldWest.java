package com.rdvdev2.TimeTravelMod.common.worldgen.villages.oldwest;

import com.rdvdev2.TimeTravelMod.ModDimensions;
import com.rdvdev2.TimeTravelMod.common.worldgen.villages.IVillage;
import net.minecraft.world.World;

public class VillageOldWest implements IVillage {

    @Override
    public int frequency(World world) {
        if (world.provider.getDimension() == ModDimensions.OldWestId) {
            return 100;
        } else {
            return 0;
        }
    }
}
