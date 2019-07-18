package tk.rdvdev2.TimeTravelMod.api.timemachine.block.tileentity;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import tk.rdvdev2.TimeTravelMod.api.timemachine.block.TMReadyProperty;

/**
 * This TileEntity is attached to non cooled down Time Machine cores and is used to calculate when they are ready
 */
public class TMCooldownTileEntity extends TileEntity implements ITickableTileEntity {

    public static TileEntityType<TMCooldownTileEntity> type;

    Integer remainingTicks;

    /**
     * Constructor of the TileEntity
     * @param ticks How many ticks needs this Time Machine core to cool down
     */
    public TMCooldownTileEntity(int ticks) {
        super(type);
        this.remainingTicks = ticks;
    }

    /**
     * Default constructor of the TileEntity (Time Machine static HashMap stores time)
     */
    public TMCooldownTileEntity() {
        super(type);
        remainingTicks = null;
    }

    public void setTime(int ticks) {
        this.remainingTicks = ticks;
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putInt("ticks", remainingTicks);
        return compound;
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        this.remainingTicks = compound.getInt("ticks");
    }

    @Override
    public void tick() {
        if (remainingTicks == null || remainingTicks < 0) return;
        this.remainingTicks -= 1;
        if (this.remainingTicks == 0) {
            this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).getBlock().getDefaultState().with(TMReadyProperty.ready, true));
        }
        this.markDirty();
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    @Override
    public void handleUpdateTag(CompoundNBT tag) {
        this.read(tag);
    }
}
