package tk.rdvdev2.TimeTravelMod.common.block.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import tk.rdvdev2.TimeTravelMod.ModBlocks;
import tk.rdvdev2.TimeTravelMod.ModItems;
import tk.rdvdev2.TimeTravelMod.TimeTravelMod;

import javax.annotation.Nullable;

public class TileEntityTemporalCauldron extends TileEntity implements ITickable {

    private final static int CRYSTAL_SLOT = 0;
    private final static int ITEM_SLOT = 1;

    @CapabilityInject(IItemHandler.class)
    static Capability<IItemHandler> ITEM_HANDLER_CAPABILITY = null;

    IItemHandler inventory;

    public TileEntityTemporalCauldron() {
        inventory = new ItemStackHandler(2) {
            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                TimeTravelMod.logger.info("Cauldron contents: " + inventory.getStackInSlot(CRYSTAL_SLOT) + ", " + inventory.getStackInSlot(ITEM_SLOT));
                markDirty();
            }
        };
    }

    public boolean containsItem() {
        return !inventory.getStackInSlot(ITEM_SLOT).isEmpty();
    }

    public void putItem(ItemStack item) {
        if (item.isItemStackDamageable()); inventory.insertItem(ITEM_SLOT, new ItemStack(item.getItem(), 1), false);
    }

    public ItemStack removeItem() {
        return inventory.extractItem(ITEM_SLOT, 1, false);
    }

    public boolean containsCrystal() {
        return !inventory.getStackInSlot(CRYSTAL_SLOT).isEmpty();
    }

    public void putCrystal(ItemStack item) {
        if (item.getItem() == ModItems.timeCrystal); inventory.insertItem(CRYSTAL_SLOT, new ItemStack(item.getItem(), 1), false);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        ITEM_HANDLER_CAPABILITY.readNBT(inventory, null, compound.getTag("inventory"));

        // Read data
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = super.writeToNBT(compound);
        compound.setTag("inventory", ITEM_HANDLER_CAPABILITY.writeNBT(inventory, null));

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

    // TODO: Custom network sync

    @Override
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
    }
}
