package com.rdvdev2.TimeTravelMod.api.timemachine.block;

import com.rdvdev2.TimeTravelMod.common.block.tileentity.TMCooldownTileEntity;
import com.rdvdev2.TimeTravelMod.common.world.TemporalExplosion;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.Random;

/**
 * {@link Blocks} that pretend to act as a Time Machine Core must extend from this class.
 * Subclasses of this will have a cooldown and a random explosion chance integrated without needing to overwrite nothing on the class.
 */
public class TimeMachineCoreBlock extends Block {

    /**
     * This property represents whether the core is ready (isn't cooling down)
     */
    public static final BooleanProperty TM_READY = BooleanProperty.create("ready");

    /**
     * @see Block#Block(Properties)
     */
    public TimeMachineCoreBlock(Properties properties) {
        super(properties);
        setDefaultState(getStateContainer().getBaseState().with(TM_READY, true));
    }

    /**
     * Gets the chance of the Time Machine core to explode
     * @return The chance (x/1)
     */
    public float getRandomExplosionChance() {
        return 0.001F;
    }

    /**
     * @see Block#hasTileEntity(BlockState)
     */
    @Override
    @OverridingMethodsMustInvokeSuper
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(new IProperty[]{TM_READY});
    }

    /**
     * @see Block#hasTileEntity(BlockState)
     */
    @Override
    public final boolean hasTileEntity(BlockState state) {
        return !state.get(TM_READY);
    }

    /**
     * @see Block#createTileEntity(BlockState, IBlockReader)
     */
    @Override
    public final TileEntity createTileEntity(BlockState state, IBlockReader world) {
        if (!state.get(TM_READY))
            return new TMCooldownTileEntity();
        else
            throw new RuntimeException("TMCooldownTileEntity can't be created in a ready TM");
    }

    /**
     * Triggers a Time Machine Core random explosion
     * @param world The {@link World} where the Time Machine Core is
     * @param pos The {@link BlockPos} of the Time Machine Core
     * @param aportation The extra chance of the Time Machine Core to explode (It is summed to the base one)
     * @return True if the Time Machine Core exploded
     */
    public final boolean randomExplosion(World world, BlockPos pos, float aportation) {
        Random r = new Random();
        if (r.nextFloat() < getRandomExplosionChance()+aportation) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
            new TemporalExplosion(world, null, pos, 4.0F).explode();
            return true;
        }
        return false;
    }

    /**
     * Triggers a Time Machine Core random explosion with it's default explosion chance
     * @param world The {@link World} where the Time Machine Core is
     * @param pos The {@link BlockPos} of the Time Machine Core
     * @return True if the Time Machine Core exploded
     */
    public final boolean randomExplosion(World world, BlockPos pos) {
        return randomExplosion(world, pos, 0);
    }

    /**
     * Triggers a Time Machine Core explosion
     * @param world The {@link World} where the Time Machine Core is
     * @param pos The {@link BlockPos} of the Time Machine Core
     * @return True if the Time Machine Core exploded (This should be always true)
     */
    public final boolean forceExplosion(World world, BlockPos pos) {
        return randomExplosion(world, pos, 1);
    }

    /**
     * @see Block#onPlayerDestroy(IWorld, BlockPos, BlockState)
     */
    @Override
    @OverridingMethodsMustInvokeSuper
    public void onPlayerDestroy(IWorld worldIn, BlockPos pos, BlockState state) {
        if (!state.get(TM_READY)) {
            forceExplosion(worldIn.getWorld(), pos);
        }
    }
}
