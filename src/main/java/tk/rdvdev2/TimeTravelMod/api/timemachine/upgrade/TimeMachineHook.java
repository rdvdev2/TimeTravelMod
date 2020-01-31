package tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tk.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * This interface represents Time Machine Hooks. Hooks are activated using Time Machine Upgrades and they allow to change the behaviour of the Time Machine.
 * Void hooks will totally replace the behaviour of the original function.
 * Non void hooks will replace the original behaviour if they run in exclusive mode. Otherwise they will modify the returned value of the original function or another hook.
 * @param <R> The return type of the hook
 */
public abstract interface TimeMachineHook<R> {

    /**
     * This is a generic function called by the Time Machine when a hook is applied. It is implemented by the specific hooks and should never be overwritten by a hook itself.
     * @param original The return value of the original function, if applicable
     * @param tm The hooked Time Machine
     * @param args The arguments recived, as an object array
     * @return The desired return value
     */
    R run(Optional<R> original, TimeMachine tm, Object... args);

    /**
     * Starts the Time Machine and shows the dimension selection GUI to the player
     */
    interface RunHook extends TimeMachineHook<Void> {

        @Override
        default Void run(Optional<Void> original, TimeMachine tm, Object... args) {
            run(tm, (World) args[0], (PlayerEntity) args[1], (BlockPos) args[2], (Direction) args[3]);
            return null;
        }

        /**
         * Starts the Time Machine and shows the dimension selection GUI to the player
         * @param tm The hooked Time Machine
         * @param world World where the Time Machine is built
         * @param playerIn The player using the Time Machine
         * @param controllerPos Position of the Time Machine Controller block
         * @param side Orientation of the Time Machine
         */
        void run(TimeMachine tm, World world, PlayerEntity playerIn, BlockPos controllerPos, Direction side);
    }

    /**
     * Returns the Time Machine tier
     */
    @SuppressWarnings("unchecked")
    interface TierHook extends TimeMachineHook<Integer> {

        @Override
        default Integer run(Optional<Integer> original, TimeMachine tm, Object... args) {
            return run(original, tm);
        }

        /**
         * Returns the Time Machine tier
         * @param original The original return value
         * @param tm The hooked Time Machine
         * @return The Time Machine tier
         */
        int run(Optional<Integer> original, TimeMachine tm);
    }

    /**
     * Returns the maximum quantity of entities that the Time Machine can transport
     */
    @SuppressWarnings("unchecked")
    interface EntityMaxLoadHook extends TimeMachineHook<Integer> {

        @Override
        default Integer run(Optional<Integer> original, TimeMachine tm, Object... args) {
            return run(original, tm);
        }

        /**
         * Returns the maximum quantity of entities that the Time Machine can transport
         * @param original The original return value
         * @param tm The hooked Time Machine
         * @return The TM maximum entity load
         */
        int run(Optional<Integer> original, TimeMachine tm);
    }

    /**
     * Returns the cooldown time of the core
     */
    public interface CooldownHook extends TimeMachineHook<Integer> {
        @Override
        default Integer run(Optional<Integer> original, TimeMachine tm, Object... args) {
            return run(original, tm);
        }

        /**
         * Returns the cooldown time of the core
         * @param original The original return value
         * @param tm The hooked Time Machine
         * @return Cooldown time in game ticks (1s = 20t)
         */
        int run(Optional<Integer> original, TimeMachine tm);
    }

    /**
     * Tries to trigger a temporal explosion on every Time Machine Core using the defined chance on the template
     */
    @SuppressWarnings("unchecked")
    public interface TriggerTemporalExplosionHook extends TimeMachineHook<Boolean> {
        @Override
        default Boolean run(Optional<Boolean> original, TimeMachine tm, Object... args) {
            return run(original, tm, (World) args[0], (BlockPos) args[1], (Direction) args[2]);
        }

        /**
         * Tries to trigger a temporal explosion on every Time Machine Core using the defined chance on the template
         * @param original The original return value
         * @param tm The hooked Time Machine
         * @param world World where the Time Machine is built
         * @param controllerPos Position of the Time Machine Controller block
         * @param side Orientation of the Time Machine
         * @return Whether any of the cores exploded
         */
        boolean run(Optional<Boolean> original, TimeMachine tm, World world, BlockPos controllerPos, Direction side);
    }

    /**
     * Does the tasks of an ITeleporter when a time travel starts
     */
    @SuppressWarnings("unchecked")
    public interface TeleporterTasks extends TimeMachineHook<Void> {
        @Override
        default Void run(Optional<Void> original, TimeMachine tm, Object... args) {
            run(tm, (Entity) args[0], (World) args[1], (World) args[2], (BlockPos) args[3], (Direction) args[4], (boolean) args[5]);
            return null;
        }

        /**
         * Does the tasks of an ITeleporter when a time travel starts
         * @param tm The hooked Time Machine
         * @param entity The traveling entity
         * @param worldIn The source world
         * @param worldOut The target world
         * @param controllerPos The position of the TM Controller
         * @param side The facing of the time machine
         * @param shouldBuild If true, Time Machine transportation should be triggered
         */
        void run(TimeMachine tm, @Nullable Entity entity, World worldIn, World worldOut, BlockPos controllerPos, Direction side, boolean shouldBuild);
    }

    /**
     * An array with all the allowed hook type. This is mainly used in the upgrade logic.
     */
    Class<? extends TimeMachineHook<?>>[] HOOK_TYPES = new Class[]{
            RunHook.class,
            TierHook.class,
            EntityMaxLoadHook.class,
            CooldownHook.class,
            TriggerTemporalExplosionHook.class,
            TeleporterTasks.class
    };
}
