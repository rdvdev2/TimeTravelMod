package tk.rdvdev2.TimeTravelMod.common.timemachine;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
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
        for(Class hook: TimeMachineHook.HOOK_TYPES) {
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
        return tm.getCooldownTime();
    }

    @Override
    public int getTier() {
        return runHooks(tm::getTier, TimeMachineHook.TierHook.class);
    }

    @Override
    public List<BlockPos> coreBlocksPos() {
        return tm.coreBlocksPos();
    }

    @Override
    public List<BlockPos> basicBlocksPos() {
        return tm.basicBlocksPos();
    }

    @Override
    public List<BlockPos> airBlocksPos() {
        return tm.airBlocksPos();
    }

    @Override
    public BlockState[] getControllerBlocks() {
        return tm.getControllerBlocks();
    }

    @Override
    public BlockState[] getCoreBlocks() {
        return tm.getCoreBlocks();
    }

    @Override
    public BlockState[] getBasicBlocks() {
        return tm.getBasicBlocks();
    }

    @Override
    public List<BlockPos> getCoreBlocksPos(Direction side) {
        return tm.getCoreBlocksPos(side);
    }

    @Override
    public List<BlockPos> getBasicBlocksPos(Direction side) {
        return tm.getBasicBlocksPos(side);
    }

    @Override
    public List<BlockPos> getAirBlocksPos(Direction side) {
        return tm.getAirBlocksPos(side);
    }

    @Override
    public int getEntityMaxLoad() {
        return runHooks(tm::getEntityMaxLoad, TimeMachineHook.EntityMaxLoadHook.class);
    }

    @Override
    public BlockState[] getBlocks() {
        return tm.getBlocks();
    }

    @Override
    public void run(World world, PlayerEntity playerIn, BlockPos controllerPos, Direction side) {
        runVoidHooks(() -> tm.run(world, playerIn, controllerPos, side), TimeMachineHook.RunHook.class, world, playerIn, controllerPos, side);
    }

    @Override
    public boolean triggerTemporalExplosion(World world, BlockPos controllerPos, Direction side) {
        return tm.triggerTemporalExplosion(world, controllerPos, side);
    }

    @Override
    public boolean isBuilt(World world, BlockPos controllerPos, Direction side) {
        return tm.isBuilt(world, controllerPos, side);
    }

    @Override
    public boolean isOverloaded(World world, BlockPos controllerPos, Direction side) {
        return tm.isOverloaded(world, controllerPos, side);
    }

    @Override
    public boolean isPlayerInside(World world, BlockPos controllerPos, Direction side, PlayerEntity player) {
        return tm.isPlayerInside(world, controllerPos, side, player);
    }

    @Override
    public AxisAlignedBB getAirSpace(BlockPos controllerPos, Direction side) {
        return tm.getAirSpace(controllerPos, side);
    }

    @Override
    public void teleporterTasks(Entity entity, World worldIn, World worldOut, BlockPos controllerPos, Direction side, boolean shouldBuild) {
        tm.teleporterTasks(entity, worldIn, worldOut, controllerPos, side, shouldBuild);
    }

    @Override
    public boolean isCooledDown(World world, BlockPos controllerPos, Direction side) {
        return tm.isCooledDown(world, controllerPos, side);
    }

    private <T> T runHooks(Supplier<T> original, Class<? extends TimeMachineHook> clazz, Object... args) {
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

    private void runVoidHooks(Runnable original, Class<? extends TimeMachineHook> clazz, Object... args) {
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
