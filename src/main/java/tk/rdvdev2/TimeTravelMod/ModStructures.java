package tk.rdvdev2.TimeTravelMod;

import net.minecraft.world.gen.structure.MapGenStructureIO;
import tk.rdvdev2.TimeTravelMod.common.dimension.oldwest.village.MapGenVillageOldWest;
import tk.rdvdev2.TimeTravelMod.common.dimension.oldwest.village.StructureVillageOldWestPieces;

public class ModStructures {
    public static void init() {
        MapGenStructureIO.registerStructure(MapGenVillageOldWest.Start.class, "timetravelmod:oldwestvillage");
        StructureVillageOldWestPieces.registerVillageOldWestPieces();
    }
}
