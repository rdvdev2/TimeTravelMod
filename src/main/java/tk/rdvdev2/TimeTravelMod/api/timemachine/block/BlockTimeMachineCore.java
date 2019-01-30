package tk.rdvdev2.TimeTravelMod.api.timemachine.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tk.rdvdev2.TimeTravelMod.api.timemachine.entity.TileEntityTMCooldown;
import tk.rdvdev2.TimeTravelMod.common.world.TemporalExplosion;

import java.util.Random;

import static tk.rdvdev2.TimeTravelMod.api.timemachine.block.PropertyTMReady.ready;

public abstract class BlockTimeMachineCore extends BlockTimeMachineComponent {

    private float randomExplosionChance = 0.001F;

    public BlockTimeMachineCore(Material material) {
        super(material);
        setDefaultState(blockState.getBaseState().withProperty(ready, true));
    }

    /**
     * Gets the chance of the Time Machine core to explode
     * @return The chance (x/1)
     */
    public float getRandomExplosionChance() {
        return 0.001F;
    }
    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, ready);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        if (state.getValue(ready)) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        if (meta == 0)
            return blockState.getBaseState().withProperty(ready, true);
        else
            return blockState.getBaseState().withProperty(ready, false);
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return !state.getValue(ready);
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        if (!state.getValue(ready))
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
            world.setBlockToAir(pos);
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
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
        if (!state.getValue(PropertyTMReady.ready)) {
                forceExplosion(worldIn, pos);
            }
    }
}
