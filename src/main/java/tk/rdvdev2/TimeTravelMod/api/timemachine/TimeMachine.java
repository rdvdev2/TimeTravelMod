package tk.rdvdev2.TimeTravelMod.api.timemachine;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.TimeMachineUpgrade;
import tk.rdvdev2.TimeTravelMod.common.timemachine.exception.IncompatibleTimeMachineHooksException;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * The Time Machine object with all it's functionality
 */
public interface TimeMachine extends TimeMachineTemplate, IForgeRegistryEntry<TimeMachine> {

    /**
     * Returns the name of the Time Machine for use in GUIs
     * @return The name of the Time Machine as a TranslationTextComponent
     */
    TranslationTextComponent getName();

    /**
     * Returns the description of the Time Machine for use in GUIs
     * @return The description of the Time Machine as a TranslationTextComponent
     */
    TranslationTextComponent getDescription();

    /**
     * Returns the valid IBlockState(s) for TM Upgrade Blocks
     * @return Array of valid IBlockStates for TM Blocks
     */
    BlockState[] getUpgradeBlocks();

    /**
     * Returns all the compatible TM Upgrades
     * @return An array including all compatible TM Upgrades
     */
    TimeMachineUpgrade[] getCompatibleUpgrades();

    /**
     * Returns the position(s) where must be a TM Core relatively to a compatible Time Machine Controller
     * @param side The orientation from wich the Time Machine is being operated
     * @return List of positions where must be a TM Core
     */
    List<BlockPos> getCoreBlocksPos(Direction side);

    /**
     * Returns the position(s) where must be a TM Basic Block or a TM Upgrade Block relatively to a compatible Time Machine Controller
     * @param side The orientation from wich the Time Machine is being operated
     * @return List of positions where must be a TM Basic Block or a TM Upgrade Block
     */
    List<BlockPos> getBasicBlocksPos(Direction side);

    /**
     * Returns the position(s) where must be air relatively to a compatible Time Machine Controller
     * @param side The orientation from wich the Time Machine is being operated
     * @return List of positions where must be a TM Core
     */
    List<BlockPos> getAirBlocksPos(Direction side);

    /**
     * Returns the absolute position where the Time Machine Upgrades are found
     * @param upgrade The specific upgrade to search for
     * @return The positions of the blocks containing the upgrade. Empty if the upgrades aren't applied.
     */
    HashSet<BlockPos> getUpgradePos(TimeMachineUpgrade upgrade);

    /**
     * Returns the valid IBlockState(s) used to build this Time Machine
     * @return An array including all the valid IBlockStates
     */
    BlockState[] getBlocks();

    /**
     * Applies all the upgrades installed in a built Time Machine
     * @param world World where the Time Machine is built
     * @param controllerPos Position of the Time Machine Controller block
     * @param side Orientation of the Time Machine
     * @return A new TimeMachine object with the necesary upgrades applied
     * @throws IncompatibleTimeMachineHooksException Throwed if an incompatible upgrade is detected
     */
    TimeMachine hook(World world, BlockPos controllerPos, Direction side) throws IncompatibleTimeMachineHooksException;

    /**
     * Removes all the applied upgrades in the Time Machine
     * @return The original TimeMachine object
     */
    TimeMachine removeHooks();

    /**
     * Detects all the Time Machine Upgrades installed in a built Time Machine
     * @param world World where the Time Machine is built
     * @param controllerPos Position of the Time Machine Controller block
     * @param side Orientation of the Time Machine
     * @return A HashMap including all the installed upgrades and the position of the blocks that added them, relative to the Time Machine Controller block
     */
    HashMap<tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.TimeMachineUpgrade, HashSet<BlockPos>> getUpgrades(World world, BlockPos controllerPos, Direction side);

    /**
     * Starts the Time Machine and shows the dimension selection GUI to the player
     * @param world World where the Time Machine is built
     * @param playerIn The player using the Time Machine
     * @param controllerPos Position of the Time Machine Controller block
     * @param side Orientation of the Time Machine
     */
    void run(World world, PlayerEntity playerIn, BlockPos controllerPos, Direction side);

    /**
     * Tries to trigger a temporal explosion on every Time Machine Core using the defined chance on the template
     * @param world World where the Time Machine is built
     * @param controllerPos Position of the Time Machine Controller block
     * @param side Orientation of the Time Machine
     * @return Whether any of the cores exploded
     */
    boolean triggerTemporalExplosion(World world, BlockPos controllerPos, Direction side);

    /**
     * Checks if the Time Machine is correctly built
     * @param world World where the Time Machine is built
     * @param controllerPos Position of the Time Machine Controller block
     * @param side Orientation of the Time Machine
     * @return Whether the Time Machine is correctly built or not
     */
    boolean isBuilt(World world, BlockPos controllerPos, Direction side);

    /**
     * Checks if the Time Machine Cores are cooled down
     * @param world World where the Time Machine is built
     * @param controllerPos Position of the Time Machine Controller block
     * @param side Orientation of the Time Machine
     * @return Whether the Time Machine Cores are cooled down
     */
    boolean isCooledDown(World world, BlockPos controllerPos, Direction side);

    /**
     * Checks if the Time Machine is overloaded
     * @param world World where the Time Machine is built
     * @param controllerPos Position of the Time Machine Controller block
     * @param side Orientation of the Time Machine
     * @return Whether the Time Machine is overloaded
     */
    boolean isOverloaded(World world, BlockPos controllerPos, Direction side);

    /**
     * Checks if the given player is inside the Time Machine
     * @param world World where the Time Machine is built
     * @param controllerPos Position of the Time Machine Controller block
     * @param side Orientation of the Time Machine
     * @param player The player to check
     * @return Whether the player is inside the Time Machine
     */
    boolean isPlayerInside(World world, BlockPos controllerPos, Direction side, PlayerEntity player);

    /**
     * Returns all the entities inside the Time Machine
     * @param world World where the Time Machine is built
     * @param controllerPos Position of the Time Machine Controller block
     * @param side Orientation of the Time Machine
     * @return A list including all the entities inside the Time Machine
     */
    List<Entity> getEntitiesInside(World world, BlockPos controllerPos, Direction side);

    /**
     * Returns the air space inside the Time Machine
     * @param controllerPos Position of the Time Machine Controller block
     * @param side Orientation of the Time Machine
     * @return An AxisAlignedBB defining the boundaries of the air space
     */
    AxisAlignedBB getAirSpace(BlockPos controllerPos, Direction side);

    /**
     * Does the tasks of an ITeleporter when a time travel starts
     * @param entity The traveling entity
     * @param worldIn The source world
     * @param worldOut The target world
     * @param controllerPos The position of the TM Controller
     * @param side The facing of the time machine
     * @param shouldBuild If true, Time Machine transportation should be triggered
     */
    void teleporterTasks(@Nullable Entity entity, World worldIn, World worldOut, BlockPos controllerPos, Direction side, boolean shouldBuild);

    /**
     * Attaches TMCooldownTileEntity to all Time Machine cores in the Time Machine to start the cooling down phase
     * @param worldIn The world of the new Time Machine
     * @param controllerPos The position of the new Time Machine controller block
     * @param side The facing of the Time Machine
     */
    void doCooldown(World worldIn, BlockPos controllerPos, Direction side);

    TimeMachine setRegistryName(String name);

    TimeMachine setRegistryName(String modID, String name);

    static TimeMachine fromTemplate(TimeMachineTemplate template) {
        return new tk.rdvdev2.TimeTravelMod.common.timemachine.TimeMachine(template);
    }
}
