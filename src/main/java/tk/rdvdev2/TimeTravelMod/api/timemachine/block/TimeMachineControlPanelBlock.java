package tk.rdvdev2.TimeTravelMod.api.timemachine.block;

import com.google.common.collect.Lists;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import tk.rdvdev2.TimeTravelMod.ModRegistries;
import tk.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;
import tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.IncompatibleTimeMachineHooksException;
import tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.TimeMachineUpgrade;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.ArrayList;
import java.util.HashMap;

public class TimeMachineControlPanelBlock extends AbstractTimeMachineComponentBlock {

    private TimeMachine timeMachine = null;

    public TimeMachineControlPanelBlock(Properties properties) {
        super(properties);
        MinecraftForge.EVENT_BUS.register(this);
    }

    /**
     * Returns the Time Machine that belongs to this block
     * @return The compatible Time Machine
     */
    public final TimeMachine getTimeMachine() {
        if (this.timeMachine == null) {
            timeMachine = ModRegistries.TIME_MACHINES.getValue(((HashMap<BlockState, ResourceLocation>) ModRegistries.TIME_MACHINES.getSlaveMap(ModRegistries.CONTROLLERTOTM, HashMap.class)).get(this.getDefaultState()));
        }
        return timeMachine;
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public boolean onBlockActivated(BlockState state,
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
                return true;
            } catch (IncompatibleTimeMachineHooksException e) {
                TranslationTextComponent message = new TranslationTextComponent("timetravelmod.error.uncompatible_upgrades", concatUncompatibilities(Lists.newArrayList(e.getIncompatibilities())));
                playerIn.sendStatusMessage(message, false);
                return false;
            }
        } else return false;
    }

    private static TranslationTextComponent concatUncompatibilities(ArrayList<TimeMachineUpgrade> upgrades) {
        if (upgrades.size() != 1) {
            String separator = upgrades.size() > 2 ? "timetravelmod.generic.comma" : "timetravelmod.generic.and";
            return new TranslationTextComponent(separator, upgrades.remove(0).getName(), concatUncompatibilities(upgrades));
        } else return upgrades.remove(0).getName();
    }
}
