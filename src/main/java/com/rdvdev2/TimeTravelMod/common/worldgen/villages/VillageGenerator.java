package com.rdvdev2.TimeTravelMod.common.worldgen.villages;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.Random;

public class VillageGenerator {
    private World world;
    private IVillage village;
    private int frequency;

    public VillageGenerator(World world, IVillage village) {
        this.world = world;
        this.village = village;
        this.frequency = village.frequency(this.world);
    }

    public void tryBuild(int chunkX, int chunkZ) {
        Chunk chunk = world.getChunkFromChunkCoords(chunkX, chunkZ);
        Random cRandom = chunk.getRandomWithSeed(world.getSeed());
        if (checkVillage(cRandom)) {
           BlockPos villagePos = getVillagePos(cRandom);
        }
    }

    private boolean checkVillage(Random cRandom) {
        if (cRandom.nextInt(frequency) == 1) {
            return true;
        } else {
            return false;
        }
    }

    private BlockPos getVillagePos(Random cRandom) {
        int x = cRandom.nextInt(16) - 8;
        int z = cRandom.nextInt(16) - 8;
        int y = world.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z)).getY();

        return new BlockPos(x, y, z).up();
    }
}
