package tk.rdvdev2.TimeTravelMod.common.block.tileentity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.InvWrapper;
import tk.rdvdev2.TimeTravelMod.ModItems;
import tk.rdvdev2.TimeTravelMod.common.block.BlockTemporalCauldron;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

import static net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;

public class TileEntityTemporalCauldron extends TileEntity implements ITickableTileEntity, IInventory {
    private NonNullList<ItemStack> cauldronContents = NonNullList.withSize(2, ItemStack.EMPTY);

    public static TileEntityType<TileEntityTemporalCauldron> type;

    private final static int CRYSTAL_SLOT = 0;
    private final static int ITEM_SLOT = 1;

    private int crystal_usages = 0;
    private int tick_count = 0;

    private LazyOptional<IItemHandlerModifiable> cauldronHandler;

    public TileEntityTemporalCauldron() {
        super(type);
    }

    public boolean containsItem() {
        return !getStackInSlot(ITEM_SLOT).isEmpty();
    }

    public void putItem(ItemStack item) {
        if (item.isDamageable()); setInventorySlotContents(ITEM_SLOT, item);
    }

    public ItemStack removeItem() {
        return removeStackFromSlot(ITEM_SLOT);
    }

    public boolean containsCrystal() {
        return !getStackInSlot(CRYSTAL_SLOT).isEmpty();
    }

    public void putCrystal(ItemStack item) {
        if (item.getItem() == ModItems.timeCrystal) {
            setInventorySlotContents(CRYSTAL_SLOT, item);
            crystal_usages = 2000;
            this.world.setBlockState(this.pos, this.world.getBlockState(pos).with(BlockTemporalCauldron.LEVEL, 3));
        }
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        this.cauldronContents = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, this.cauldronContents);

        crystal_usages = compound.getInt("crystal_usages");
        tick_count = compound.getInt("tick_count");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound = super.write(compound);
        ItemStackHelper.saveAllItems(compound, this.cauldronContents);

        compound.putInt("crystal_usages", crystal_usages);
        compound.putInt("tick_count", tick_count);

        return compound;
    }

    @Override
    public void tick() {
        if (!world.isRemote && !getStackInSlot(ITEM_SLOT).isEmpty() && !getStackInSlot(CRYSTAL_SLOT).isEmpty()) {
            if (crystal_usages == 1300) this.world.setBlockState(this.pos, this.world.getBlockState(pos).with(BlockTemporalCauldron.LEVEL, 2));
            if (crystal_usages == 600) this.world.setBlockState(this.pos, this.world.getBlockState(pos).with(BlockTemporalCauldron.LEVEL, 1));
            if (crystal_usages == 0) {
                this.world.setBlockState(this.pos, this.world.getBlockState(pos).with(BlockTemporalCauldron.LEVEL, 0));
                removeStackFromSlot(CRYSTAL_SLOT);
            }

            tick_count++;
            if (tick_count == 10) {
                tick_count = 0;
                crystal_usages--;

                ItemStack tool = decrStackSize(ITEM_SLOT, 1);
                int damage = tool.getDamage();
                Random r = new Random();

                int n = r.nextInt(100);
                if (n >= 98) damage++;
                else if (n >= 95) damage = damage=damage;
                else damage--;

                tool.setDamage(damage);
                setInventorySlotContents(ITEM_SLOT, tool);
            }

            markDirty();
        }
    }

    @Override
    public void updateContainingBlockInfo() {
        super.updateContainingBlockInfo();
        if (this.cauldronHandler != null) {
            this.cauldronHandler.invalidate();
            this.cauldronHandler = null;
        }
    }

    /**
     * Returns the number of slots in the inventory.
     */
    @Override
    public int getSizeInventory() {
        return cauldronContents.size();
    }

    @Override
    public boolean isEmpty() {
        return cauldronContents.isEmpty();
    }

    /**
     * Returns the stack in the given slot.
     *
     * @param index
     */
    @Override
    public ItemStack getStackInSlot(int index) {
        return cauldronContents.get(index);
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     *
     * @param index
     * @param count
     */
    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack stack = ItemStackHelper.getAndSplit(cauldronContents, index, count);
        this.markDirty();
        return stack;
    }

    /**
     * Removes a stack from the given slot and returns it.
     *
     * @param index
     */
    @Override
    public ItemStack removeStackFromSlot(int index) {
        ItemStack stack = ItemStackHelper.getAndRemove(cauldronContents, index);
        this.markDirty();
        return stack;
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     *
     * @param index
     * @param stack
     */
    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        cauldronContents.set(index, stack);
        this.markDirty();
    }

    /**
     * Returns the maximum stack size for a inventory slot. Seems to always be 64, possibly will be extended.
     */
    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    /**
     * Don't rename this method to canInteractWith due to conflicts with Container
     *
     * @param player
     */
    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        return true;
    }

    @Override
    public void openInventory(PlayerEntity player) {

    }

    @Override
    public void closeInventory(PlayerEntity player) {

    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot. For
     * guis use Slot.isItemValid
     *
     * @param index
     * @param stack
     */
    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return false;
    }

    /*@Override TODO: Check these are not used anymore
    public int getField(int id) {
        return 0;
    }


    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }*/

    @Override
    public void clear() {
        cauldronContents.clear();
        this.markDirty();
    }

    /*@Override
    public ITextComponent getName() {
        return new TranslationTextComponent("block.timetravelmod.temporalcauldron");
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Nullable
    @Override
    public ITextComponent getCustomName() {
        return null;
    }*/

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (!this.removed && cap == ITEM_HANDLER_CAPABILITY) {
            if (this.cauldronHandler == null) {
                this.cauldronHandler = LazyOptional.of(this::createHandler);
            }
            return this.cauldronHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    private IItemHandlerModifiable createHandler() {
        return new InvWrapper(this);
    }

    @Override
    public void remove() {
        super.remove();
        if (cauldronHandler != null)
            cauldronHandler.invalidate();
    }
}
