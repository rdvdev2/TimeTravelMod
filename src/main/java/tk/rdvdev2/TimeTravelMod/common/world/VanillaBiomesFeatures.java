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

public class VanillaBiomesFeatures { // TODO: Worldgen rewritten

    public static void register() {
        // Time Crystal Ores
        registerGenerator(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.func_222280_a(
                Feature.MINABLE,
                new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, ModBlocks.timeCrystalOre.getDefaultState(), 4),
                Placement.field_215028_n,
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
