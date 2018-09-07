package com.rdvdev2.TimeTravelMod.api.timemachine.block;

import com.rdvdev2.TimeTravelMod.ModRegistries;
import com.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;
import com.rdvdev2.TimeTravelMod.api.timemachine.entity.TileEntityTMCooldown;
import com.rdvdev2.TimeTravelMod.common.event.EventSetTimeMachine;
import com.rdvdev2.TimeTravelMod.common.world.TemporalExplosion;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.HashMap;
import java.util.Random;

import static com.rdvdev2.TimeTravelMod.api.timemachine.block.PropertyTMReady.ready;

/**
 * A generic Block instance that works with the TimeMachine mechanics
 */
public abstract class BlockTimeMachineComponent extends Block {

    /**
     * The type of Time Machine block this is. The constructor sets this value
     */
    private static EnumTimeMachineComponentType stype;
    private EnumTimeMachineComponentType type;

    /**
     * The Time Machine this block belongs to. This value is automatically set
     */
    private TimeMachine timeMachine;

    private float randomExplosionChance = 0.001F;

    /**
     * The constructor of the block. It's recommended to overwrite it and call super() to set the Time Machine block type.
     * @param material
     * @param type
     */
    public BlockTimeMachineComponent(Material material, EnumTimeMachineComponentType type) {
        super(prepare(material, type));
        this.type = type;
        if (type == EnumTimeMachineComponentType.CORE)
            setDefaultState(blockState.getBaseState().withProperty(ready, true));
        else
            setDefaultState(blockState.getBaseState());
    }

    private static Material prepare(Material material, EnumTimeMachineComponentType ntype) {
        stype = ntype;
        return material;
    }

    public float getRandomExplosionChance() {
        return randomExplosionChance;
    }

    public void setRandomExplosionChance(float randomExplosionChance) {
        this.randomExplosionChance = randomExplosionChance;
    }

    /**
     * Links the block with it's corresponding Time Machine
     * @param event The linking event
     */
    @SubscribeEvent
    public final void setTimeMachine(EventSetTimeMachine event) {
        this.timeMachine = ModRegistries.timeMachinesRegistry.getValue(((HashMap<IBlockState, ResourceLocation>) ModRegistries.timeMachinesRegistry.getSlaveMap(ModRegistries.BLOCKTOTM, HashMap.class)).get(getDefaultState()));
        if (this.timeMachine == null) throw new IllegalArgumentException("This block ("+getDefaultState().toString()+") is not registered in any Time Machine");
    }

    @OverridingMethodsMustInvokeSuper
    @Override
    public boolean onBlockActivated(World worldIn,
                                    BlockPos pos,
                                    IBlockState state,
                                    EntityPlayer playerIn,
                                    EnumHand hand,
                                    EnumFacing side,
                                    float hitX,
                                    float hitY,
                                    float hitZ) {
        if (this.type == EnumTimeMachineComponentType.CONTROLPANEL &&
                !(side == EnumFacing.UP || side == EnumFacing.DOWN)) {
            timeMachine.run(worldIn, playerIn, pos, side);
            return true;
        } else return false;
    }

    /**
     * Returns the Time Machine block type
     * @return The Time Machine block type
     */
    public final EnumTimeMachineComponentType getType() {
        return type;
    }

    /**
     * Returns the Time Machine that belongs to this block
     * @return The compatible Time Machine
     */
    public final TimeMachine getTimeMachine() {
        return timeMachine;
    }

    // TODO: JavaDoc
    @Override
    public BlockStateContainer createBlockState() {
        if (this.stype == EnumTimeMachineComponentType.CORE)
            return new BlockStateContainer(this, ready);
        else
            return new BlockStateContainer(this);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        if (this.type == EnumTimeMachineComponentType.CORE) {
            if (state.getValue(ready)) {
                return 0;
            } else {
                return 1;
            }
        } else {
            return 0;
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        if (this.type == EnumTimeMachineComponentType.CORE)
            if (meta == 0)
                return blockState.getBaseState().withProperty(ready, true);
            else
                return blockState.getBaseState().withProperty(ready, false);
        else
            return blockState.getBaseState();
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        if (this.type == EnumTimeMachineComponentType.CORE)
            return !state.getValue(ready);
        return false;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        if (this.type == EnumTimeMachineComponentType.CORE)
            if (!state.getValue(ready))
                return new TileEntityTMCooldown();
            else
                throw new RuntimeException("TileEntityTMCooldown can't be created in a ready TM");
            throw new RuntimeException("TileEntityTMCooldown can be created only in TM Core blocks");
    }

    public boolean randomExplosion(World world, BlockPos pos, float approtation) {
        if (this.type == EnumTimeMachineComponentType.CORE) {
            Random r = new Random();
            if (r.nextFloat() < randomExplosionChance+approtation) {
                world.setBlockToAir(pos);
                new TemporalExplosion(world, null, pos, 4.0F).explode();
                return true;
            }
        }
        return false;
    }

    public boolean randomExplosion(World world, BlockPos pos) {
        return randomExplosion(world, pos, 0);
    }

    public boolean forceExplosion(World world, BlockPos pos) {
        return randomExplosion(world, pos, 1);
    }

    @Override
    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
        if (this.type == EnumTimeMachineComponentType.CORE) {
            if (!state.getValue(PropertyTMReady.ready)) {
                forceExplosion(worldIn, pos);
            }
        }
    }
}
