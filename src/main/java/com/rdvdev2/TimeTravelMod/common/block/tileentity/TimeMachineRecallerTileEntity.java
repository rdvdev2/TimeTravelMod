package com.rdvdev2.TimeTravelMod.common.block.tileentity;

import com.rdvdev2.TimeTravelMod.ModBlocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;

public class TimeMachineRecallerTileEntity extends TileEntity {

    private BlockPos controllerPos;
    private Direction side;
    private DimensionType dest;

    public TimeMachineRecallerTileEntity() {
        super(ModBlocks.TileEntities.TM_RECALLER.get());
    }

    public BlockPos getControllerPos() {
        return controllerPos;
    }

    public void setControllerPos(BlockPos controllerPos) {
        this.controllerPos = controllerPos;
        markDirty();
    }

    public Direction getSide() {
        return side;
    }

    public void setSide(Direction side) {
        this.side = side;
        markDirty();
    }

    public DimensionType getDest() {
        return dest;
    }

    public void setDest(DimensionType dest) {
        this.dest = dest;
        markDirty();
    }

    @Override
    public void read(CompoundNBT nbt) {
        super.read(nbt);
        this.controllerPos = BlockPos.fromLong(nbt.getLong("controllerpos"));
        this.side = Direction.byIndex(nbt.getInt("side"));
        this.dest = DimensionType.byName(ResourceLocation.tryCreate(nbt.getString("dest")));
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        nbt = super.write(nbt);
        nbt.putLong("controllerpos", this.controllerPos.toLong());
        nbt.putInt("side", this.side.getIndex());
        nbt.putString("dest", this.dest.getRegistryName().toString());
        return nbt;
    }
}
