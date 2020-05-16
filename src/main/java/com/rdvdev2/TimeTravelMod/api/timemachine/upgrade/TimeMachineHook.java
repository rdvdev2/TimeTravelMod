package com.rdvdev2.TimeTravelMod.api.timemachine.upgrade;

import com.rdvdev2.TimeTravelMod.api.dimension.TimeLine;
import com.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;
import com.rdvdev2.TimeTravelMod.api.timemachine.TimeMachineTemplate;
import com.rdvdev2.TimeTravelMod.api.timemachine.block.TimeMachineControlPanelBlock;
import com.rdvdev2.TimeTravelMod.api.timemachine.block.TimeMachineCoreBlock;
import com.rdvdev2.TimeTravelMod.api.timemachine.block.TimeMachineUpgradeBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ITeleporter;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * This interface represents Time Machine Hooks. Hooks are activated using {@link TimeMachineUpgradeBlock} and they allow to change the behaviour of the {@link TimeMachine}.
 * {@link Void} hooks will totally replace the behaviour of the original function.
 * Non void hooks will replace the original behaviour if they run in exclusive mode. Otherwise they will modify the returned value of the original function or another hook.
 * @param <R> The return type of the hook
 */
public abstract interface TimeMachineHook<R> {

    @Deprecated
    R run(Optional<R> original, TimeMachine tm, Object... args);

    /**
     * @see TimeMachine#run(World, PlayerEntity, BlockPos, Direction)
     */
    interface RunHook extends TimeMachineHook<Void> {

        @Override
        default Void run(Optional<Void> original, TimeMachine tm, Object... args) {
            run(tm, (World) args[0], (PlayerEntity) args[1], (BlockPos) args[2], (Direction) args[3]);
            return null;
        }

        /**
         * Starts the {@link TimeMachine} and shows the {@link TimeLine} selection GUI to the {@link PlayerEntity}
         * @param tm The hooked {@link TimeMachine}
         * @param world The {@link World} where the {@link TimeMachine} is built
         * @param playerIn The {@link PlayerEntity} using the {@link TimeMachine}
         * @param controllerPos The {@link BlockPos} of the {@link TimeMachineControlPanelBlock}
         * @param side The orientation of the {@link TimeMachine}
         */
        void run(TimeMachine tm, World world, PlayerEntity playerIn, BlockPos controllerPos, Direction side);
    }

    /**
     * @see TimeMachineTemplate#getTier()
     */
    interface TierHook extends TimeMachineHook<Integer> {

        @Override
        default Integer run(Optional<Integer> original, TimeMachine tm, Object... args) {
            return run(original, tm);
        }

        /**
         * Returns the {@link TimeMachine} tier
         * @param original The original return value
         * @param tm The hooked {@link TimeMachine}
         * @return The {@link TimeMachine} tier
         */
        int run(Optional<Integer> original, TimeMachine tm);
    }

    /**
     * @see TimeMachineTemplate#getEntityMaxLoad()
     */
    interface EntityMaxLoadHook extends TimeMachineHook<Integer> {

        @Override
        default Integer run(Optional<Integer> original, TimeMachine tm, Object... args) {
            return run(original, tm);
        }

        /**
         * Returns the maximum quantity of {@link Entity}s that the {@link TimeMachine} can transport
         * @param original The original return value
         * @param tm The hooked {@link TimeMachine}
         * @return The {@link TimeMachine} maximum {@link Entity} load
         */
        int run(Optional<Integer> original, TimeMachine tm);
    }

    /**
     * @see TimeMachineTemplate#getCooldownTime()
     */
    interface CooldownHook extends TimeMachineHook<Integer> {

        @Override
        default Integer run(Optional<Integer> original, TimeMachine tm, Object... args) {
            return run(original, tm);
        }

        /**
         * Returns the cooldown time of the core
         * @param original The original return value
         * @param tm The hooked {@link TimeMachine}
         * @return Cooldown time in game ticks (1s = 20t)
         */
        int run(Optional<Integer> original, TimeMachine tm);
    }

    /**
     * @see TimeMachine#triggerTemporalExplosion(World, BlockPos, Direction)
     */
    interface TriggerTemporalExplosionHook extends TimeMachineHook<Boolean> {

        @Override
        default Boolean run(Optional<Boolean> original, TimeMachine tm, Object... args) {
            return run(original, tm, (World) args[0], (BlockPos) args[1], (Direction) args[2]);
        }

        /**
         * Tries to trigger a temporal explosion on every {@link TimeMachineCoreBlock} using the defined chance on the template
         * @param original The original return value
         * @param tm The hooked {@link TimeMachine}
         * @param world The {@link World} where the {@link TimeMachine} is built
         * @param controllerPos The {@link BlockPos} of the {@link TimeMachineControlPanelBlock}
         * @param side The orientation of the {@link TimeMachine}
         * @return Whether any of the {@link TimeMachineCoreBlock}s exploded
         */
        boolean run(Optional<Boolean> original, TimeMachine tm, World world, BlockPos controllerPos, Direction side);
    }

    /**
     * @see TimeMachine#teleporterTasks(Entity, World, World, BlockPos, Direction, boolean)
     */
    interface TeleporterTasks extends TimeMachineHook<Void> {
        @Override
        default Void run(Optional<Void> original, TimeMachine tm, Object... args) {
            run(tm, (Entity) args[0], (World) args[1], (World) args[2], (BlockPos) args[3], (Direction) args[4], (boolean) args[5]);
            return null;
        }

        /**
         * Does the tasks of a {@link ITeleporter} when a time travel starts
         * @param tm The hooked {@link TimeMachine}
         * @param entity The traveling {@link Entity}
         * @param worldIn The source {@link World}
         * @param worldOut The target {@link World}
         * @param controllerPos The {@link BlockPos} of the {@link TimeMachineControlPanelBlock}
         * @param side The orientation of the {@link TimeMachine}
         * @param shouldBuild If true, the {@link TimeMachine} transportation should be triggered
         */
        void run(TimeMachine tm, @Nullable Entity entity, World worldIn, World worldOut, BlockPos controllerPos, Direction side, boolean shouldBuild);
    }

    /**
     * An array with all the allowed hook types. This is mainly used in the {@link TimeMachineUpgrade} logic.
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
