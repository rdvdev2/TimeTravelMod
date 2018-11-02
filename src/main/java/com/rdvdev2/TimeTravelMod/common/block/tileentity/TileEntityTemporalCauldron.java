package com.rdvdev2.TimeTravelMod.common.block.tileentity;

import com.rdvdev2.TimeTravelMod.ModBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEntityTemporalCauldron extends TileEntity implements ITickable {

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        // Read data
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = super.writeToNBT(compound);

        // Write data

        return compound;
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        if (
                newSate == ModBlocks.temporalCauldron.getStateFromMeta(0) ||
                newSate == ModBlocks.temporalCauldron.getStateFromMeta(1) ||
                newSate == ModBlocks.temporalCauldron.getStateFromMeta(2) ||
                newSate == ModBlocks.temporalCauldron.getStateFromMeta(3))
            return false;
        else
            return true;
    }

    @Override
    public void update() {
        // Do temporal cauldron behaviour
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        readFromNBT(tag);
    }
}
