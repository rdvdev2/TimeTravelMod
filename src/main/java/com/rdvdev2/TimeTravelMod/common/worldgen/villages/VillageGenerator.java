package com.rdvdev2.TimeTravelMod.common.worldgen.villages;

import com.rdvdev2.TimeTravelMod.api.worldgen.IVillage;
import net.minecraft.block.BlockLiquid;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.Random;

public class VillageGenerator {
    private World world;
    private IVillage village;
    private IVillage.IStructure.IStreet street;
    private IVillage.IStructure[] structures;
    private int frequency;

    public VillageGenerator(World world, IVillage village) {
        this.world = world;
        this.village = village;
        this.street = village.getStreet();
        this.structures = village.getStructures();
        this.frequency = village.frequency(this.world);
    }

    public void tryBuild(int chunkX, int chunkZ) {
        Chunk chunk = world.getChunkFromChunkCoords(chunkX, chunkZ);
        Random cRandom = chunk.getRandomWithSeed(world.getSeed());
        if (checkVillage(cRandom)) {
           BlockPos villagePos = getVillagePos(cRandom);
           build(villagePos);
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

    private void build(BlockPos villagePos) {
        int x1, x2, z1, z2;

        // Security  checks
        if (village.getStreet().getWidth() <= 0) {
            throw new IllegalArgumentException("Illegal village streets width. The value cannot be minor than one");
        }

        // Build a street cross in the center of the village
        switch (village.getStreet().getWidth() % 2) {
            case 0:
                x1 = villagePos.getX() - (village.getStreet().getWidth() / 2);
                x2 = villagePos.getX() + (village.getStreet().getWidth() / 2);
                z1 = villagePos.getZ() - (village.getStreet().getWidth() / 2);
                z2 = villagePos.getZ() + (village.getStreet().getWidth() / 2);
                break;
            case 1:
                x1 = (int) (villagePos.getX() - ((float)village.getStreet().getWidth() / 2F) - 0.5);
                x2 = (int) (villagePos.getX() + ((float)village.getStreet().getWidth() / 2F) + 0.5);
                z1 = (int) (villagePos.getZ() - ((float)village.getStreet().getWidth() / 2F) - 0.5);
                z2 = (int) (villagePos.getZ() + ((float)village.getStreet().getWidth() / 2F) + 0.5);
                break;
            default:
                x1 = 0;
                x2 = 0;
                z1 = 0;
                z2 = 0;
        }

        for (int x = x1; x <= x2; x++) {
            for (int z = z1; z <= z2; z++) {
                placeStreetBlock(x, z);
            }
        }
    }

    private void placeStreetBlock(int x, int z) {
        BlockPos pos = world.getTopSolidOrLiquidBlock(new BlockPos(x, 0, z));
        if (world.getBlockState(pos).getBlock() instanceof BlockLiquid) {
            world.setBlockState(pos.add(0, village.getStreet().getBridgeYOffset(), 0), village.getStreet().getBridgeMaterial());
        } else {
            world.setBlockState(pos.add(0, village.getStreet().getDefaultYOffset(), 0), village.getStreet().getDefaultMaterial());
        }
    }
}
