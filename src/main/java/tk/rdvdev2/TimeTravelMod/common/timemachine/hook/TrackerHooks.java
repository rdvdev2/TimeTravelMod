package tk.rdvdev2.TimeTravelMod.common.timemachine.hook;

import net.minecraft.entity.Entity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tk.rdvdev2.TimeTravelMod.TimeTravelMod;
import tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.TimeMachineHook;
import tk.rdvdev2.TimeTravelMod.common.timemachine.TimeMachineHookRunner;

public class TrackerHooks {

    public static final TimeMachineHook[] HOOKS = {new TeleporterHook()};

    public static class TeleporterHook implements TimeMachineHook.TeleporterTasks {

        @Override
        public void run(TimeMachineHookRunner tm, Entity entity, World worldIn, World worldOut, BlockPos controllerPos, Direction side, boolean shouldBuild) {
            tm.removeHooks().teleporterTasks(entity, worldIn, worldOut, controllerPos, side, shouldBuild);
            if (shouldBuild) {
                TimeTravelMod.LOGGER.info("Tracker Hook is working fine!");
            }
        }
    }
}
