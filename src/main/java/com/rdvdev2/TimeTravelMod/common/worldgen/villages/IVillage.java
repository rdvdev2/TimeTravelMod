package com.rdvdev2.TimeTravelMod.common.worldgen.villages;

import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;

// Defines a village for the VillageGenerator
public interface IVillage {

        // Returns it's frequency in a given world
        // A village will be generated every x chunks
        int frequency(World world);

        // Returns an array of the possible structures in the village
        IStructure[] getStructures();

        // Returns the village street object
        IStructure.IStreet getStreet();

        // Defines a village structure for the VillageGenerator
        interface IStructure {

            // Returns the weight of this structure to be built in a given world
            int getWeight(World world);

        // Defines the village streets
        interface IStreet extends IStructure {
            @Override
            default int getWeight(World world) {return 0;};

            // Returns the width of the streets
            int getWidth();

            // Returns the padding between the street and the structures
            int getPadding();

            // Returns the block used to build the street
            IBlockState getDefaultMaterial();

            // Returnes the block used to build bridges in the village
            IBlockState getBridgeMaterial();

            // Returns the y offset of the streets
            default int getDefaultYOffset() {return 0;}

            // Returns the y offset of the bridged streets
            default int getBridgeYOffset() {return 0;}

            // Returns the minimum size of the village (Size is handled as a radius)
            default int getMinSize() {return 0;}

            // Returns the maximum size of the village (Size is handled as a radius)
            default int getMaxSize() {return 0;}

            // Returns
        }
    }

}
