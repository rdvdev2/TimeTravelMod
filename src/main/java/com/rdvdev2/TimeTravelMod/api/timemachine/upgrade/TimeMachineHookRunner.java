package com.rdvdev2.TimeTravelMod.api.timemachine.upgrade;

import com.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TimeMachineHookRunner extends TimeMachine {

    TimeMachine tm;
    TimeMachineUpgrade[] upgrades;

    public TimeMachineHookRunner(TimeMachine tm, TimeMachineUpgrade[] upgrades) {
        this.tm = tm;
        this.upgrades = upgrades;
    }

    public TimeMachine removeHooks() {
        return this.tm;
    }

    @Override
    public int getTier() {
        return tm.getTier();
    }

    @Override
    public int[][] coreBlocksPos() {
        return tm.coreBlocksPos();
    }

    @Override
    public int[][] basicBlocksPos() {
        return tm.basicBlocksPos();
    }

    @Override
    public int[][] airBlocksPos() {
        return tm.airBlocksPos();
    }

    @Override
    public IBlockState[] getControllerBlocks() {
        return tm.getControllerBlocks();
    }

    @Override
    public IBlockState[] getCoreBlocks() {
        return tm.getCoreBlocks();
    }

    @Override
    public IBlockState[] getBasicBlocks() {
        return tm.getBasicBlocks();
    }

    @Override
    public IBlockState[] getUpgradeBlocks() {
        return tm.getUpgradeBlocks();
    }

    @Override
    public BlockPos[] getCoreBlocksPos(EnumFacing side) {
        return tm.getCoreBlocksPos(side);
    }

    @Override
    public BlockPos[] getBasicBlocksPos(EnumFacing side) {
        return tm.getBasicBlocksPos(side);
    }

    @Override
    public BlockPos[] getAirBlocksPos(EnumFacing side) {
        return tm.getAirBlocksPos(side);
    }

    @Override
    public int getEntityMaxLoad() {
        return tm.getEntityMaxLoad();
    }

    @Override
    public IBlockState[] getBlocks() {
        return tm.getBlocks();
    }

    @Override
    public void run(World world, EntityPlayer playerIn, BlockPos controllerPos, EnumFacing side) {
        for(TimeMachineUpgrade upgrade:upgrades) {
            if (upgrade.runVoidHook(TimeMachineHook.RunHook.class, world, playerIn, controllerPos, side))
                return;
        }
        tm.run(world, playerIn, controllerPos, side);
    }

    @Override
    public boolean triggerTemporalExplosion(World world, BlockPos controllerPos, EnumFacing side) {
        return tm.triggerTemporalExplosion(world, controllerPos, side);
    }

    @Override
    public boolean isBuilt(World world, BlockPos controllerPos, EnumFacing side) {
        return tm.isBuilt(world, controllerPos, side);
    }

    @Override
    public boolean isOverloaded(World world, BlockPos controllerPos, EnumFacing side) {
        return tm.isOverloaded(world, controllerPos, side);
    }

    @Override
    public boolean isPlayerInside(World world, BlockPos controllerPos, EnumFacing side, EntityPlayer player) {
        return tm.isPlayerInside(world, controllerPos, side, player);
    }

    @Override
    public AxisAlignedBB getAirSpace(BlockPos controllerPos, EnumFacing side) {
        return tm.getAirSpace(controllerPos, side);
    }

    @Override
    public void teleporterTasks(World worldIn, World worldOut, BlockPos controllerPos, EnumFacing side) {
        tm.teleporterTasks(worldIn, worldOut, controllerPos, side);
    }

    @Override
    public boolean isCooledDown(World world, BlockPos controllerPos, EnumFacing side) {
        return tm.isCooledDown(world, controllerPos, side);
    }
}
