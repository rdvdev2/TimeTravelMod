package com.rdvdev2.TimeTravelMod;

import com.rdvdev2.TimeTravelMod.common.dimension.oldwest.village.MapGenVillageOldWest;
import com.rdvdev2.TimeTravelMod.common.dimension.oldwest.village.StructureVillageOldWestPieces;
import net.minecraft.world.gen.structure.MapGenStructureIO;

public class ModStructures {
    public static void init() {
        MapGenStructureIO.registerStructure(MapGenVillageOldWest.Start.class, "timetravelmod:oldwestvillage");
        StructureVillageOldWestPieces.registerVillageOldWestPieces();
    }
}
