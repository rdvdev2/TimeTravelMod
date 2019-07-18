package tk.rdvdev2.TimeTravelMod.api.timemachine.block;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import tk.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;
import tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.IncompatibleTimeMachineHooksException;
import tk.rdvdev2.TimeTravelMod.common.event.ConfigureTimeMachineBlocksEvent;
import tk.rdvdev2.TimeTravelMod.common.timemachine.TimeMachineHookRunner;

import javax.annotation.OverridingMethodsMustInvokeSuper;

public abstract class AbstractTimeMachineControlPanelBlock extends AbstractTimeMachineComponentBlock {

    private TimeMachine timeMachine = null;

    public AbstractTimeMachineControlPanelBlock(Properties properties) {
        super(properties);
        MinecraftForge.EVENT_BUS.register(this);
    }

    /**
     * Links the controller with it's corresponding Time Machine
     * @param event The linking event
     */
    @SuppressWarnings("unchecked")
    @SubscribeEvent
    public void setTimeMachine(ConfigureTimeMachineBlocksEvent event) {
        this.timeMachine = event.getTimeMachine(getDefaultState());
        if (this.timeMachine == null)
            throw new IllegalArgumentException("This block (" + getDefaultState().toString() + ") is not registered in any Time Machine");
    }

    /**
     * Returns the Time Machine that belongs to this block
     * @return The compatible Time Machine
     */
    public final TimeMachine getTimeMachine() {
        return timeMachine;
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
                hookRunner = getTimeMachine().hook(worldIn, pos, side);
                hookRunner.run(worldIn, playerIn, pos, side);
                return true;
            } catch (IncompatibleTimeMachineHooksException e) {
                playerIn.sendStatusMessage(new StringTextComponent("This time machine has incompatible hooks"), false); // TODO: Descriptive error and translations
                return false;
            }
        } else return false;
    }
}
