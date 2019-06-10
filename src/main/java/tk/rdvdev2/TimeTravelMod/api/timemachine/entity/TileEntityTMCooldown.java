package tk.rdvdev2.TimeTravelMod.api.timemachine.entity;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import tk.rdvdev2.TimeTravelMod.api.timemachine.block.PropertyTMReady;

/**
 * This TileEntity is attached to non cooled down Time Machine cores and is used to calculate when they are ready
 */
public class TileEntityTMCooldown extends TileEntity implements ITickableTileEntity {

    public static TileEntityType<TileEntityTMCooldown> type;

    int remainingTicks;

    /**
     * Constructor of the TileEntity
     * @param ticks How many ticks needs this Time Machine core to cool down
     */
    public TileEntityTMCooldown(int ticks) {
        super(type);
        this.remainingTicks = ticks;
    }

    /**
     * Default constructor of the TileEntity (20 seconds cool down time)
     */
    public TileEntityTMCooldown() {
        this(20*20);
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

    /*@Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        try {
            if (newSate.get(PropertyTMReady.ready)) {
                return true;
            } else {
                return false;
            }
        } catch (IllegalArgumentException e) {
            return true;
        }
    }*/

    @Override
    public void tick() {
        this.remainingTicks -= 1;
        if (this.remainingTicks == 0) {
            this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).getBlock().getDefaultState().with(PropertyTMReady.ready, true));
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
