package com.rdvdev2.TimeTravelMod.common.block.tileentity;

import com.rdvdev2.TimeTravelMod.ModBlocks;
import com.rdvdev2.TimeTravelMod.ModItems;
import com.rdvdev2.TimeTravelMod.common.block.TemporalCauldronBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.Random;

public class TemporalCauldronTileEntity extends TileEntity implements ITickableTileEntity {

    @CapabilityInject(IItemHandler.class)
    static Capability<IItemHandler> ITEM_HANDLER_CAPABILITY = null;

    private IItemHandler inventory = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            markDirty();
        }
    };

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
        if (cap == ITEM_HANDLER_CAPABILITY) {
            return LazyOptional.of(()-> (T)inventory);
        }
        return super.getCapability(cap, side);
    }

    private final static int CRYSTAL_SLOT = 0;
    private final static int ITEM_SLOT = 1;

    private int crystal_usages = 0;
    private int tick_count = 0;

    public TemporalCauldronTileEntity() {
        super(ModBlocks.TileEntities.TEMPORAL_CAULDRON.get());
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
        if (item.getItem() == ModItems.TIME_CRYSTAL.get()) {
            inventory.insertItem(CRYSTAL_SLOT, item, false);
            crystal_usages = 2000;
            this.world.setBlockState(this.pos, this.world.getBlockState(pos).with(TemporalCauldronBlock.LEVEL, 3));
        }
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        ITEM_HANDLER_CAPABILITY.getStorage().readNBT(ITEM_HANDLER_CAPABILITY, inventory, null, compound.get("Items"));

        crystal_usages = compound.getInt("crystal_usages");
        tick_count = compound.getInt("tick_count");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound = super.write(compound);
        compound.put("Items", ITEM_HANDLER_CAPABILITY.getStorage().writeNBT(ITEM_HANDLER_CAPABILITY, inventory, null));

        compound.putInt("crystal_usages", crystal_usages);
        compound.putInt("tick_count", tick_count);

        return compound;
    }

    @Override
    public void tick() {
        if (world.isRemote) return;
        if (!inventory.getStackInSlot(ITEM_SLOT).isEmpty() && !inventory.getStackInSlot(CRYSTAL_SLOT).isEmpty()) {
            if (crystal_usages == 1300) this.world.setBlockState(this.pos, this.world.getBlockState(pos).with(TemporalCauldronBlock.LEVEL, 2));
            if (crystal_usages == 600) this.world.setBlockState(this.pos, this.world.getBlockState(pos).with(TemporalCauldronBlock.LEVEL, 1));
            if (crystal_usages == 0) {
                this.world.setBlockState(this.pos, this.world.getBlockState(pos).with(TemporalCauldronBlock.LEVEL, 0));
                inventory.extractItem(CRYSTAL_SLOT, 1 ,false);
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
                else if (n < 95) damage--;

                tool.setDamage(damage);
                inventory.insertItem(ITEM_SLOT, tool, false);
            }

            markDirty();
        }
    }

    @Override
    public void remove() {
        super.remove();
        if (inventory != null)
            inventory = null;
    }
}
