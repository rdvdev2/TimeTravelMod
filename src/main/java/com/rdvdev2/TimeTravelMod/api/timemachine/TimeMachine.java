package com.rdvdev2.TimeTravelMod.api.timemachine;

import com.rdvdev2.TimeTravelMod.ModBlocks;
import com.rdvdev2.TimeTravelMod.TimeTravelMod;
import com.rdvdev2.TimeTravelMod.api.timemachine.block.BlockTimeMachineComponent;
import com.rdvdev2.TimeTravelMod.api.timemachine.block.PropertyTMReady;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.commons.lang3.ArrayUtils;

import java.util.List;

/**
 * Defines the behaviour and the aspect of a Time Machine
 */
public abstract class TimeMachine extends IForgeRegistryEntry.Impl<TimeMachine> {

    /**
     * Gets the Time Machine tier
     * @return The Time Machine tier
     */
    abstract public int getTier();

    /**
     * Returns the position(s) where must be a TM Core relatively to a compatible Time Machine Controller facing north
     * @return Array of positions where must be a TM Core
     */
    abstract public int[][] coreBlocksPos();

    /**
     * Returns the position(s) where must be a TM Basic Block or a TM Upgrade relatively to a compatible Time Machine Controller facing north
     * @return Array of positions where must be a TM Basic Block or a TM Upgrade
     */
    abstract public int[][] basicBlocksPos();

    /**
     * Returns the position(s) where must be air relatively to a compatible Time Machine Controller facing north
     * @return Array of positions where must be air
     */
    abstract public int[][] airBlocksPos();

    /**
     * Returns the valid IBlockState(s) for TM Controller blocks
     * @return Array of valid IBlockStates for TM Controller blocks
     */
    public IBlockState[] getControllerBlocks() {
        return new IBlockState[]{ModBlocks.timeMachineControlPanel.getDefaultState()};
    }

    /**
     * Returns the valid IBlockState(s) for TM Blocks
     * @return Array of valid IBlockStates for TM Blocks
     */
    public IBlockState[] getCoreBlocks() {
        return new IBlockState[]{ModBlocks.timeMachineCore.getDefaultState()};
    }

    /**
     * Returns the valid IBlockState(s) for TM Basic Blocks
     * @return Array of valid IBlockStates for TM Basic Blocks
     */
    public IBlockState[] getBasicBlocks() {
        return new IBlockState[]{ModBlocks.timeMachineBasicBlock.getDefaultState()};
    }

    /**
     * Returns the valid IBlockState(s) for TM Upgrade Blocks
     * @return Array of valid IBlockStates for TM Upgrade Blocks
     */
    public IBlockState[] getUpgradeBlocks() {
        return new IBlockState[]{};
    }

    /**
     * Returns the position(s) where must be a TM Core
     * @param side Facing of the time machine
     * @return Array of positions where must be a TM Core
     */
    public BlockPos[] getCoreBlocksPos(EnumFacing side) {
        return applySide(coreBlocksPos(), side);
    }

    /**
     * Returns the position(s) where must be a TM Basic Block or a TM Upgrade
     * @param side Facing of the time machine
     * @return Array of positions where must be a TM Basic Block or a TM Upgrade
     */
    public BlockPos[] getBasicBlocksPos(EnumFacing side) {
        return applySide(basicBlocksPos(), side);
    }

    /**
     * Returns the position(s) where must be air
     * @param side Facing of the time machine
     * @return Array of positions where must be air
     */
    public BlockPos[] getAirBlocksPos(EnumFacing side) {
        return applySide(airBlocksPos(), side);
    }

