package com.rdvdev2.TimeTravelMod.common.dimension;

import com.rdvdev2.TimeTravelMod.ModDimensions;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.gen.ChunkGeneratorDebug;
import net.minecraft.world.gen.ChunkGeneratorHell;
import net.minecraft.world.gen.ChunkGeneratorOverworld;
import net.minecraft.world.gen.IChunkGenerator;

public class WorldProviderOldWest extends WorldProvider {

    public static int dimId = 20;

    public WorldProviderOldWest() {
        super();
    }

    @Override
    public DimensionType getDimensionType() {
        return ModDimensions.OLD_WEST;
    }

    public void registerWorldChunkManager() {
        this.biomeProvider = new BiomeProviderSingle(Biomes.DESERT);
        this.setDimension(ModDimensions.OldWestId);
        this.setAllowedSpawnTypes(true,true);
    }

    @Override
    public IChunkGenerator createChunkGenerator() {
        return new ChunkGeneratorOldWest(this.world);
    }

    public Biome getBiomeGenForCoords(BlockPos pos){
        return Biomes.DESERT;
    }

    @Override
    public boolean canRespawnHere() {
        return true;
    }

    @Override
    public int getRespawnDimension(EntityPlayerMP player) {
        return ModDimensions.OldWestId;
    }

    @Override
    public boolean isSurfaceWorld() {
        return true;
    }

    /*@Override
    public String getWelcomeMessage() {
        return "Taking a step back";
    }*/


}
