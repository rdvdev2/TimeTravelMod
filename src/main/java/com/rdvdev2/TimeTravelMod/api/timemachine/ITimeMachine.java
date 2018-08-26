package com.rdvdev2.TimeTravelMod.api.timemachine;

import com.rdvdev2.TimeTravelMod.ModBlocks;
import com.rdvdev2.TimeTravelMod.TimeTravelMod;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.ArrayUtils;

/**
 * Defines the behaviour and the aspect of a Time Machine
 */
public interface ITimeMachine {

    /**
     * Gets the Time Machine ID in the registry
     * @return The Time Machine ID in the registry
     */
    int getId();

    /**
     * Sets the Time Machine ID in the registry
     * @param id The Time Machine ID in the registry
     */
    void setId(int id);

    /**
     * Gets the Time Machine tier
     * @return The Time Machine tier
     */
    int getTier();

    /**
     * Returns the position(s) where must be a TM Core relatively to a compatible Time Machine Controller facing north
     * @return Array of positions where must be a TM Core
     */
    int[][] coreBlocksPos();

    /**
     * Returns the position(s) where must be a TM Basic Block or a TM Upgrade relatively to a compatible Time Machine Controller facing north
     * @return Array of positions where must be a TM Basic Block or a TM Upgrade
     */
    int[][] basicBlocksPos();

    /**
     * Returns the position(s) where must be air relatively to a compatible Time Machine Controller facing north
     * @return Array of positions where must be air
     */
    int[][] airBlocksPos();

    /**
     * Returns the valid IBlockState(s) for TM Controller blocks
     * @return Array of valid IBlockStates for TM Controller blocks
     */
    default IBlockState[] getControllerBlocks() {
        return new IBlockState[]{ModBlocks.timeMachineControlPanel.getDefaultState()};
    }

    /**
     * Returns the valid IBlockState(s) for TM Blocks
     * @return Array of valid IBlockStates for TM Blocks
     */
    default IBlockState[] getCoreBlocks() {
        return new IBlockState[]{ModBlocks.timeMachineCore.getDefaultState()};
    }

    /**
     * Returns the valid IBlockState(s) for TM Basic Blocks
     * @return Array of valid IBlockStates for TM Basic Blocks
     */
    default IBlockState[] getBasicBlocks() {
        return new IBlockState[]{ModBlocks.timeMachineBasicBlock.getDefaultState()};
    }

    /**
     * Returns the valid IBlockState(s) for TM Upgrade Blocks
     * @return Array of valid IBlockStates for TM Upgrade Blocks
     */
    default IBlockState[] getUpgradeBlocks() {
        return new IBlockState[]{};
    }

    /**
     * Returns the position(s) where must be a TM Core
     * @param side Facing of the time machine
     * @return Array of positions where must be a TM Core
     */
    default BlockPos[] getCoreBlocksPos(EnumFacing side) {
        return applySide(coreBlocksPos(), side);
    }

    /**
     * Returns the position(s) where must be a TM Basic Block or a TM Upgrade
     * @param side Facing of the time machine
     * @return Array of positions where must be a TM Basic Block or a TM Upgrade
     */
    default BlockPos[] getBasicBlocksPos(EnumFacing side) {
        return applySide(basicBlocksPos(), side);
    }

    /**
     * Returns the position(s) where must be air
     * @param side Facing of the time machine
     * @return Array of positions where must be air
     */
    default BlockPos[] getAirBlocksPos(EnumFacing side) {
        return applySide(airBlocksPos(), side);
    }

    /**
     * Returns all the valid IBlockStates that can be used to build this Time Machine
     * @return Array of vaild IBlockStates
     */
    default IBlockState[] getBlocks() {
        if (getUpgradeBlocks().length != 0) {
            return (IBlockState[]) ArrayUtils.addAll(ArrayUtils.addAll((IBlockState[]) getControllerBlocks(), (IBlockState[]) getCoreBlocks()), ArrayUtils.addAll((IBlockState[]) getBasicBlocks(), (IBlockState[]) getUpgradeBlocks()));
        } else {
            return (IBlockState[]) ArrayUtils.addAll(ArrayUtils.addAll((IBlockState[]) getControllerBlocks(), (IBlockState[]) getCoreBlocks()), (IBlockState[]) getBasicBlocks());
        }
    }

    /**
     * Converts the relative block positions to meet the actual Time Machine Facing
     * @param input An array of relative positions. The positions are represented in an int array of format {x, y, z}
     * @param side The actual facing of the Time Machine
     * @return An array of BlockPos aligned with the Time Machine facing
     */
    static BlockPos[] applySide(int[][] input, EnumFacing side){
        BlockPos[] output = new BlockPos[input.length];
        for (int i = 0; i < input.length; i++) {
            switch (side.getName()) {
                case "north":
                    output[i] = new BlockPos(input[i][0], input[i][1], input[i][2]);
                    break;
                case "south":
                    output[i] = new BlockPos(input[i][0], input[i][1], input[i][2] * -1);
                    break;
                case "west":
                    output[i] = new BlockPos(input[i][2], input[i][1], input[i][0]);
                    break;
                case "east":
                    output[i] = new BlockPos(input[i][2] * -1, input[i][1], input[i][0]);
                    break;
            }
        }
        return output;
    }