    public int getEntityMaxLoad() {
        return 1;
    }
    /**
     * Returns all the valid IBlockStates that can be used to build this Time Machine
     * @return Array of vaild IBlockStates
     */
    public IBlockState[] getBlocks() {
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
    public final static BlockPos[] applySide(int[][] input, EnumFacing side){
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
    public void run(World world, EntityPlayer playerIn, BlockPos controllerPos, EnumFacing side) {
        if (isBuilt(world, controllerPos, side) &&
            isPlayerInside(world, controllerPos, side, playerIn) &&
            !isOverloaded(world, controllerPos, side) &&
            !world.isRemote) {
            if (!triggerTemporalExplosion(world, controllerPos, side))
                TimeTravelMod.proxy.displayTMGuiScreen(playerIn, this, controllerPos, side);
        }
    }

    public boolean triggerTemporalExplosion(World world, BlockPos controllerPos, EnumFacing side) {
        for (BlockPos pos:getCoreBlocksPos(side)) {
            BlockTimeMachineComponent core = (BlockTimeMachineComponent)world.getBlockState(controllerPos.add(pos)).getBlock();
            if (core.randomExplosion(world, controllerPos.add(pos)))
                return true;
        }
        return false;
    }

    /**
     * Checks if the Time Machine is correctly built
     * @param world The world were the Time Machine is built
     * @param controllerPos The position of the TM Controller
     * @param side The facing of the Time Machine
     * @return Returns true if the Time Machine is correctly built
     */
    public boolean isBuilt(World world, BlockPos controllerPos, EnumFacing side) {
        BlockPos[] corePos = getCoreBlocksPos(side);
        BlockPos[] basicPos = getBasicBlocksPos(side);
        BlockPos[] airPos = getAirBlocksPos(side);
        IBlockState[] core = getCoreBlocks();
        IBlockState[] base = ArrayUtils.addAll(getBasicBlocks(), getUpgradeBlocks());
        for (int i = 0; i < corePos.length; i++) {
            boolean coincidence = false;
            for (int j = 0; j < core.length; j++) {
                if (world.getBlockState(controllerPos.add(corePos[i])) == core[j].withProperty(PropertyTMReady.ready, true)) {coincidence=true; break;}
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
     * Detects if the Time Machine has too many Entities inside
     * @param world The world where the Time Machine is
     * @param controllerPos The position of the Time Machine Controller
     * @param side The facing of the Time Machine
     * @return True if the Time Machine is overloaded
     */
    public boolean isOverloaded(World world, BlockPos controllerPos, EnumFacing side) {
        return getEntitiesInside(world, controllerPos, side).size() > getEntityMaxLoad();
    }

    /**
     * Detects if a given player is inside the Time Machine
     * @param world The world where the Time Machine is
     * @param controllerPos The position of the Time Machine Controller
     * @param side The facing of the Time Machine
     * @param player The wanted player
     * @return True if the player is inside the Time Machine
     */
    public boolean isPlayerInside(World world, BlockPos controllerPos, EnumFacing side, EntityPlayer player) {
        System.out.println(getEntitiesInside(world, controllerPos, side));
        for (Entity entity:getEntitiesInside(world, controllerPos, side)){
            System.out.println(entity);
            if (entity.getPersistentID().equals(player.getPersistentID())) {
                System.out.println("The player is inside");
                return true;
            }
        }
        System.out.println("The player isn't inside");
        return false;
    }

    /**
     * Gets all the Entities inside the Time Machine
     * @param world The world where the Time Machine is
     * @param controllerPos The position of the Time Machine Controller
     * @param side The facing of the Time Machine
     * @return A list with all the Entities inside the Time Machine
     */
    public final List<Entity> getEntitiesInside(World world, BlockPos controllerPos, EnumFacing side) {
        AxisAlignedBB airSpace = getAirSpace(controllerPos, side);
        System.out.println(airSpace);
        return world.getEntitiesWithinAABB(Entity.class, airSpace);
    }

    /**
     * Calculates the zone where Entities must be detected
     * @param controllerPos The position of the Time Machine Controller
     * @param side The facing of the Time Machine
     * @return An AxisAlignedBB of the Time Machine Air Blocks
     */
    public AxisAlignedBB getAirSpace(BlockPos controllerPos, EnumFacing side) {
        // Get the air blocks
        BlockPos relativeAirBlocks[] = applySide(airBlocksPos(), side);
        // First block is the min and max block by default
        BlockPos minPos = relativeAirBlocks[0];
        BlockPos maxPos = relativeAirBlocks[0];
        // Check for the correct min and max block
        for (int block = 1; block < relativeAirBlocks.length; block++) {
            if (relativeAirBlocks[block].getX() < minPos.getX() ||
                relativeAirBlocks[block].getY() < minPos.getY() ||
                relativeAirBlocks[block].getZ() < minPos.getZ()) {
                minPos = relativeAirBlocks[block];
            } else
            if (relativeAirBlocks[block].getX() > maxPos.getX() ||
                relativeAirBlocks[block].getY() > maxPos.getY() ||
                relativeAirBlocks[block].getZ() > maxPos.getZ()) {
                maxPos = relativeAirBlocks[block];
            }
        }
        // Convert the relative positions to real ones
        minPos = minPos.add(controllerPos);
        maxPos = maxPos.add(controllerPos);
        // Return the Air Space
        float offset = 0.3f;
        return new AxisAlignedBB(
                minPos.getX() + offset,
                minPos.getY() + offset,
                minPos.getZ() + offset,
                maxPos.getX() + 1-offset,
                maxPos.getY() + 1-offset,
                maxPos.getZ() + 1-offset
        );
    }

    /**
     * Does the tasks of the ITimeMachineTeleporter when a time travel starts
     * @param worldIn The source world
     * @param worldOut The target world
     * @param controllerPos The position of the TM Controller
     * @param side The facing of the time machine
     */
    public void teleporterTasks(World worldIn, World worldOut, BlockPos controllerPos, EnumFacing side) {
        BlockPos[] posData = getPosData(controllerPos, side);
        IBlockState[] blockData = getBlockData(worldOut, posData);
        destroyTM(worldOut, posData);
        worldIn.getChunkProvider().getLoadedChunk(worldIn.getChunkFromBlockCoords(controllerPos).x, worldIn.getChunkFromBlockCoords(controllerPos).z);
        buildTM(worldIn, posData, blockData);
        doCooldown(worldIn, controllerPos, side);
    }

    /**
     * Copies the Time Machine blocks positions in the source world
     * @param controllerPos The position of the TM Controller
     * @param side The Time Machine facing
     * @return An array with all the Time Machine blocks positions
     */
    public final BlockPos[] getPosData(BlockPos controllerPos, EnumFacing side) {
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
    public final IBlockState[] getBlockData(World world, BlockPos[] posData) {
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
    public final void destroyTM(World world, BlockPos[] posData) {
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
    public final void buildTM(World world, BlockPos[] posData, IBlockState[] blockData) {
        for (int i = 0; i < posData.length; i++) {
            world.setBlockState(posData[i], blockData[i]);
        }
    }

    // TODO: JadaDoc
    public final void doCooldown(World worldIn, BlockPos controllerPos, EnumFacing side) {
        for (BlockPos block:getCoreBlocksPos(side)) {
            worldIn.setBlockState(controllerPos.add(block), worldIn.getBlockState(controllerPos.add(block)).withProperty(PropertyTMReady.ready, false));
        }
    }
}
