package tk.rdvdev2.TimeTravelMod.common.world;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.registries.ForgeRegistries;
import tk.rdvdev2.TimeTravelMod.ModBlocks;

public class VanillaBiomesFeatures {

    public static void register() {
        // Time Crystal Ores
        registerGenerator(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createDecoratedFeature(
                Feature.ORE,
                new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, ModBlocks.TIME_CRYSTAL_ORE.getDefaultState(), 4),
                Placement.COUNT_RANGE,
                new CountRangeConfig(1, 0, 0, 16))
        );
    }

    private static void registerGenerator(GenerationStage.Decoration stage, ConfiguredFeature feature) {
        for (Biome biome : ForgeRegistries.BIOMES.getValues()) {
            if (biome.getCategory() != Biome.Category.NETHER && biome.getCategory() != Biome.Category.THEEND) {
                biome.addFeature(stage, feature);
            }
        }
    }
}