    /**
     * Starts the Time Machine to check if the player can be teleported
     * @param world The player's world
     * @param playerIn The player that triggered the time machine
     * @param controllerPos The position of the TM Controller
     * @param side The facing of the time machine
     */
    default void run(World world, EntityPlayer playerIn, BlockPos controllerPos, EnumFacing side) {
        if (isBuilt(world, controllerPos, side)) {
            TimeTravelMod.proxy.displayTMGuiScreen(playerIn, this, controllerPos, side);
        }
    }

    /**
     * Checks if the Time Machine is correctly built
     * @param world The world were the Time Machine is built
     * @param controllerPos The position of the TM Controller
     * @param side The facing of the Time Machine
     * @return Returns true if the Time Machine is correctly built
     */
    default boolean isBuilt(World world, BlockPos controllerPos, EnumFacing side) {
        BlockPos[] corePos = getCoreBlocksPos(side);
        BlockPos[] basicPos = getBasicBlocksPos(side);
        BlockPos[] airPos = getAirBlocksPos(side);
        IBlockState[] core = getCoreBlocks();
        IBlockState[] base = ArrayUtils.addAll(getBasicBlocks(), getUpgradeBlocks());
        for (int i = 0; i < corePos.length; i++) {
            boolean coincidence = false;
            for (int j = 0; j < core.length; j++) {
                if (world.getBlockState(controllerPos.add(corePos[i])) == core[j]) {coincidence=true; break;}
            }
            if (!coincidence) {return false;}
        }
        for (int i = 0; i < basicPos.length; i++) {
            boolean coincidence = false;
            for (int j = 0; j < base.length; j++) {
                if (world.getBlockState(controllerPos.add(basicPos[i])) == base[j]) {coincidence=true; break;}
            }
            if (!coincidence) {return false;}
        }
        for (int i = 0; i < airPos.length; i++) {
            if (world.getBlockState(controllerPos.add(airPos[i])) != Blocks.AIR.getDefaultState()) {return false;}
        }
        return true;
    }

    /**
     * Does the tasks of the ITimeMachineTeleporter when a time travel starts
     * @param worldIn The source world
     * @param worldOut The target world
     * @param controllerPos The position of the TM Controller
     * @param side The facing of the time machine
     */
    default void teleporterTasks(World worldIn, World worldOut, BlockPos controllerPos, EnumFacing side) {
        BlockPos[] posData = getPosData(controllerPos, side);
        IBlockState[] blockData = getBlockData(worldOut, posData);
        destroyTM(worldOut, posData);
        worldIn.getChunkProvider().getLoadedChunk(worldIn.getChunkFromBlockCoords(controllerPos).x, worldIn.getChunkFromBlockCoords(controllerPos).z);
        buildTM(worldIn, posData, blockData);
    }

    /**
     * Copies the Time Machine blocks positions in the source world
     * @param controllerPos The position of the TM Controller
     * @param side The Time Machine facing
     * @return An array with all the Time Machine blocks positions
     */
    default BlockPos[] getPosData(BlockPos controllerPos, EnumFacing side) {
        BlockPos[] controllerPosA = new BlockPos[]{new BlockPos(0, 0, 0)};
        BlockPos[] corePos = getCoreBlocksPos(side);
        BlockPos[] basePos = getBasicBlocksPos(side);
        BlockPos[] airPos = getAirBlocksPos(side);
        BlockPos[] posData = (BlockPos[]) ArrayUtils.addAll((BlockPos[]) ArrayUtils.addAll(controllerPosA, corePos), (BlockPos[]) ArrayUtils.addAll(basePos, airPos));
        for (int i = 0; i < posData.length; i++) {
            posData[i] = controllerPos.add(posData[i]);
        }
        return posData;
    }

    /**
     * Copies the Time Machine blocks IBlockStates in the source world
     * @param world The world were the Time Machine is now placed
     * @param posData The positions gathered by the getPosData() method
     * @return An array with all the Time Machine blocks IBlockStates
     */
    default IBlockState[] getBlockData(World world, BlockPos[] posData) {
        IBlockState[] blockData = new IBlockState[posData.length];
        for (int i = 0; i < blockData.length; i++) {
            blockData[i] = world.getBlockState(posData[i]);
        }
        return blockData;
    }

    /**
     * Destroys the Time Machine in the source world
     * @param world The source world
     * @param posData The positions gathered by the getPosData() method
     */
    default void destroyTM(World world, BlockPos[] posData) {
        for (int i = 0; i < posData.length; i++) {
            world.setBlockState(posData[i], Blocks.AIR.getDefaultState());
        }
    }

    /**
     * Build the Time Machine in the target world
     * @param world The target world
     * @param posData The positions gathered by the getPosData() method
     * @param blockData The IBlockStates gathered by the getBlockData() method
     */
    default void buildTM(World world, BlockPos[] posData, IBlockState[] blockData) {
        for (int i = 0; i < posData.length; i++) {
            world.setBlockState(posData[i], blockData[i]);
        }
    }
}
