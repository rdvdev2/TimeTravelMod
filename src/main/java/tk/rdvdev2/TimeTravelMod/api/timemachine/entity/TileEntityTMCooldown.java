package tk.rdvdev2.TimeTravelMod.api.timemachine.entity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ITickable;
import tk.rdvdev2.TimeTravelMod.api.timemachine.block.PropertyTMReady;

/**
 * This TileEntity is attached to non cooled down Time Machine cores and is used to calculate when they are ready
 */
public class TileEntityTMCooldown extends TileEntity implements ITickable {

    public static TileEntityType<TileEntityTMCooldown> type = TileEntityType.register("timetravelmod:entity.tmcooldown", TileEntityType.Builder.create(TileEntityTMCooldown::new));

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
    public NBTTagCompound write(NBTTagCompound compound) {
        super.write(compound);
        compound.setInt("ticks", remainingTicks);
        return compound;
    }

    @Override
    public void read(NBTTagCompound compound) {
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
    public NBTTagCompound getUpdateTag() {
        return this.write(new NBTTagCompound());
    }

    @Override
    public void handleUpdateTag(NBTTagCompound tag) {
        this.read(tag);
    }
}
