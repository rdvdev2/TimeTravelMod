package tk.rdvdev2.TimeTravelMod.common.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.tileentity.BarrelTileEntity;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.structure.Structures;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class IllagerBanditEntity extends MonsterEntity {

    @CapabilityInject(IItemHandler.class)
    private static Capability<IItemHandler> ITEM_HANDLER_CAPABILITY = null;

    private IItemHandlerModifiable inventory;

    public IllagerBanditEntity(EntityType<? extends MonsterEntity> type, World world) {
        super(type, world);
        this.inventory = new ItemStackHandler(27);
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new LookRandomlyGoal(this));
        this.goalSelector.addGoal(2, new RandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(2, new AvoidEntityGoal(this, IronGolemEntity.class, 5, 1.0D, 1.2D));
        this.goalSelector.addGoal(3, new SearchVillagesDuringNightGoal(this, 1.0D));
        this.goalSelector.addGoal(10, new StealChestsDuringNightGoal(this, 1.2D));
    }

    @Override
    public boolean canDespawn(double distanceToClosestPlayer) {
        if (super.canDespawn(distanceToClosestPlayer)) {
            for (int slot = 0; slot < inventory.getSlots(); slot++) {
                if (!inventory.getStackInSlot(slot).isEmpty()) return false;
            }
            return true;
        } else return false;
    }

    @Override
    protected void dropInventory() {
        super.dropInventory();
        for (int slot = 0; slot < inventory.getSlots(); slot++) {
            if (!inventory.getStackInSlot(slot).isEmpty()) entityDropItem(inventory.getStackInSlot(slot));
        }
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction facing) {
        if (cap == ITEM_HANDLER_CAPABILITY) {
            return LazyOptional.of(() -> (T)inventory);
        } else return super.getCapability(cap, facing);
    }

    private static class SearchVillagesDuringNightGoal extends Goal {

        private final IllagerBanditEntity entity;
        private final double speed;
        private Path path;
        private final PathNavigator navigator;

        public SearchVillagesDuringNightGoal(IllagerBanditEntity entity, double speed) {
            this.entity = entity;
            this.navigator = entity.getNavigator();
            this.speed = speed;
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        @Override
        public boolean shouldExecute() {
            if (!this.entity.world.isDaytime()) {
                BlockPos nearestVillage = entity.world.findNearestStructure(Structures.VILLAGE.getStructureName(), entity.getPosition(), 255, false);
                if (Structures.VILLAGE.isPositionInsideStructure(this.entity.world, this.entity.getPosition())) return false;
                this.path = this.navigator.getPathToPos(nearestVillage, 0);
                return this.path != null;
            } else return false;
        }

        @Override
        public boolean shouldContinueExecuting() {
            return !this.navigator.noPath();
        }

        @Override
        public void startExecuting() {
            this.navigator.setPath(this.path, this.speed);
        }

        @Override
        public void tick() {
            super.tick();
            if (Structures.VILLAGE.isPositionInsideStructure(this.entity.world, this.entity.getPosition())) {
                this.navigator.clearPath();
            }
        }
    }

    private static class StealChestsDuringNightGoal extends Goal {

        private final IllagerBanditEntity entity;
        private final PathNavigator navigator;
        private final double speed;
        private Path path;
        private Status status = Status.DONE;
        private LockableLootTileEntity target;
        private int tickCount = 0;

        public StealChestsDuringNightGoal(IllagerBanditEntity entity, double speed) {
            this.entity = entity;
            this.navigator = entity.navigator;
            this.speed = speed;
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        @Override
        public boolean shouldExecute() {
            this.status = Status.DONE;
            if (!this.entity.world.isDaytime()) {
                this.status = Status.SEARCHING;
                List<BlockPos> possibleTargets = new ArrayList<>();
                for (TileEntity te: this.entity.world.loadedTileEntityList) {
                    if (te instanceof ChestTileEntity || te instanceof BarrelTileEntity) {
                        possibleTargets.add(te.getPos());
                    }
                }
                BlockPos target = null;
                double minDistance = Double.MAX_VALUE;
                for (BlockPos pos: possibleTargets) {
                    double distance = this.entity.getDistanceSq(pos.getX(), pos.getY(), pos.getZ());
                    if (distance < minDistance) {
                        target = pos;
                        minDistance = distance;
                    }
                }
                if (target == null) return false;
                this.path = this.navigator.getPathToPos(target, 0);
                this.target = (LockableLootTileEntity) this.entity.world.getTileEntity(target);
                return this.path != null;
            } else return false;
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        @Override
        public boolean shouldContinueExecuting() {
            return status == Status.DONE;
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        @Override
        public void startExecuting() {
            this.navigator.setPath(this.path, this.speed);
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        @Override
        public void tick() {
            super.tick();
            if (target.isRemoved()) status = Status.DONE;
            switch (status) {
                case SEARCHING:
                    if (this.entity.getDistanceSq(target.getPos().getX(), target.getPos().getY(), target.getPos().getZ()) < 2) {
                        status = Status.STEALING;
                        this.navigator.clearPath();
                    } else break;
                case STEALING:
                    // TODO: Visuals
                    if (!target.isEmpty() && tickCount++ >= 20) { // Every second
                        tickCount = 0;
                        for (int slot = 0; slot < target.getSizeInventory(); slot++) {
                            ItemStack stack = target.getStackInSlot(slot);
                            if (!stack.isEmpty()) {
                                int targetSlot = getTargetSlot(entity.inventory, stack);
                                if (targetSlot == -1) {
                                    status = status.DONE;
                                    break;
                                }
                                stack = entity.inventory.insertItem(targetSlot, stack, false);
                                target.setInventorySlotContents(slot, stack);
                                break;
                            }
                            status = Status.DONE;
                        }
                    } else if (target.isEmpty()) status = Status.DONE;
            }
        }

        public int getTargetSlot(IItemHandler inventory, ItemStack newStack) {
            for (int slot = 0; slot < inventory.getSlots(); slot++) {
                ItemStack stack = inventory.getStackInSlot(slot);
                if (stack.isEmpty() || (stack.getItem().equals(newStack.getItem()) && stack.getCount() < stack.getMaxStackSize())) {
                    return slot;
                }
            }
            return -1; // Full inventory
        }

        private enum Status {SEARCHING, STEALING, DONE};
    }
}
