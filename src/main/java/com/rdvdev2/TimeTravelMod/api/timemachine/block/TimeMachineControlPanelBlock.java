package com.rdvdev2.TimeTravelMod.api.timemachine.block;

import com.google.common.collect.Lists;
import com.rdvdev2.TimeTravelMod.ModRegistries;
import com.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;
import com.rdvdev2.TimeTravelMod.api.timemachine.exception.IncompatibleTimeMachineHooksException;
import com.rdvdev2.TimeTravelMod.common.util.TimeMachineUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.HashMap;

/**
 * This subclass of {@link Block} is meant to be used on blocks that will act as a Time Machine Control Panel.
 * This block will provide a GUI to control the Time Machine on right click without needing to overwrite nothing on the class.
 */
public class TimeMachineControlPanelBlock extends Block {

    private TimeMachine timeMachine = null;

    /**
     * @see Block#Block(Properties)
     */
    public TimeMachineControlPanelBlock(Properties properties) {
        super(properties);
        MinecraftForge.EVENT_BUS.register(this);
    }

    /**
     * Returns the {@link TimeMachine} that belongs to this block
     * @return The compatible {@link TimeMachine}
     */
    public final TimeMachine getTimeMachine() {
        if (this.timeMachine == null) {
            timeMachine = ModRegistries.TIME_MACHINES.getValue(((HashMap<BlockState, ResourceLocation>) ModRegistries.TIME_MACHINES.getSlaveMap(ModRegistries.CONTROLLERTOTM, HashMap.class)).get(this.getDefaultState()));
        }
        return timeMachine;
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public ActionResultType onBlockActivated(BlockState state,
                                           World worldIn,
                                           BlockPos pos,
                                           PlayerEntity playerIn,
                                           Hand hand,
                                           BlockRayTraceResult blockRayTraceResult) {
        Direction side = blockRayTraceResult.getFace();
        if (!worldIn.isRemote && !(side == Direction.UP || side == Direction.DOWN)) {
            TimeMachine hookRunner = null;
            try {
                hookRunner = getTimeMachine().hook(worldIn, pos, side);
                hookRunner.run(worldIn, playerIn, pos, side);
                return ActionResultType.SUCCESS;
            } catch (IncompatibleTimeMachineHooksException e) {
                TranslationTextComponent message = new TranslationTextComponent("timetravelmod.error.uncompatible_upgrades", TimeMachineUtils.concatUncompatibilities(Lists.newArrayList(e.getIncompatibilities())));
                playerIn.sendStatusMessage(message, false);
                return ActionResultType.FAIL;
            }
        } else return ActionResultType.FAIL;
    }

}
