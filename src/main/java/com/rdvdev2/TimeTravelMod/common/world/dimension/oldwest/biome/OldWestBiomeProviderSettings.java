package com.rdvdev2.TimeTravelMod.common.world.dimension.oldwest.biome;

import net.minecraft.world.biome.provider.IBiomeProviderSettings;
import net.minecraft.world.gen.OverworldGenSettings;
import net.minecraft.world.storage.WorldInfo;

public class OldWestBiomeProviderSettings implements IBiomeProviderSettings {
    private WorldInfo worldInfo;
    private OverworldGenSettings generatorSettings;

    public OldWestBiomeProviderSettings(WorldInfo worldInfo) {
        this.setWorldInfo(worldInfo);
    }

    public OldWestBiomeProviderSettings setWorldInfo(WorldInfo worldInfo) {
        this.worldInfo = worldInfo;
        return this;
    }

    public OldWestBiomeProviderSettings setGeneratorSettings(OverworldGenSettings generatorSettings) {
        this.generatorSettings = generatorSettings;
        return this;
    }

    public WorldInfo getWorldInfo() {
        return worldInfo;
    }

    public OverworldGenSettings getGeneratorSettings() {
        return generatorSettings;
    }
}
