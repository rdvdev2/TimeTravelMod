package tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tk.rdvdev2.TimeTravelMod.common.timemachine.TimeMachineHookRunner;

import java.util.Optional;

public interface TimeMachineHook<R> {

    static final Class[] HOOK_TYPES = new Class[]{
            RunHook.class,
            TierHook.class,
            EntityMaxLoadHook.class
    };

    R run(Optional<R> original, TimeMachineHookRunner tm, Object... args);

    interface RunHook extends TimeMachineHook<Void> {

        @Override
        default Void run(Optional<Void> original, TimeMachineHookRunner tm, Object... args) {
            run(tm, (World) args[0], (PlayerEntity) args[1], (BlockPos) args[2], (Direction) args[3]);
            return null;
        }

        void run(TimeMachineHookRunner tm, World world, PlayerEntity playerIn, BlockPos controllerPos, Direction side);
    }

    @SuppressWarnings("unchecked")
    interface TierHook extends TimeMachineHook<Integer> {

        @Override
        default Integer run(Optional<Integer> original, TimeMachineHookRunner tm, Object... args) {
            return run(original, tm);
        }

        int run(Optional<Integer> original, TimeMachineHookRunner tm);
    }

    @SuppressWarnings("unchecked")
    interface EntityMaxLoadHook extends TimeMachineHook<Integer> {

        @Override
        default Integer run(Optional<Integer> original, TimeMachineHookRunner tm, Object... args) {
            return run(original, tm);
        }

        int run(Optional<Integer> original, TimeMachineHookRunner tm);
    }
}
