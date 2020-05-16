package com.rdvdev2.TimeTravelMod.api.timemachine;

import com.rdvdev2.TimeTravelMod.api.dimension.Corruption;
import com.rdvdev2.TimeTravelMod.api.timemachine.block.TimeMachineControlPanelBlock;
import com.rdvdev2.TimeTravelMod.api.timemachine.block.TimeMachineCoreBlock;
import com.rdvdev2.TimeTravelMod.api.timemachine.block.TimeMachineUpgradeBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

import java.util.List;

/**
 * Template that defines the behaviour and the aspect of a {@link TimeMachine}
 */
public interface TimeMachineTemplate {

    /**
     * Returns the cooldown time of the core
     * @return Cooldown time in game ticks (1s = 20t)
     */
    default int getCooldownTime() {
        return 400;
    }

    /**
     * Returns the {@link TimeMachine} tier
     * @return The {@link TimeMachine} tier
     */
    default int getTier() {
        return 1;
    };

    /**
     * Returns the position(s) where there must be a {@link TimeMachineCoreBlock} relatively to a compatible {@link TimeMachineControlPanelBlock} facing north
     * @return Array of {@link BlockPos} where must be a {@link TimeMachineCoreBlock}
     */
    List<BlockPos> coreBlocksPos();

    /**
     * Returns the position(s) where must be a Time Machine Basic Block or a {@link TimeMachineUpgradeBlock} relatively to a compatible {@link TimeMachineControlPanelBlock} facing north
     * @return Array of {@link BlockPos} where must be a Time Machine Basic Block or a {@link TimeMachineUpgradeBlock}
     */
    List<BlockPos> basicBlocksPos();

    /**
     * Returns the position(s) where must be air relatively to a compatible {@link TimeMachineControlPanelBlock} facing north
     * @return Array of {@link BlockPos} where must be air
     */
    List<BlockPos> airBlocksPos();

    /**
     * Returns the valid {@link BlockState}(s) for {@link TimeMachineControlPanelBlock} blocks
     * @return Array of valid {@link BlockState}s for {@link TimeMachineControlPanelBlock} blocks
     */
    BlockState[] getControllerBlocks();

    /**
     * Returns the valid {@link BlockState}(s) for Time Machine Blocks
     * @return Array of valid {@link BlockState}s for Time Machine Blocks
     */
    BlockState[] getCoreBlocks();

    /**
     * Returns the valid {@link BlockState}(s) for Time Machine Basic Blocks
     * @return Array of valid {@link BlockState}s for Time Machine Basic Blocks
     */
    BlockState[] getBasicBlocks();

    /**
     * Returns the maximum quantity of {@link net.minecraft.entity.Entity}s that the {@link TimeMachine} can transport
     * @return The {@link TimeMachine} maximum {@link net.minecraft.entity.Entity} load
     */
    default int getEntityMaxLoad() {
        return 1;
    };

    /**
     * When higher, more {@link Corruption} will be caused by the {@link net.minecraft.entity.Entity}
     * @return The {@link Corruption} multiplier
     */
    default int getCorruptionMultiplier() {
        return 1;
    };
}
