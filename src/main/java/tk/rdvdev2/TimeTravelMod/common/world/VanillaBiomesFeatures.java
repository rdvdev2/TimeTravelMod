package tk.rdvdev2.TimeTravelMod.common.world;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraftforge.registries.ForgeRegistries;
import tk.rdvdev2.TimeTravelMod.ModBlocks;

public class VanillaBiomesFeatures { // TODO: Worldgen rewritten

    public static void register() {
        // Time Crystal Ores
        registerGenerator(GenerationStage.Decoration.UNDERGROUND_ORES, new CompositeFeature(
                Feature.MINABLE,
                new MinableConfig(MinableConfig.IS_ROCK, ModBlocks.timeCrystalOre.getDefaultState(), 4),
                Biome.COUNT_RANGE,
                new CountRangeConfig(1, 0, 0, 16)
        ));
    }

    private static void registerGenerator(GenerationStage.Decoration stage, CompositeFeature feature) {
        for (Biome biome : ForgeRegistries.BIOMES.getValues()) {
            if (biome.getCategory() != Biome.Category.NETHER && biome.getCategory() != Biome.Category.THEEND) {
                biome.addFeature(stage, feature);
            }
        }
    }
}
