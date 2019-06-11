package tk.rdvdev2.TimeTravelMod.common.world.dimension.oldwest;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.structure.MineshaftConfig;
import net.minecraft.world.gen.feature.structure.MineshaftStructure;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.placement.ChanceConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import tk.rdvdev2.TimeTravelMod.ModBlocks;
import tk.rdvdev2.TimeTravelMod.ModFeatures;

public final class OldWestBiome extends Biome { // TODO: Massive rewrite
    public OldWestBiome() {
        // Vanilla desert start
        super((new Biome.Builder()).func_222351_a(SurfaceBuilder.field_215396_G, SurfaceBuilder.field_215429_z).precipitation(Biome.RainType.NONE).category(Biome.Category.DESERT).depth(0.125F).scale(0.05F).temperature(2.0F).downfall(0.0F).waterColor(4159204).waterFogColor(329011).parent((String)null));
        this.addStructure(Feature.field_214550_p, new VillageConfig("village/desert/town_centers", 6)); // TODO: Custom village
        //this.addStructure(Feature.field_214536_b, new PillagerOutpostConfig(0.004D));
        //this.addStructure(Feature.DESERT_PYRAMID, IFeatureConfig.NO_FEATURE_CONFIG);
        this.addStructure(Feature.MINESHAFT, new MineshaftConfig(0.004D, MineshaftStructure.Type.NORMAL)); // TODO: Custom mineshaft
        //this.addStructure(Feature.STRONGHOLD, IFeatureConfig.NO_FEATURE_CONFIG);
        DefaultBiomeFeatures.func_222300_a(this);
        DefaultBiomeFeatures.func_222295_c(this);
        DefaultBiomeFeatures.func_222301_e(this);
        DefaultBiomeFeatures.func_222335_f(this);
        DefaultBiomeFeatures.func_222326_g(this);
        DefaultBiomeFeatures.func_222288_h(this);
        DefaultBiomeFeatures.func_222282_l(this);
        DefaultBiomeFeatures.func_222342_U(this);
        DefaultBiomeFeatures.func_222348_W(this);
        DefaultBiomeFeatures.func_222334_S(this);
        DefaultBiomeFeatures.func_222315_Z(this);
        DefaultBiomeFeatures.func_222292_ad(this);
        DefaultBiomeFeatures.func_222337_am(this);
        DefaultBiomeFeatures.func_222281_af(this);
        DefaultBiomeFeatures.func_222297_ap(this);
        this.addSpawn(EntityClassification.CREATURE, new Biome.SpawnListEntry(EntityType.RABBIT, 4, 2, 3));
        this.addSpawn(EntityClassification.AMBIENT, new Biome.SpawnListEntry(EntityType.BAT, 10, 8, 8));
        this.addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(EntityType.SPIDER, 100, 4, 4));
        this.addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(EntityType.SKELETON, 100, 4, 4));
        this.addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(EntityType.CREEPER, 100, 4, 4));
        this.addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(EntityType.SLIME, 100, 4, 4));
        this.addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(EntityType.ENDERMAN, 10, 1, 4));
        this.addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(EntityType.WITCH, 5, 1, 1));
        this.addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(EntityType.ZOMBIE, 19, 4, 4));
        this.addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(EntityType.ZOMBIE_VILLAGER, 1, 1, 1));
        this.addSpawn(EntityClassification.MONSTER, new Biome.SpawnListEntry(EntityType.HUSK, 80, 4, 4));

        // Time Travel Mod start
        this.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.func_222280_a(Feature.MINABLE, new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NATURAL_STONE, ModBlocks.timeCrystalOre.getDefaultState(), 4), Placement.field_215028_n, new CountRangeConfig(1, 0, 0, 16)));
        this.addFeature(GenerationStage.Decoration.SURFACE_STRUCTURES, Biome.func_222280_a(ModFeatures.GUNPOWDER, NoFeatureConfig.NO_FEATURE_CONFIG, Placement.field_215023_i, new ChanceConfig(100)));
    }
}
