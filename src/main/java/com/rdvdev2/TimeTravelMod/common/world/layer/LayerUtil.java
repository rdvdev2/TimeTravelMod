package com.rdvdev2.TimeTravelMod.common.world.layer;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.IExtendedNoiseRandom;
import net.minecraft.world.gen.LazyAreaLayerContext;
import net.minecraft.world.gen.OverworldGenSettings;
import net.minecraft.world.gen.area.IArea;
import net.minecraft.world.gen.area.IAreaFactory;
import net.minecraft.world.gen.area.LazyArea;
import net.minecraft.world.gen.layer.*;

import java.util.function.LongFunction;

import static net.minecraft.world.gen.layer.LayerUtil.getModdedBiomeSize;
import static net.minecraft.world.gen.layer.LayerUtil.repeat;

public class LayerUtil {

    protected static final int WARM_OCEAN = Registry.BIOME.getId(Biomes.WARM_OCEAN);
    protected static final int LUKEWARM_OCEAN = Registry.BIOME.getId(Biomes.LUKEWARM_OCEAN);
    protected static final int OCEAN = Registry.BIOME.getId(Biomes.OCEAN);
    protected static final int COLD_OCEAN = Registry.BIOME.getId(Biomes.COLD_OCEAN);
    protected static final int FROZEN_OCEAN = Registry.BIOME.getId(Biomes.FROZEN_OCEAN);
    protected static final int DEEP_WARM_OCEAN = Registry.BIOME.getId(Biomes.DEEP_WARM_OCEAN);
    protected static final int DEEP_LUKEWARM_OCEAN = Registry.BIOME.getId(Biomes.DEEP_LUKEWARM_OCEAN);
    protected static final int DEEP_OCEAN = Registry.BIOME.getId(Biomes.DEEP_OCEAN);
    protected static final int DEEP_COLD_OCEAN = Registry.BIOME.getId(Biomes.DEEP_COLD_OCEAN);
    protected static final int DEEP_FROZEN_OCEAN = Registry.BIOME.getId(Biomes.DEEP_FROZEN_OCEAN);

    public static Layer buildOldWestProcedure(long seed, WorldType worldType, OverworldGenSettings settings) {
        IAreaFactory<LazyArea> iareafactory = buildOldWestProcedure(worldType, settings, in -> {
            return new LazyAreaLayerContext(25, seed, in);
        });
        return new Layer(iareafactory);
    }

