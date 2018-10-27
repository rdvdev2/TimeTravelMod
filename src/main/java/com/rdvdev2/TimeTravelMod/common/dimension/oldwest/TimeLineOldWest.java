package com.rdvdev2.TimeTravelMod.common.dimension.oldwest;

import com.rdvdev2.TimeTravelMod.ModBiomes;
import com.rdvdev2.TimeTravelMod.api.dimension.TimeLine;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.gen.IChunkGenerator;

public class TimeLineOldWest extends TimeLine {

    public TimeLineOldWest() {
        super(20, DimensionType.register("OLDWEST", "_oldwest", 20, TimeLineOldWest.class, false), 1);
    }

    public void init() {
        super.init();
        this.biomeProvider = new BiomeProviderSingle(ModBiomes.OLDWEST);
        this.setAllowedSpawnTypes(true,true);
        this.hasSkyLight = true;
    }

    @Override
    public IChunkGenerator createChunkGenerator() {
        return new ChunkGeneratorOldWest(this.world);
    }

    public Biome getBiomeGenForCoords(BlockPos pos){
        return ModBiomes.OLDWEST;
    }

}
