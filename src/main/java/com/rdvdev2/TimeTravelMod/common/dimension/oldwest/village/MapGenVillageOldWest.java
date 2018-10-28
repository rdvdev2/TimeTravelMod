package com.rdvdev2.TimeTravelMod.common.dimension.oldwest.village;

import com.rdvdev2.TimeTravelMod.ModBiomes;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.MapGenVillage;

import java.util.Arrays;
import java.util.List;

public class MapGenVillageOldWest extends MapGenVillage {
    public static List<Biome> VILLAGE_SPAWN_BIOMES = Arrays.<Biome>asList(ModBiomes.OLDWEST);

    @Override
    public String getStructureName() {
        return "Old West Village";
    }
}
