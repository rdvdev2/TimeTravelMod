package tk.rdvdev2.TimeTravelMod.api.timemachine.block;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.IncompatibleTimeMachineHooksException;
import tk.rdvdev2.TimeTravelMod.common.timemachine.TimeMachineHookRunner;

import javax.annotation.OverridingMethodsMustInvokeSuper;

public abstract class AbstractTimeMachineControlPanelBlock extends AbstractTimeMachineComponentBlock {

    public AbstractTimeMachineControlPanelBlock(Properties properties) {
        super(properties);
    }

    @OverridingMethodsMustInvokeSuper
    @Override
    public boolean onBlockActivated(BlockState state,
                                    World worldIn,
                                    BlockPos pos,
                                    PlayerEntity playerIn,
                                    Hand hand,
                                    BlockRayTraceResult blockRayTraceResult) {
        Direction side = blockRayTraceResult.getFace();
        if (!worldIn.isRemote && !(side == Direction.UP || side == Direction.DOWN)) {
            TimeMachineHookRunner hookRunner = null;
            try {
                hookRunner = super.getTimeMachine().hook(worldIn, pos, side);
                hookRunner.run(worldIn, playerIn, pos, side);
                return true;
            } catch (IncompatibleTimeMachineHooksException e) {
                playerIn.sendStatusMessage(new StringTextComponent("This time machine has incompatible hooks"), false); // TODO: Descriptive error and translations
                return false;
            }
        } else return false;
    }
}
