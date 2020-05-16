package com.rdvdev2.TimeTravelMod.api.timemachine;

import com.rdvdev2.TimeTravelMod.api.dimension.TimeLine;
import com.rdvdev2.TimeTravelMod.api.timemachine.block.TimeMachineControlPanelBlock;
import com.rdvdev2.TimeTravelMod.api.timemachine.block.TimeMachineCoreBlock;
import com.rdvdev2.TimeTravelMod.api.timemachine.block.TimeMachineUpgradeBlock;
import com.rdvdev2.TimeTravelMod.api.timemachine.exception.IncompatibleTimeMachineHooksException;
import com.rdvdev2.TimeTravelMod.api.timemachine.upgrade.TimeMachineUpgrade;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The Time Machine object with all it's functionality
 */
public interface TimeMachine extends TimeMachineTemplate, IForgeRegistryEntry<TimeMachine> {

    /**
     * Returns the name of the Time Machine for use in GUIs
     * @return The name of the Time Machine as a {@link TranslationTextComponent}
     */
    TranslationTextComponent getName();

    /**
     * Returns the description of the Time Machine for use in GUIs
     * @return The description of the Time Machine as a {@link TranslationTextComponent}
     */
    TranslationTextComponent getDescription();

    /**
     * Returns the valid {@link BlockState}(s) for {@link TimeMachineUpgradeBlock}s
     * @return Array of valid {@link BlockState}s for {@link TimeMachineUpgradeBlock}s
     */
    BlockState[] getUpgradeBlocks();

    /**
     * Returns all the compatible {@link TimeMachineUpgradeBlock}s
     * @return An array including all compatible {@link TimeMachineUpgradeBlock}s
     */
    TimeMachineUpgrade[] getCompatibleUpgrades();

    /**
     * Returns the {@link BlockPos} where there must be a {@link TimeMachineCoreBlock} relatively to a compatible {@link TimeMachineControlPanelBlock}
     * @param side The orientation from wich the Time Machine is being operated
     * @return List of {@link BlockPos} where there must be a {@link TimeMachineCoreBlock}
     */
    List<BlockPos> getCoreBlocksPos(Direction side);

    /**
     * Returns the {@link BlockPos} where there must be a Time Machine Basic Block or a {@link TimeMachineUpgradeBlock} relatively to a compatible {@link TimeMachineControlPanelBlock}
     * @param side The orientation from wich the Time Machine is being operated
     * @return List of {@link BlockPos} where there must be a Time Machine Basic Block or a {@link TimeMachineUpgradeBlock}
     */
    List<BlockPos> getBasicBlocksPos(Direction side);

    /**
     * Returns the {@link BlockPos} where there must be air relatively to a compatible {@link TimeMachineControlPanelBlock}
     * @param side The orientation from wich the Time Machine is being operated
     * @return List of {@link BlockPos} where there must be a Time Machine Core
     */
    List<BlockPos> getAirBlocksPos(Direction side);

    /**
     * Returns the absolute {@link BlockPos} where the {@link TimeMachineUpgrade}s are found
     * @param upgrade The specific {@link TimeMachineUpgrade} to search for
     * @return The {@link BlockPos} of the blocks containing the {@link TimeMachineUpgrade}. Empty if the {@link TimeMachineUpgrade}s aren't applied.
     */
    Set<BlockPos> getUpgradePos(TimeMachineUpgrade upgrade);

    /**
     * Returns the valid {@link BlockState}(s) used to build this Time Machine
     * @return An array including all the valid {@link BlockState}s
     */
    BlockState[] getBlocks();

    /**
     * Applies all the {@link TimeMachineUpgrade}s installed in a built Time Machine
     * @param world The {@link World} where the Time Machine is built
     * @param controllerPos The {@link BlockPos} of the {@link TimeMachineControlPanelBlock}
     * @param side The orientation of the Time Machine
     * @return A new Time Machine object with the necesary {@link TimeMachineUpgrade}s applied
     * @throws IncompatibleTimeMachineHooksException Throwed if an incompatible {@link TimeMachineUpgrade} is detected
     */
    TimeMachine hook(World world, BlockPos controllerPos, Direction side) throws IncompatibleTimeMachineHooksException;

    /**
     * Removes all the applied {@link TimeMachineUpgrade}s in the Time Machine
     * @return The original Time Machine object
     */
    TimeMachine removeHooks();

    /**
     * Detects all the {@link TimeMachineUpgrade}s installed in a built Time Machine
     * @param world The {@link World} where the Time Machine is built
     * @param controllerPos The {@link BlockPos} of the {@link TimeMachineControlPanelBlock}
     * @param side The orientation of the Time Machine
     * @return A {@link Map} including all the installed {@link TimeMachineUpgrade}s and the {@link BlockPos} of the blocks that added them, relative to the {@link TimeMachineControlPanelBlock}
     */
    Map<TimeMachineUpgrade, Set<BlockPos>> getUpgrades(World world, BlockPos controllerPos, Direction side);

