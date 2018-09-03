package com.rdvdev2.TimeTravelMod.api.timemachine.block;

import com.rdvdev2.TimeTravelMod.common.event.EventSetTimeMachine;
import com.rdvdev2.TimeTravelMod.api.timemachine.ITimeMachine;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.OverridingMethodsMustInvokeSuper;

/**
 * A generic Block instance that works with the TimeMachine mechanics
 */
public abstract class BlockTimeMachineComponent extends Block {

    /**
     * The type of Time Machine block this is. The constructor sets this value
     */
    private EnumTimeMachineComponentType type;

    /**
     * The Time Machine this block belongs to. This value is automatically set
     */
    private ITimeMachine timeMachine;

    /**
     * The constructor of the block. It's recommended to overwrite it and call super() to set the Time Machine block type.
     * @param material
     * @param type
     */
    public BlockTimeMachineComponent(Material material, EnumTimeMachineComponentType type) {
        super(material);
        this.type = type;
    }

    /**
     * Links the block with it's corresponding Time Machine
     * @param event The linking event
     */
    public final void setTimeMachine(EventSetTimeMachine event) {
        this.timeMachine = event.getTimeMachine(this.getDefaultState());
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
        if (type == EnumTimeMachineComponentType.CONTROLPANEL) {
            timeMachine.run(worldIn, playerIn, pos, side);
            return true;
        } else {return false;}
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
    public final ITimeMachine getTimeMachine() {
        return timeMachine;
    }
}
