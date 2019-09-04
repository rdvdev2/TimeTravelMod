package tk.rdvdev2.TimeTravelMod.common.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.tileentity.BarrelTileEntity;
import net.minecraft.tileentity.ChestTileEntity;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.structure.Structures;

import java.util.ArrayList;
import java.util.List;

public class IllagerBanditEntity extends MonsterEntity {

    public IllagerBanditEntity(EntityType<? extends MonsterEntity> type, World world) {
        super(type, world);
    }

    @Override
    public boolean canSpawn(IWorld worldIn, SpawnReason spawnReasonIn) {
        return super.canSpawn(worldIn, spawnReasonIn);
    }

    @Override
    protected PathNavigator createNavigator(World worldIn) {
        PathNavigator navigator = super.createNavigator(worldIn);
        navigator.setCanSwim(true);
        return navigator;
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
        this.goalSelector.addGoal(4, new StealChestsDuringNightGoal(this, 1.2D));
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
            switch (status) {
                case SEARCHING:
                    if (this.navigator.noPath()) status = Status.STEALING;
                case STEALING:
                    // TODO: Start stealing
            }
        }

        private enum Status {SEARCHING, STEALING, DONE};
    }
}
