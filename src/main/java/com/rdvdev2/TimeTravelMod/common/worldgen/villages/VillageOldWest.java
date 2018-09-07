package com.rdvdev2.TimeTravelMod.common.worldgen.villages;

import com.rdvdev2.TimeTravelMod.ModTimeLines;
import com.rdvdev2.TimeTravelMod.api.worldgen.IVillage;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;

public class VillageOldWest implements IVillage {

    @Override
    public int frequency(World world) {
        if (world.provider.getDimension() == ModTimeLines.oldWest.getDimId()) {
            return 100;
        } else {
            return 0;
        }
    }

    @Override
    public IStructure[] getStructures() {
        return new IStructure[0];
    }

    @Override
    public IStructure.IStreet getStreet() {
        return new IStructure.IStreet() {
            @Override
            public int getWidth() {
                return 8;
            }

            @Override
            public int getPadding() {
                return 0;
            }

            @Override
            public IBlockState getDefaultMaterial() {
                return Blocks.SAND.getDefaultState();
            }

            @Override
            public IBlockState getBridgeMaterial() {
                return Blocks.PLANKS.getDefaultState();
            }
        };
    }
}
