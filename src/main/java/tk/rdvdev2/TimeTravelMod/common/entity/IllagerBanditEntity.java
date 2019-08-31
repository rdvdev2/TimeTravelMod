package tk.rdvdev2.TimeTravelMod.common.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.MonsterEntity;
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
}