    /**
     * Starts the Time Machine and shows the {@link TimeLine} selection GUI to the {@link PlayerEntity}
     * @param world The {@link World} where the Time Machine is built
     * @param playerIn The {@link PlayerEntity} using the Time Machine
     * @param controllerPos The {@link BlockPos} of the {@link TimeMachineControlPanelBlock}
     * @param side The orientation of the Time Machine
     */
    void run(World world, PlayerEntity playerIn, BlockPos controllerPos, Direction side);

    /**
     * Tries to trigger a temporal explosion on every {@link TimeMachineCoreBlock} using the defined chance on the template
     * @param world The {@link World} where the Time Machine is built
     * @param controllerPos The {@link BlockPos} of the {@link TimeMachineControlPanelBlock}
     * @param side The orientation of the Time Machine
     * @return Whether any of the {@link TimeMachineCoreBlock}s exploded
     */
    boolean triggerTemporalExplosion(World world, BlockPos controllerPos, Direction side);

    /**
     * Checks if the Time Machine is correctly built
     * @param world The {@link World} where the Time Machine is built
     * @param controllerPos The {@link BlockPos} of the {@link TimeMachineControlPanelBlock}
     * @param side The orientation of the Time Machine
     * @return Whether the Time Machine is correctly built or not
     */
    boolean isBuilt(World world, BlockPos controllerPos, Direction side);

    /**
     * Checks if the {@link TimeMachineCoreBlock}s are cooled down
     * @param world The {@link World} where the Time Machine is built
     * @param controllerPos The {@link BlockPos} of the {@link TimeMachineControlPanelBlock}
     * @param side The orientation of the Time Machine
     * @return Whether the {@link TimeMachineCoreBlock}s are cooled down
     */
    boolean isCooledDown(World world, BlockPos controllerPos, Direction side);

    /**
     * Checks if the Time Machine is overloaded
     * @param world The {@link World} where the Time Machine is built
     * @param controllerPos The {@link BlockPos} of the {@link TimeMachineControlPanelBlock}
     * @param side The orientation of the Time Machine
     * @return Whether the Time Machine is overloaded
     */
    boolean isOverloaded(World world, BlockPos controllerPos, Direction side);

    /**
     * Checks if the given {@link PlayerEntity} is inside the Time Machine
     * @param world The {@link World} where the Time Machine is built
     * @param controllerPos The {@link BlockPos} of the {@link TimeMachineControlPanelBlock}
     * @param side The orientation of the Time Machine
     * @param player The {@link PlayerEntity} to check
     * @return Whether the {@link PlayerEntity} is inside the Time Machine
     */
    boolean isPlayerInside(World world, BlockPos controllerPos, Direction side, PlayerEntity player);

    /**
     * Returns all the {@link Entity}s inside the Time Machine
     * @param world The {@link World} where the Time Machine is built
     * @param controllerPos The {@link BlockPos} of the {@link TimeMachineControlPanelBlock}
     * @param side The orientation of the Time Machine
     * @return A list including all the {@link Entity}s inside the Time Machine
     */
    List<Entity> getEntitiesInside(World world, BlockPos controllerPos, Direction side);

    /**
     * Returns the air space inside the Time Machine
     * @param controllerPos The {@link BlockPos} of the {@link TimeMachineControlPanelBlock}
     * @param side The orientation of the Time Machine
     * @return A {@link AxisAlignedBB} defining the boundaries of the air space
     */
    AxisAlignedBB getAirSpace(BlockPos controllerPos, Direction side);

    /**
     * Does the tasks of an {@link ITeleporter} when a time travel starts
     * @param entity The traveling {@link Entity}
     * @param worldIn The source {@link World}
     * @param worldOut The target {@link World}
     * @param controllerPos The {@link BlockPos} of the {@link TimeMachineControlPanelBlock}
     * @param side The orientation of the Time Machine
     * @param shouldBuild If true, the Time Machine transportation should be triggered
     */
    void teleporterTasks(@Nullable Entity entity, World worldIn, World worldOut, BlockPos controllerPos, Direction side, boolean shouldBuild);

    /**
     * Triggers the cooldown on all the used {@link TimeMachineCoreBlock}s
     * @param worldIn The {@link World} of the new Time Machine
     * @param controllerPos The {@link BlockPos} of the new {@link TimeMachineControlPanelBlock}
     * @param side The orientation of the Time Machine
     */
    void doCooldown(World worldIn, BlockPos controllerPos, Direction side);

    /**
     * Support method for registration
     * @see net.minecraftforge.registries.ForgeRegistryEntry#setRegistryName(String, String)
     */
    TimeMachine setRegistryName(String name);

    /**
     * Support method for registration
     * @see net.minecraftforge.registries.ForgeRegistryEntry#setRegistryName(String, String)
     */
    TimeMachine setRegistryName(String modID, String name);

    /**
     * Constructs a new Time Machine using a given {@link TimeMachineTemplate}
     * @param template The {@link TimeMachineTemplate} to use
     * @return The new Time Machine
     */
    static TimeMachine fromTemplate(TimeMachineTemplate template) {
        return new com.rdvdev2.TimeTravelMod.common.timemachine.TimeMachine(template);
    }
}
