package tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tk.rdvdev2.TimeTravelMod.common.timemachine.TimeMachineHookRunner;

import javax.annotation.Nullable;
import java.util.Optional;

public abstract interface TimeMachineHook<R> {

    static final Class<? extends TimeMachineHook>[] HOOK_TYPES = new Class[]{
            RunHook.class,
            TierHook.class,
            EntityMaxLoadHook.class,
            CooldownHook.class,
            TriggerTemporalExplosionHook.class,
            TeleporterTasks.class
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

    public interface CooldownHook extends TimeMachineHook<Integer> {
        @Override
        default Integer run(Optional<Integer> original, TimeMachineHookRunner tm, Object... args) {
            return run(original, tm);
        }

        int run(Optional<Integer> original, TimeMachineHookRunner tm);
    }

    @SuppressWarnings("unchecked")
    public interface TriggerTemporalExplosionHook extends TimeMachineHook<Boolean> {
        @Override
        default Boolean run(Optional<Boolean> original, TimeMachineHookRunner tm, Object... args) {
            return run(original, tm, (World) args[0], (BlockPos) args[1], (Direction) args[2]);
        }

        boolean run(Optional<Boolean> original, TimeMachineHookRunner tm, World world, BlockPos controllerPos, Direction side);
    }

    @SuppressWarnings("unchecked")
    public interface TeleporterTasks extends TimeMachineHook<Void> {
        @Override
        default Void run(Optional<Void> original, TimeMachineHookRunner tm, Object... args) {
            run(tm, (Entity) args[0], (World) args[1], (World) args[2], (BlockPos) args[3], (Direction) args[4], (boolean) args[5]);
            return null;
        }

        void run(TimeMachineHookRunner tm, @Nullable Entity entity, World worldIn, World worldOut, BlockPos controllerPos, Direction side, boolean shouldBuild);
    }
}
