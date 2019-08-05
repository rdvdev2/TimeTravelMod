package tk.rdvdev2.TimeTravelMod.common.timemachine;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tk.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;
import tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.TimeMachineHook;
import tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.TimeMachineUpgrade;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class TimeMachineHookRunner extends TimeMachine {

    TimeMachine tm;
    TimeMachineUpgrade[] upgrades;

    public TimeMachineHookRunner(TimeMachine tm, TimeMachineUpgrade[] upgrades) {
        this.tm = tm;
        this.upgrades = upgrades;
        setRegistryName(tm.getRegistryName()); // Hack to get the proper time machine name and description
    }

    public TimeMachine removeHooks() {
        return this.tm;
    }

    public HashSet<TimeMachineUpgrade> checkIncompatibilities() {
        HashSet<TimeMachineUpgrade> incompatibilities = new HashSet<>(0);
        for(Class<? extends TimeMachineHook> hook: TimeMachineHook.HOOK_TYPES) {
            HashSet<TimeMachineUpgrade> found = new HashSet<>(0);
            for (TimeMachineUpgrade upgrade: this.upgrades) {
                if (upgrade.isExclusiveHook(hook)) found.add(upgrade);
            }
            if (found.size() > 1) incompatibilities.addAll(found);
        }
        return incompatibilities;
    }

    @Override
    public int getCooldownTime() {
        return runHooks(tm::getCooldownTime, TimeMachineHook.CooldownHook.class);
    }

    @Override
    public int getTier() {
        return runHooks(tm::getTier, TimeMachineHook.TierHook.class);
    }

    @Override
    public List<BlockPos> coreBlocksPos() {
        return tm.coreBlocksPos(); // Directly delegate, this isn't in the Upgrades scope
    }

    @Override
    public List<BlockPos> basicBlocksPos() {
        return tm.basicBlocksPos(); // Directly delegate, this isn't in the Upgrades scope
    }

    @Override
    public List<BlockPos> airBlocksPos() {
        return tm.airBlocksPos(); // Directly delegate, this isn't in the Upgrades scope
    }

    @Override
    public BlockState[] getControllerBlocks() {
        return tm.getControllerBlocks(); // Directly delegate, this isn't in the Upgrades scope
    }

    @Override
    public BlockState[] getCoreBlocks() {
        return tm.getCoreBlocks(); // Directly delegate, this isn't in the Upgrades scope
    }

    @Override
    public BlockState[] getBasicBlocks() {
        return tm.getBasicBlocks(); // Directly delegate, this isn't in the Upgrades scope
    }

    @Override
    public int getEntityMaxLoad() {
        return runHooks(tm::getEntityMaxLoad, TimeMachineHook.EntityMaxLoadHook.class);
    }

    @Override
    public void run(World world, PlayerEntity playerIn, BlockPos controllerPos, Direction side) {
        runVoidHooks(() -> tm.run(world, playerIn, controllerPos, side), TimeMachineHook.RunHook.class, world, playerIn, controllerPos, side);
    }

    @Override
    public boolean triggerTemporalExplosion(World world, BlockPos controllerPos, Direction side) {
        return runHooks(() -> tm.triggerTemporalExplosion(world, controllerPos, side), TimeMachineHook.TriggerTemporalExplosionHook.class);
    }

    @Override
    public void teleporterTasks(Entity entity, World worldIn, World worldOut, BlockPos controllerPos, Direction side, boolean shouldBuild) {
        runVoidHooks(() -> tm.teleporterTasks(entity, worldIn, worldOut, controllerPos, side, shouldBuild), TimeMachineHook.TeleporterTasks.class);
    }

    private <T> T runHooks(Supplier<T> original, Class<? extends TimeMachineHook<T>> clazz, Object... args) {
        for(TimeMachineUpgrade upgrade:upgrades) {
            if (upgrade.isExclusiveHook(clazz)) {
                return upgrade.runHook(Optional.empty(), clazz, this, args);
            }
        }
        T result = original.get();
        for (TimeMachineUpgrade upgrade:upgrades) {
            result = upgrade.runHook(Optional.of(result), clazz, this, args);
        }
        return result;
    }

    private void runVoidHooks(Runnable original, Class<? extends TimeMachineHook<Void>> clazz, Object... args) {
        for(TimeMachineUpgrade upgrade:upgrades) {
            if (upgrade.isExclusiveHook(clazz)) {
                upgrade.runVoidHook(clazz, this, args);
                return;
            }
        }
        boolean done = false;
        for(TimeMachineUpgrade upgrade:upgrades) {
            if (upgrade.runVoidHook(clazz, this, args))
                done = true;
        }
        if (!done) original.run();
    }
}
