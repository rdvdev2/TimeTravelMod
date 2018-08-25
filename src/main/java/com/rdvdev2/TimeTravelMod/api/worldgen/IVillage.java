package com.rdvdev2.TimeTravelMod.api.worldgen;

import net.minecraft.block.state.IBlockState;
import net.minecraft.world.World;

/**
 * Defines how is a Village for the mod's custom village generator
 */
public interface IVillage {

    /**
     * Returns the frequency of the village in a given world. A village will be generated in one every x chunks
     * @param world The world this village is trying to be generated
     * @return The frequency of the village
     */
    int frequency(World world);

    /**
     * Returns all the possible structures in the village
     * @return An array with all the possible structures
     */
    IStructure[] getStructures();

    /**
     * Returns the village's street definition
     * @return The village's street definition
     */
    IStructure.IStreet getStreet();

    /**
     * Defines how is a village structure
     */
    interface IStructure {

        /**
         * Returns the weight of this structure in the village in a given world. The possibility of the structure to be spawned is calculated dividing the structure's weight by the sum of all the structures' weights.
         * @param world The world where this village is being generated
         * @return This structure's weight
         */
        int getWeight(World world);

        /**
         * Defines how is a village street
         */
        interface IStreet extends IStructure {
            @Override
            default int getWeight(World world) {return 0;};

            /**
             * Returns the width of the streets
             * @return The width of the streets
             */
            int getWidth();

            /**
             * Returns the space left between the streets and the structures. This space is not modified when building the village.
             * @return
             */
            int getPadding();

            /**
             * Returns the block used to build the streets
             * @return The default building block
             */
            IBlockState getDefaultMaterial();

            /**
             * Returns the block used to build the streets when they are over a fluid or a gap
             * @return The bridge building material
             */
            IBlockState getBridgeMaterial();

            /**
             * Returns the height offset used when building a normal street
             * @return The Y offset
             */
            default int getDefaultYOffset() {return 0;}

            /**
             * Returns the height offset used when building a bridged street
             * @return The Y offset
             */
            default int getBridgeYOffset() {return 0;}

            /**
             * Returns the minimum size of the village (Size is handled as a radius)
             * @return The minimum village size
             */
            default int getMinSize() {return 0;}

            /**
             * Returns the maximum size of the village (Size is handled as a radius)
             * @return The maximum village size
             */
            default int getMaxSize() {return 0;}
        }
    }

}
