package com.rdvdev2.TimeTravelMod.common.dimension.oldwest.village;

import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureVillagePieces;

public class StructureVillageOldWestPieces {
    public static void registerVillageOldWestPieces() {
        MapGenStructureIO.registerStructureComponent(StructureVillagePieces.Path.class, "ViSR");
    }
}
