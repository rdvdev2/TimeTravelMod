package com.rdvdev2.TimeTravelMod.util;

import com.rdvdev2.TimeTravelMod.ModBlocks;
import com.rdvdev2.TimeTravelMod.client.gui.GuiTimeMachine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.commons.lang3.ArrayUtils;

public interface TimeMachine {

    // Position(s) where must be a TM Core
    int[][] coreBlocksPos();

    // Positions where must be a TM Basic Block
    int[][] basicBlocksPos();

    // Positions where must be air
    int[][] airBlocksPos();

    // Valid IBlockState(s) for TM Control Panel
    default IBlockState[] controllerBlocks() {
        return new IBlockState[]{ModBlocks.timeMachineControlPanel.getDefaultState()};
    }

    // Valid IBlockState(s) for TM Core
    default IBlockState[] coreBlocks() {
        return new IBlockState[]{ModBlocks.timeMachineCore.getDefaultState()};
    }

    // Valid IBlockState(s) for TM Basic Block
    default IBlockState[] basicBlocks() {
        return new IBlockState[]{ModBlocks.timeMachineBasicBlock.getDefaultState()};
    }

    // Valid IBlockState(s) for TM Upgrades
    default IBlockState[] upgradeBlocks() {
        return new IBlockState[]{};
    }

    default BlockPos[] getCoreBlocksPos(EnumFacing side) {
        return applySide(coreBlocksPos(), side);
    }

    default BlockPos[] getBasicBlocksPos(EnumFacing side) {
        return applySide(basicBlocksPos(), side);
    }

    default BlockPos[] getAirBlocksPos(EnumFacing side) {
        return applySide(airBlocksPos(), side);
    }

    default IBlockState[] getControllerBlocks() {
        return controllerBlocks();
    }

    default IBlockState[] getCoreBlocks() {
        return coreBlocks();
    }

    default IBlockState[] getBasicBlocks() {
        return basicBlocks();
    }

    default IBlockState[] getUpgradeBlocks() {
        return upgradeBlocks();
    }

    default IBlockState[] getBlocks() {
        if (upgradeBlocks().length != 0) {
            return (IBlockState[]) ArrayUtils.addAll(ArrayUtils.addAll((IBlockState[]) getControllerBlocks(), (IBlockState[]) getCoreBlocks()), ArrayUtils.addAll((IBlockState[]) getBasicBlocks(), (IBlockState[]) getUpgradeBlocks()));
        } else {
            return (IBlockState[]) ArrayUtils.addAll(ArrayUtils.addAll((IBlockState[]) getControllerBlocks(), (IBlockState[]) getCoreBlocks()), (IBlockState[]) getBasicBlocks());
        }
    }

    // Modifies the relative BlockPos arrays to meet the facing of the TM
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

    default void run(World world, EntityPlayer playerIn, BlockPos controllerPos, EnumFacing side) {
        if (isBuilt(world, controllerPos, side)) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiTimeMachine(playerIn, this, controllerPos, side));
        }
    }
    // Checks if the TM is correctly built
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

    // ITeleporterTimeMachine related methods
    default void teleporterTasks(World worldIn, World worldOut, BlockPos controllerPos, EnumFacing side) {
        BlockPos[] posData = getPosData(controllerPos, side);
        IBlockState[] blockData = getBlockData(worldOut, posData);
        destroyTM(worldOut, posData);
        buildTM(worldIn, posData, blockData);
    }

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

    default IBlockState[] getBlockData(World world, BlockPos[] posData) {
        IBlockState[] blockData = new IBlockState[posData.length];
        for (int i = 0; i < blockData.length; i++) {
            blockData[i] = world.getBlockState(posData[i]);
        }
        return blockData;
    }

    default void destroyTM(World world, BlockPos[] posData) {
        for (int i = 0; i < posData.length; i++) {
            world.setBlockState(posData[i], Blocks.AIR.getDefaultState());
        }
    }

    default void buildTM(World world, BlockPos[] posData, IBlockState[] blockData) {
        for (int i = 0; i < posData.length; i++) {
            world.setBlockState(posData[i], blockData[i]);
        }
    }
}
