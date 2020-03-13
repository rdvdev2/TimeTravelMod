package com.rdvdev2.TimeTravelMod.api.timemachine;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

import java.util.List;

/**
 * Template that defines the behaviour and the aspect of a Time Machine
 */
public interface TimeMachineTemplate {

    /**
     * Returns the cooldown time of the core
     * @return Cooldown time in game ticks (1s = 20t)
     */
    default int getCooldownTime() {
        return 400;
    }

    /**
     * Returns the Time Machine tier
     * @return The Time Machine tier
     */
    default int getTier() {
        return 1;
    };

    /**
     * Returns the position(s) where must be a TM Core relatively to a compatible Time Machine Controller facing north
     * @return Array of positions where must be a TM Core
     */
    List<BlockPos> coreBlocksPos();

    /**
     * Returns the position(s) where must be a TM Basic Block or a TM Upgrade relatively to a compatible Time Machine Controller facing north
     * @return Array of positions where must be a TM Basic Block or a TM Upgrade
     */
    List<BlockPos> basicBlocksPos();

    /**
     * Returns the position(s) where must be air relatively to a compatible Time Machine Controller facing north
     * @return Array of positions where must be air
     */
    List<BlockPos> airBlocksPos();

    /**
     * Returns the valid IBlockState(s) for TM Controller blocks
     * @return Array of valid IBlockStates for TM Controller blocks
     */
    BlockState[] getControllerBlocks();

    /**
     * Returns the valid IBlockState(s) for TM Blocks
     * @return Array of valid IBlockStates for TM Blocks
     */
    BlockState[] getCoreBlocks();

    /**
     * Returns the valid IBlockState(s) for TM Basic Blocks
     * @return Array of valid IBlockStates for TM Basic Blocks
     */
    BlockState[] getBasicBlocks();

    /**
     * Returns the maximum quantity of entities that the Time Machine can transport
     * @return The TM maximum entity load
     */
    default int getEntityMaxLoad() {
        return 1;
    };

    /**
     * When higher, more corruption will be caused by the time machine
     * @return The corruption multiplier
     */
    default int getCorruptionMultiplier() {
        return 1;
    };
}
