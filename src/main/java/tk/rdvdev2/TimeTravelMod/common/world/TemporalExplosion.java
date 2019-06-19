package tk.rdvdev2.TimeTravelMod.common.world;

import net.minecraft.entity.Entity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import tk.rdvdev2.TimeTravelMod.ModBlocks;
import tk.rdvdev2.TimeTravelMod.common.block.TemporalExplosionBlock;

public class TemporalExplosion {

    private World world;
    private Entity entity;
    private BlockPos pos;
    private float strength;

    public TemporalExplosion(World world, Entity entity, BlockPos pos, float strength) {
        this.world = world;
        this.entity = entity;
        this.pos = pos;
        this.strength = strength;
    }

    public void explode() {
        Explosion explosion = new Explosion(world, entity, pos.getX(), pos.getY(), pos.getZ(), strength, false, Explosion.Mode.NONE);
        explosion.doExplosionA();
        world.playSound(null, pos, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 6.0F, (1.0F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 0.7F);
        for(BlockPos block:explosion.getAffectedBlockPositions()) {
            for (Entity entity:world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(block))) {
                entity.attackEntityFrom(TemporalExplosionBlock.damage, 1000000);
            }
            world.setBlockState(block, ModBlocks.temporalExplosion.getDefaultState());
        }
    }
}