    public static <T extends IArea, C extends IExtendedNoiseRandom<T>> IAreaFactory<T> buildOldWestProcedure(WorldType worldType, OverworldGenSettings settings, LongFunction<C> contextFactory) {
        IAreaFactory<T> factory = IslandLayer.INSTANCE.apply(contextFactory.apply(1L));
        factory = ZoomLayer.FUZZY.apply(contextFactory.apply(2000L), factory);
        factory = AddIslandLayer.INSTANCE.apply(contextFactory.apply(1L), factory);
        factory = ZoomLayer.NORMAL.apply(contextFactory.apply(2001L), factory);
        factory = AddIslandLayer.INSTANCE.apply(contextFactory.apply(2L), factory);
        factory = AddIslandLayer.INSTANCE.apply(contextFactory.apply(50L), factory);
        factory = AddIslandLayer.INSTANCE.apply(contextFactory.apply(70L), factory);
        factory = RemoveTooMuchOceanLayer.INSTANCE.apply(contextFactory.apply(2L), factory);
        IAreaFactory<T> factory1 = OceanLayer.INSTANCE.apply(contextFactory.apply(2L));
        factory1 = repeat(2001L, ZoomLayer.NORMAL, factory1, 6, contextFactory);
        factory = AddIslandLayer.INSTANCE.apply(contextFactory.apply(3L), factory);
        factory = EdgeLayer.CoolWarm.INSTANCE.apply(contextFactory.apply(2L), factory);
        factory = EdgeLayer.Special.INSTANCE.apply(contextFactory.apply(3L), factory);
        factory = ZoomLayer.NORMAL.apply(contextFactory.apply(2002L), factory);
        factory = ZoomLayer.NORMAL.apply(contextFactory.apply(2003L), factory);
        factory = AddIslandLayer.INSTANCE.apply(contextFactory.apply(4L), factory);
        factory = DeepOceanLayer.INSTANCE.apply(contextFactory.apply(4L), factory);
        factory = repeat(1000L, ZoomLayer.NORMAL, factory, 0, contextFactory);
        int i = 4;
        int j = i;
        if (settings != null) {
            i = settings.getBiomeSize();
            j = settings.getRiverSize();
        }

        if (worldType == WorldType.LARGE_BIOMES) {
            i = 6;
        }

        i = getModdedBiomeSize(worldType, i);

        IAreaFactory<T> factory2 = repeat(1000L, ZoomLayer.NORMAL, factory, 0, contextFactory);
        factory2 = StartRiverLayer.INSTANCE.apply((IExtendedNoiseRandom)contextFactory.apply(100L), factory2);
        IAreaFactory<T> factory3 = getOldWestBiomeLayer(factory, settings, contextFactory); // Custom biomes
        IAreaFactory<T> factory4 = repeat(1000L, ZoomLayer.NORMAL, factory2, 2, contextFactory);
        factory3 = HillsLayer.INSTANCE.apply((IExtendedNoiseRandom)contextFactory.apply(1000L), factory3, factory4);
        factory2 = repeat(1000L, ZoomLayer.NORMAL, factory2, 2, contextFactory);
        factory2 = repeat(1000L, ZoomLayer.NORMAL, factory2, j, contextFactory);
        factory2 = RiverLayer.INSTANCE.apply((IExtendedNoiseRandom)contextFactory.apply(1L), factory2); // Custom rivers
        factory2 = SmoothLayer.INSTANCE.apply((IExtendedNoiseRandom)contextFactory.apply(1000L), factory2);

        for(int k = 0; k < i; ++k) {
            factory3 = ZoomLayer.NORMAL.apply((IExtendedNoiseRandom)contextFactory.apply((long)(1000 + k)), factory3);
            if (k == 0) {
                factory3 = AddIslandLayer.INSTANCE.apply((IExtendedNoiseRandom)contextFactory.apply(3L), factory3);
            }

            if (k == 1 || i == 1) {
                factory3 = ShoreLayer.INSTANCE.apply((IExtendedNoiseRandom)contextFactory.apply(1000L), factory3);
            }
        }

        factory3 = SmoothLayer.INSTANCE.apply((IExtendedNoiseRandom)contextFactory.apply(1000L), factory3);
        factory3 = MixRiverLayer.INSTANCE.apply((IExtendedNoiseRandom)contextFactory.apply(100L), factory3, factory2);
        factory3 = MixOceansLayer.INSTANCE.apply(contextFactory.apply(100L), factory3, factory1);
        return factory3;
    }

    static <T extends IArea, C extends IExtendedNoiseRandom<T>> IAreaFactory<T> getOldWestBiomeLayer(IAreaFactory<T> parentLayer, OverworldGenSettings chunkSettings, LongFunction<C> contextFactory) {
        parentLayer = (new OldWestBiomeLayer(chunkSettings)).apply(contextFactory.apply(200L), parentLayer);
        parentLayer = net.minecraft.world.gen.layer.LayerUtil.repeat(1000L, ZoomLayer.NORMAL, parentLayer, 2, contextFactory);
        parentLayer = EdgeBiomeLayer.INSTANCE.apply(contextFactory.apply(1000L), parentLayer);
        return parentLayer;
    }

    public static boolean isOcean(int biomeIn) {
        return biomeIn == WARM_OCEAN || biomeIn == LUKEWARM_OCEAN || biomeIn == OCEAN || biomeIn == COLD_OCEAN || biomeIn == FROZEN_OCEAN || biomeIn == DEEP_WARM_OCEAN || biomeIn == DEEP_LUKEWARM_OCEAN || biomeIn == DEEP_OCEAN || biomeIn == DEEP_COLD_OCEAN || biomeIn == DEEP_FROZEN_OCEAN;
    }
}
