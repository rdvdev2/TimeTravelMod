package tk.rdvdev2.TimeTravelMod.api.timemachine.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import tk.rdvdev2.TimeTravelMod.api.timemachine.entity.TileEntityTMCooldown;
import tk.rdvdev2.TimeTravelMod.common.world.TemporalExplosion;

import java.util.Random;

import static tk.rdvdev2.TimeTravelMod.api.timemachine.block.PropertyTMReady.ready;

public abstract class BlockTimeMachineCore extends BlockTimeMachineComponent {

    private float randomExplosionChance = 0.001F;

    public BlockTimeMachineCore(Properties properties) {
        super(properties);
        setDefaultState(getStateContainer().getBaseState().with(ready, true));
    }

    /**
     * Gets the chance of the Time Machine core to explode
     * @return The chance (x/1)
     */
    public float getRandomExplosionChance() {
        return 0.001F;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> stateBuilder) {
        stateBuilder.add(new IProperty[]{ready});
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return !state.get(ready);
    }

    @Override
    public TileEntity createTileEntity(IBlockState state, IBlockReader world) {
        if (!state.get(ready))
            return new TileEntityTMCooldown(super.getTimeMachine().getCooldownTime());
        else
            throw new RuntimeException("TileEntityTMCooldown can't be created in a ready TM");
    }

    /**
     * Triggers a Time Machine core random explosion
     * @param world The world where the Time Machine core is
     * @param pos The position of the Time Machine core
     * @param aportation The extra chance of the Time Machine core to explode (It is summed to the base one)
     * @return True if the Time Machine core exploded
     */
    public boolean randomExplosion(World world, BlockPos pos, float aportation) {
        Random r = new Random();
        if (r.nextFloat() < randomExplosionChance+aportation) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
            new TemporalExplosion(world, null, pos, 4.0F).explode();
            return true;
        }
        return false;
    }

    /**
     * Triggers a Time Machine core random explosion with it's default explosion chance
     * @param world The world where the Time Machine core is
     * @param pos The position of the Time Machine core
     * @return True if the Time Machine core exploded
     */
    public boolean randomExplosion(World world, BlockPos pos) {
        return randomExplosion(world, pos, 0);
    }

    /**
     * Trigger a Time Machine core explosion
     * @param world The world where the Time Machine core is
     * @param pos The position of the Time Machine core
     * @return True if the Time Machine core exploded (This should be always true)
     */
    public boolean forceExplosion(World world, BlockPos pos) {
        return randomExplosion(world, pos, 1);
    }

    @Override
    public void onPlayerDestroy(IWorld worldIn, BlockPos pos, IBlockState state) {
        if (!state.get(PropertyTMReady.ready)) {
            forceExplosion(worldIn.getWorld(), pos);
        }
    }
}
