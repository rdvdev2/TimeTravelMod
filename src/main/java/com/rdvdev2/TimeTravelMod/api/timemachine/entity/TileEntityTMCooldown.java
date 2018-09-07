package com.rdvdev2.TimeTravelMod.api.timemachine.entity;

import com.rdvdev2.TimeTravelMod.api.timemachine.block.PropertyTMReady;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

// TODO: JavaDoc
public class TileEntityTMCooldown extends TileEntity implements ITickable {
    int remainingTicks;

    public TileEntityTMCooldown(int ticks) {
        super();
        this.remainingTicks = ticks;
    }

    // Cooldown time defaults to 20 seconds (20 ticks per second)
    public TileEntityTMCooldown() {
        this(20*20);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("ticks", remainingTicks);
        return compound;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        this.remainingTicks = compound.getInteger("ticks");
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        try {
            if (newSate.getValue(PropertyTMReady.ready)) {
                return true;
            } else {
                return false;
            }
        } catch (IllegalArgumentException e) {
            return true;
        }
    }

    @Override
    public void update() {
        this.remainingTicks -= 1;
        if (this.remainingTicks == 0) {
            this.world.setBlockState(this.pos, this.blockType.getDefaultState().withProperty(PropertyTMReady.ready, true));
        }
        this.markDirty();
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        this.readFromNBT(tag);
    }
}
