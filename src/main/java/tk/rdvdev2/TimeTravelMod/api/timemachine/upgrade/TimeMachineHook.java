package tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface TimeMachineHook {
    <T> T run(T original, TimeMachineHookRunner tm, Object... args);

    interface RunHook extends TimeMachineHook {

        @Override
        default <T> T run(T original, TimeMachineHookRunner tm, Object... args) {
            run(tm, (World) args[0], (PlayerEntity) args[1], (BlockPos) args[2], (Direction) args[3]);
            return null;
        }

        void run(TimeMachineHookRunner tm, World world, PlayerEntity playerIn, BlockPos controllerPos, Direction side);
    }

    @SuppressWarnings("unchecked")
    interface TierHook extends TimeMachineHook {

        @Override
        default <T> T run(T original, TimeMachineHookRunner tm, Object... args) {
            if (original instanceof Integer) return (T)(Integer)run((Integer) original, tm);
            else throw new IllegalArgumentException("TierHook takes an int value");
        }

        int run(int original, TimeMachineHookRunner tm);
    }

    @SuppressWarnings("unchecked")
    interface EntityMaxLoadHook extends TimeMachineHook {

        @Override
        default <T> T run(T original, TimeMachineHookRunner tm, Object... args) {
            if (original instanceof Integer) return (T) (Integer)run((Integer) original, tm);
            else throw new IllegalArgumentException("EntityMaxLoadHook takes an int value");
        }

        int run(int original, TimeMachineHookRunner tm);
    }
}
