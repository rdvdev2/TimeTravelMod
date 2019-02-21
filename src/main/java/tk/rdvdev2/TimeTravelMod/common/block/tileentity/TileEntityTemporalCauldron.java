package tk.rdvdev2.TimeTravelMod.common.block.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import tk.rdvdev2.TimeTravelMod.ModItems;
import tk.rdvdev2.TimeTravelMod.common.block.BlockTemporalCauldron;

import java.util.Random;

public class TileEntityTemporalCauldron extends TileEntity implements ITickable {

    public static TileEntityType<TileEntityTemporalCauldron> type;

    private final static int CRYSTAL_SLOT = 0;
    private final static int ITEM_SLOT = 1;

    private int crystal_usages = 0;
    private int tick_count = 0;

    @CapabilityInject(IItemHandler.class)
    static Capability<IItemHandler> ITEM_HANDLER_CAPABILITY = null;

    IItemHandler inventory;

    public TileEntityTemporalCauldron() {
        super(type);
        inventory = new ItemStackHandler(2) {
            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                markDirty();
            }
        };
    }

    public boolean containsItem() {
        return !inventory.getStackInSlot(ITEM_SLOT).isEmpty();
    }

    public void putItem(ItemStack item) {
        if (item.isDamageable()); inventory.insertItem(ITEM_SLOT, item, false);
    }

    public ItemStack removeItem() {
        return inventory.extractItem(ITEM_SLOT, 1, false);
    }

    public boolean containsCrystal() {
        return !inventory.getStackInSlot(CRYSTAL_SLOT).isEmpty();
    }

    public void putCrystal(ItemStack item) {
        if (item.getItem() == ModItems.timeCrystal) {
            inventory.insertItem(CRYSTAL_SLOT, item, false);
            crystal_usages = 2000;
            this.world.setBlockState(this.pos, this.world.getBlockState(pos).with(BlockTemporalCauldron.LEVEL, 3));
        }
    }

    @Override
    public void read(NBTTagCompound compound) {
        super.read(compound);
        ITEM_HANDLER_CAPABILITY.readNBT(inventory, null, compound.getTag("inventory"));

        crystal_usages = compound.getInt("crystal_usages");
        tick_count = compound.getInt("tick_count");
    }

    @Override
    public NBTTagCompound write(NBTTagCompound compound) {
        compound = super.write(compound);
        compound.setTag("inventory", ITEM_HANDLER_CAPABILITY.writeNBT(inventory, null));

        compound.setInt("crystal_usages", crystal_usages);
        compound.setInt("tick_count", tick_count);

        return compound;
    }

    /*@Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        if (
                newSate == ModBlocks.temporalCauldron.getDefaultState().with(BlockTemporalCauldron.LEVEL, 0) ||
                newSate == ModBlocks.temporalCauldron.getDefaultState().with(BlockTemporalCauldron.LEVEL, 1) ||
                newSate == ModBlocks.temporalCauldron.getDefaultState().with(BlockTemporalCauldron.LEVEL, 2) ||
                newSate == ModBlocks.temporalCauldron.getDefaultState().with(BlockTemporalCauldron.LEVEL, 3))
            return false;
        else
            return true;
    }*/

    @Override
    public void tick() {
        if (!world.isRemote && !inventory.getStackInSlot(ITEM_SLOT).isEmpty() && !inventory.getStackInSlot(CRYSTAL_SLOT).isEmpty()) {
            if (crystal_usages == 1300) this.world.setBlockState(this.pos, this.world.getBlockState(pos).with(BlockTemporalCauldron.LEVEL, 2));
            if (crystal_usages == 600) this.world.setBlockState(this.pos, this.world.getBlockState(pos).with(BlockTemporalCauldron.LEVEL, 1));
            if (crystal_usages == 0) {
                this.world.setBlockState(this.pos, this.world.getBlockState(pos).with(BlockTemporalCauldron.LEVEL, 0));
                inventory.extractItem(CRYSTAL_SLOT, 1, false);
            }

            tick_count++;
            if (tick_count == 10) {
                tick_count = 0;
                crystal_usages--;

                ItemStack tool = inventory.extractItem(ITEM_SLOT, 1, false);
                int damage = tool.getDamage();
                Random r = new Random();

                int n = r.nextInt(100);
                if (n >= 98) damage++;
                else if (n >= 95) damage = damage=damage;
                else damage--;

                tool.setDamage(damage);
                inventory.insertItem(ITEM_SLOT, tool, false);
            }

            markDirty();
        }
    }


    // TODO: Investigate new capabilty system
    /*@Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        if (capability == ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == ITEM_HANDLER_CAPABILITY) {
            return (T) inventory;
        }
        return super.getCapability(capability, facing);
    } */
}
