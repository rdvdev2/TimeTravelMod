package com.rdvdev2.TimeTravelMod.common.worldgen.villages;

import com.rdvdev2.TimeTravelMod.api.worldgen.IVillage;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Random;

public class VillageGenerator {
    private World world;
    private IVillage village;
    private IVillage.IStructure.IStreet street;
    private IVillage.IStructure[] structures;
    private int frequency;
    private Chunk currentChunk;
    private Random cRandom;
    private VillageSchematic[] schematics = new VillageSchematic[0];

    public VillageGenerator(World world, IVillage village) {
        this.world = world;
        this.village = village;
        this.street = village.getStreet();
        this.structures = village.getStructures();
        this.frequency = village.frequency(this.world);
    }

    public void run(int chunkX, int chunkZ) {
        currentChunk = world.getChunkFromChunkCoords(chunkX, chunkZ);
        cRandom = currentChunk.getRandomWithSeed(world.getSeed());
        if (villageShouldBuild())
            generate();
        place();
    }

    private void place() {
        if(shouldPlace()) {
            VillageSchematic schematic = getChunkSchematic();
            schematic.applyHeights();
            schematic.apply();
        }
    }

    private VillageSchematic getChunkSchematic() {
        // TODO: Create a method that returns the schematic of the current chunk
        return null;
    }

    private boolean shouldPlace() {
        // TODO: Create a method that search if a chunk is included in an schematic
        return false;
    }

    public void generate() {
        VillageSchematic schematic = getNewVillage(getVillagePos());
        generateBaseStreet(schematic);
        boolean keepGenerating = true;
        while(keepGenerating) {
            // TODO: Create the village generation main loop
        }

    }

    private void generateBaseStreet(VillageSchematic schematic) {
        // TODO: Create the base street generation algorithm
    }

    private boolean villageShouldBuild() {
        if (cRandom.nextInt(frequency) == 1) {
            return true;
        } else {
            return false;
        }
    }

    private BlockPos getVillagePos() {
        int x = cRandom.nextInt(16);
        int z = cRandom.nextInt(16);
        int y = world.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z)).getY();

        return new BlockPos(x, y, z);
    }

    private VillageSchematic getNewVillage(BlockPos villagePos) {
        int id = schematics.length;
        ArrayUtils.add(schematics, new VillageSchematic(village.getMaxSize(), villagePos));
        return schematics[id];
    }

    private class VillageSchematic {
        private IBlockState[][][] schematic;
        private VillageBlockType[][][] type;
        private int schematicOffset;
        private int centerChunkX, centerChunkZ;
        private BlockPos centerPos;

        public VillageSchematic(int maxRadius, BlockPos villagePos) {
            schematic = new IBlockState[maxRadius*2][256][maxRadius*2];
            type = new VillageBlockType[maxRadius*2][256][maxRadius*2];
            schematicOffset = maxRadius;
            centerChunkX = currentChunk.x;
            centerChunkZ = currentChunk.z;
            centerPos = villagePos;
        }

        public void setBlock(BlockPos pos, IBlockState blockState, VillageBlockType type) {
            schematic[pos.getX() - schematicOffset][pos.getY()][pos.getZ() - schematicOffset] = blockState;
            this.type[pos.getX() - schematicOffset][pos.getY()][pos.getZ() - schematicOffset] = type;
        }

        public void applyHeights() {
            // TODO: Create a method that analyses the chunk and applies the height map to the schematic
        }

        public void apply() {
            // TODO: Create a method that modifies the world to fit the schematic and builds it
        }
    }
}
