package com.rdvdev2.TimeTravelMod.common.dimension.oldwest.village;

import com.rdvdev2.TimeTravelMod.ModBiomes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraft.world.gen.structure.StructureVillagePieces;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MapGenVillageOldWest extends MapGenStructure
{
    /** A list of all the biomes villages can spawn in. */
    public static List<Biome> VILLAGE_SPAWN_BIOMES = Arrays.<Biome>asList(ModBiomes.OLDWEST);
    /** None */
    private int size;
    private int averageSpacing;

    public MapGenVillageOldWest()
    {
        this.averageSpacing = 32;
    }

    public MapGenVillageOldWest(Map<String, String> map)
    {
        this();

        for (Map.Entry<String, String> entry : map.entrySet())
        {
            if (((String)entry.getKey()).equals("size"))
            {
                this.size = MathHelper.getInt(entry.getValue(), this.size, 0);
            }
            else if (((String)entry.getKey()).equals("averageSpacing"))
            {
                this.averageSpacing = MathHelper.getInt(entry.getValue(), this.averageSpacing, 9);
            }
        }
    }

    public String getStructureName()
    {
        return "Old West Village";
    }

    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ)
    {
        int i = chunkX;
        int j = chunkZ;

        if (chunkX < 0)
        {
            chunkX -= this.averageSpacing - 1;
        }

        if (chunkZ < 0)
        {
            chunkZ -= this.averageSpacing - 1;
        }

        int k = chunkX / this.averageSpacing;
        int l = chunkZ / this.averageSpacing;
        Random random = this.world.setRandomSeed(k, l, 10387312);
        k = k * this.averageSpacing;
        l = l * this.averageSpacing;
        k = k + random.nextInt(this.averageSpacing - 8);
        l = l + random.nextInt(this.averageSpacing - 8);

        if (i == k && j == l)
        {
            boolean flag = this.world.getBiomeProvider().areBiomesViable(i * 16 + 8, j * 16 + 8, 0, VILLAGE_SPAWN_BIOMES);

            if (flag)
            {
                return true;
            }
        }

        return false;
    }

    public BlockPos getNearestStructurePos(World worldIn, BlockPos pos, boolean findUnexplored)
    {
        this.world = worldIn;
        return findNearestStructurePosBySpacing(worldIn, this, pos, this.averageSpacing, 8, 10387312, false, 100, findUnexplored);
    }

    protected StructureStart getStructureStart(int chunkX, int chunkZ)
    {
        return new MapGenVillageOldWest.Start(this.world, this.rand, chunkX, chunkZ, this.size);
    }

    public static class Start extends StructureStart
    {
        /** well ... thats what it does */
        private boolean hasMoreThanTwoComponents;

        public Start()
        {
        }

        public Start(World worldIn, Random rand, int x, int z, int size)
        {
            super(x, z);
            List<StructureVillagePieces.PieceWeight> list = StructureVillageOldWestPieces.getStructureVillageWeightedPieceList(rand, size);
            StructureVillagePieces.Start start = new StructureVillagePieces.Start(worldIn.getBiomeProvider(), 0, rand, (x << 4) + 2, (z << 4) + 2, list, size);
            this.components.add(start);
            start.buildComponent(start, this.components, rand);
            List<StructureComponent> pendingRoads = start.pendingRoads;
            List<StructureComponent> pendingHouses = start.pendingHouses;

            while (!pendingRoads.isEmpty() || !pendingHouses.isEmpty())
            {
                if (pendingRoads.isEmpty())
                {
                    int i = rand.nextInt(pendingHouses.size());
                    StructureComponent structurecomponent = pendingHouses.remove(i);
                    structurecomponent.buildComponent(start, this.components, rand);
                }
                else
                {
                    int j = rand.nextInt(pendingRoads.size());
                    StructureComponent structurecomponent2 = pendingRoads.remove(j);
                    structurecomponent2.buildComponent(start, this.components, rand);
                }
            }

            this.updateBoundingBox();
            int nonRoadComponentCount = 0;

            for (StructureComponent component : this.components)
            {
                if (!(component instanceof StructureVillagePieces.Road))
                {
                    ++nonRoadComponentCount;
                }
            }

            this.hasMoreThanTwoComponents = nonRoadComponentCount > 2;
        }

        /**
         * currently only defined for Villages, returns true if Village has more than 2 non-road components
         */
        public boolean isSizeableStructure()
        {
            return this.hasMoreThanTwoComponents;
        }

        public void writeToNBT(NBTTagCompound tagCompound)
        {
            super.writeToNBT(tagCompound);
            tagCompound.setBoolean("Valid", this.hasMoreThanTwoComponents);
        }

        public void readFromNBT(NBTTagCompound tagCompound)
        {
            super.readFromNBT(tagCompound);
            this.hasMoreThanTwoComponents = tagCompound.getBoolean("Valid");
        }
    }
}