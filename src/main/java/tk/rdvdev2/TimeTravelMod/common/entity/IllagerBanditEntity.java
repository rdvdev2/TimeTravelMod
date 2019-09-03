package tk.rdvdev2.TimeTravelMod.common.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.RandomWalkingGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

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
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.goalSelector.addGoal(7, new RandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new AvoidEntityGoal(this, IronGolemEntity.class, 5, 1.0D, 1.2D));
    }
}
