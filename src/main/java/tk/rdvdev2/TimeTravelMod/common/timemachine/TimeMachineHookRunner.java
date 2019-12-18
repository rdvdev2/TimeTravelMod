package tk.rdvdev2.TimeTravelMod.common.timemachine;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import tk.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;
import tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.TimeMachineHook;
import tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.TimeMachineUpgrade;
import tk.rdvdev2.TimeTravelMod.common.timemachine.exception.IncompatibleTimeMachineHooksException;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class TimeMachineHookRunner implements TimeMachine {

    TimeMachine tm;
    private final HashMap<tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.TimeMachineUpgrade, HashSet<BlockPos>> upgrades;

    public TimeMachineHookRunner(TimeMachine tm, HashMap<tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.TimeMachineUpgrade, HashSet<BlockPos>> upgrades) {
        this.tm = tm;
        this.upgrades = upgrades;
    }

    public TimeMachine removeHooks() {
        return this.tm;
    }

    public HashSet<tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.TimeMachineUpgrade> checkIncompatibilities() {
        HashSet<tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.TimeMachineUpgrade> incompatibilities = new HashSet<>(0);
        for(Class<? extends TimeMachineHook> hook: TimeMachineHook.HOOK_TYPES) {
            HashSet<tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.TimeMachineUpgrade> found = new HashSet<>(0);
            for (TimeMachineUpgrade upgrade: this.upgrades.keySet()) {
                if (((tk.rdvdev2.TimeTravelMod.common.timemachine.upgrade.TimeMachineUpgrade) upgrade).isExclusiveHook(hook)) found.add(upgrade);
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
        return runHooks(() -> tm.triggerTemporalExplosion(world, controllerPos, side), TimeMachineHook.TriggerTemporalExplosionHook.class, world, controllerPos, side);
    }

    @Override
    public void teleporterTasks(@Nullable Entity entity, World worldIn, World worldOut, BlockPos controllerPos, Direction side, boolean shouldBuild) {
        runVoidHooks(() -> tm.teleporterTasks(entity, worldIn, worldOut, controllerPos, side, shouldBuild), TimeMachineHook.TeleporterTasks.class, entity, worldIn, worldOut, controllerPos, side, shouldBuild);
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
    public BlockState[] getBlocks() {
        return tm.getBlocks();
    }

    @Override
    public boolean isBuilt(World world, BlockPos controllerPos, Direction side) {
        return tm.isBuilt(world, controllerPos, side);
    }

    @Override
    public boolean isCooledDown(World world, BlockPos controllerPos, Direction side) {
        return tm.isCooledDown(world, controllerPos, side);
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
    public int getCorruptionMultiplier() {
        return tm.getCorruptionMultiplier();
    }

    @Override
    public String toString() {
        return tm.toString();
    }

    private <T> T runHooks(Supplier<T> original, Class<? extends TimeMachineHook<T>> clazz, Object... args) {
        for(TimeMachineUpgrade upgrade:upgrades.keySet()) {
            if (((tk.rdvdev2.TimeTravelMod.common.timemachine.upgrade.TimeMachineUpgrade) upgrade).isExclusiveHook(clazz)) {
                return ((tk.rdvdev2.TimeTravelMod.common.timemachine.upgrade.TimeMachineUpgrade) upgrade).runHook(Optional.empty(), clazz, this, args);
            }
        }
        T result = original.get();
        for (TimeMachineUpgrade upgrade:upgrades.keySet()) {
            result = ((tk.rdvdev2.TimeTravelMod.common.timemachine.upgrade.TimeMachineUpgrade) upgrade).runHook(Optional.of(result), clazz, this, args);
        }
        return result;
    }

    private void runVoidHooks(Runnable original, Class<? extends TimeMachineHook<Void>> clazz, Object... args) {
        for(TimeMachineUpgrade upgrade:upgrades.keySet()) {
            if (((tk.rdvdev2.TimeTravelMod.common.timemachine.upgrade.TimeMachineUpgrade) upgrade).isExclusiveHook(clazz)) {
                ((tk.rdvdev2.TimeTravelMod.common.timemachine.upgrade.TimeMachineUpgrade) upgrade).runVoidHook(clazz, this, args);
                return;
            }
        }
        boolean done = false;
        for(TimeMachineUpgrade upgrade:upgrades.keySet()) {
            if (((tk.rdvdev2.TimeTravelMod.common.timemachine.upgrade.TimeMachineUpgrade) upgrade).runVoidHook(clazz, this, args))
                done = true;
        }
        if (!done) original.run();
    }

    public HashSet<BlockPos> getUpgradePos(tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.TimeMachineUpgrade upgrade) {
        return upgrades.get(upgrade);
    }

    @Override
    public TranslationTextComponent getName() {
        return tm.getName();
    }

    @Override
    public TranslationTextComponent getDescription() {
        return tm.getDescription();
    }

    @Override
    public BlockState[] getUpgradeBlocks() {
        return tm.getUpgradeBlocks();
    }

    @Override
    public tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.TimeMachineUpgrade[] getCompatibleUpgrades() {
        return tm.getCompatibleUpgrades();
    }

    @Override
    public TimeMachineHookRunner hook(World world, BlockPos controllerPos, Direction side) throws IncompatibleTimeMachineHooksException {
        return this;
    }

    @Override
    public HashMap<tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.TimeMachineUpgrade, HashSet<BlockPos>> getUpgrades(World world, BlockPos controllerPos, Direction side) {
        return this.upgrades;
    }

    @Override
    public List<Entity> getEntitiesInside(World world, BlockPos controllerPos, Direction side) {
        return tm.getEntitiesInside(world, controllerPos, side);
    }

    @Override
    public void doCooldown(World worldIn, BlockPos controllerPos, Direction side) {
        tm.doCooldown(worldIn, controllerPos, side);
    }

    // Delegate registry functionality to the original Time Machine

    @Override
    public TimeMachine setRegistryName(String name) {
        return tm.setRegistryName(name);
    }

    @Override
    public TimeMachine setRegistryName(String modID, String name) {
        return tm.setRegistryName(modID, name);
    }

    @Override
    public TimeMachine setRegistryName(ResourceLocation name) {
        return tm.setRegistryName(name);
    }

    @Nullable
    @Override
    public ResourceLocation getRegistryName() {
        return tm.getRegistryName();
    }

    @Override
    public Class<TimeMachine> getRegistryType() {
        return tm.getRegistryType();
    }
}
