package tk.rdvdev2.TimeTravelMod.api.timemachine;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.IChunk;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.commons.lang3.ArrayUtils;
import tk.rdvdev2.TimeTravelMod.ModBlocks;
import tk.rdvdev2.TimeTravelMod.ModRegistries;
import tk.rdvdev2.TimeTravelMod.ModTriggers;
import tk.rdvdev2.TimeTravelMod.TimeTravelMod;
import tk.rdvdev2.TimeTravelMod.api.timemachine.block.AbstractTimeMachineComponentBlock;
import tk.rdvdev2.TimeTravelMod.api.timemachine.block.AbstractTimeMachineCoreBlock;
import tk.rdvdev2.TimeTravelMod.api.timemachine.block.AbstractTimeMachineUpgradeBlock;
import tk.rdvdev2.TimeTravelMod.api.timemachine.block.TMReadyProperty;
import tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.IncompatibleTimeMachineHooksException;
import tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.TimeMachineUpgrade;
import tk.rdvdev2.TimeTravelMod.common.timemachine.TimeMachineHookRunner;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Defines the behaviour and the aspect of a Time Machine
 */
public abstract class TimeMachine implements IForgeRegistryEntry<TimeMachine> {

    // FORGE REGISTRY START

    private ResourceLocation registry_name = null;

    /**
     * Sets a unique name for this Item. This should be used for uniquely identify the instance of the Item.
     * This is the valid replacement for the atrocious 'getUnlocalizedName().substring(6)' stuff that everyone does.
     * Unlocalized names have NOTHING to do with unique identifiers. As demonstrated by vanilla blocks and items.
     * <p>
     * The supplied name will be prefixed with the currently active mod's modId.
     * If the supplied name already has a prefix that is different, it will be used and a warning will be logged.
     * <p>
     * If a name already exists, or this Item is already registered in a registry, then an IllegalStateException is thrown.
     * <p>
     * Returns 'this' to allow for chaining.
     *
     * @param name Unique registry name
     * @return This instance
     */
    @Override
    public final TimeMachine setRegistryName(ResourceLocation name) {
        if (registry_name == null)
            registry_name = name;
        else
            throw new RuntimeException("Tryed to change a TimeMachine registry name!");
        return this;
    }

    /**
     * A unique identifier for this entry, if this entry is registered already it will return it's official registry name.
     * Otherwise it will return the name set in setRegistryName().
     * If neither are valid null is returned.
     *
     * @return Unique identifier or null.
     */
    @Nullable
    @Override
    public final ResourceLocation getRegistryName() {
        return registry_name;
    }

    /**
     * Determines the type for this entry, used to look up the correct registry in the global registries list as there can only be one
     * registry per concrete class.
     *
     * @return Root registry type.
     */
    @Override
    public final Class<TimeMachine> getRegistryType() {
        return (Class<TimeMachine>)this.getClass();
    }

    // FORGE REGISTRY END

    /**
     * Gets the name of the Time Machine. Used for the Engineer's book.
     * @return The Time Machine name
     */
    abstract public TranslationTextComponent getName();

    public TranslationTextComponent getDescription() {
        return new TranslationTextComponent("tm.default.description", getTier());
    }

    /**
     * Gets the cooldown time of the core
     * @return Cooldown time in game ticks (1s = 20t)
     */
    abstract public int getCooldownTime();

    /**
     * Gets the Time Machine tier
     * @return The Time Machine tier
     */
    abstract public int getTier();

    /**
     * Returns the position(s) where must be a TM Core relatively to a compatible Time Machine Controller facing north
     * @return Array of positions where must be a TM Core
     */
    abstract public int[][] coreBlocksPos();

    /**
     * Returns the position(s) where must be a TM Basic Block or a TM Upgrade relatively to a compatible Time Machine Controller facing north
     * @return Array of positions where must be a TM Basic Block or a TM Upgrade
     */
    abstract public int[][] basicBlocksPos();

    /**
     * Returns the position(s) where must be air relatively to a compatible Time Machine Controller facing north
     * @return Array of positions where must be air
     */
    abstract public int[][] airBlocksPos();

    /**
     * Returns the valid IBlockState(s) for TM Controller blocks
     * @return Array of valid IBlockStates for TM Controller blocks
     */
    public BlockState[] getControllerBlocks() {
        return new BlockState[]{ModBlocks.timeMachineControlPanel.getDefaultState()};
    }

    /**
     * Returns the valid IBlockState(s) for TM Blocks
     * @return Array of valid IBlockStates for TM Blocks
     */
    public BlockState[] getCoreBlocks() {
        return new BlockState[]{ModBlocks.timeMachineCore.getDefaultState()};
    }

    /**
     * Returns the valid IBlockState(s) for TM Basic Blocks
     * @return Array of valid IBlockStates for TM Basic Blocks
     */
    public BlockState[] getBasicBlocks() {
        return new BlockState[]{ModBlocks.timeMachineBasicBlock.getDefaultState()};
    }

    /**
     * Returns the valid IBlockState(s) for TM Upgrade Blocks
     * @return Array of valid IBlockStates for TM Upgrade Blocks
     */
    @SuppressWarnings("unchecked")
    public final BlockState[] getUpgradeBlocks() {
        AbstractTimeMachineComponentBlock[] blocks = new AbstractTimeMachineComponentBlock[0];
        try {
            for (TimeMachineUpgrade upgrade : getCompatibleUpgrades()) {
                HashMap<TimeMachineUpgrade, AbstractTimeMachineComponentBlock[]> hm = (HashMap<TimeMachineUpgrade, AbstractTimeMachineComponentBlock[]>) ModRegistries.upgradesRegistry.getSlaveMap(ModRegistries.UPGRADETOBLOCK, HashMap.class);
                blocks = blocks == null ? hm.get(upgrade) : ArrayUtils.addAll(blocks, hm.get(upgrade));
            }
            BlockState[] states = new BlockState[0];
            for (AbstractTimeMachineComponentBlock block : blocks) {
                states = states == null ? new BlockState[]{block.getDefaultState()} : ArrayUtils.addAll(states, new BlockState[]{block.getDefaultState()});
            }
            return states;
        } catch (NullPointerException e) {
            return new BlockState[]{};
        }
    }

    @SuppressWarnings("unchecked")
    public final TimeMachineUpgrade[] getCompatibleUpgrades() {
        return ((HashMap<TimeMachine, TimeMachineUpgrade[]>)ModRegistries.upgradesRegistry.getSlaveMap(ModRegistries.TMTOUPGRADE, HashMap.class)).get(this);
    }

    /**
     * Returns the position(s) where must be a TM Core
     * @param side Facing of the time machine
     * @return Array of positions where must be a TM Core
     */
    public BlockPos[] getCoreBlocksPos(Direction side) {
        return applySide(coreBlocksPos(), side);
    }

    /**
     * Returns the position(s) where must be a TM Basic Block or a TM Upgrade
     * @param side Facing of the time machine
     * @return Array of positions where must be a TM Basic Block or a TM Upgrade
     */
    public BlockPos[] getBasicBlocksPos(Direction side) {
        return applySide(basicBlocksPos(), side);
    }

    /**
     * Returns the position(s) where must be air
     * @param side Facing of the time machine
     * @return Array of positions where must be air
     */
    public BlockPos[] getAirBlocksPos(Direction side) {
        return applySide(airBlocksPos(), side);
    }

    public int getEntityMaxLoad() {
        return 1;
    }
    /**
     * Returns all the valid IBlockStates that can be used to build this Time Machine
     * @return Array of vaild IBlockStates
     */
    public BlockState[] getBlocks() {
        if (getUpgradeBlocks().length != 0) {
            return (BlockState[]) ArrayUtils.addAll(ArrayUtils.addAll((BlockState[]) getControllerBlocks(), (BlockState[]) getCoreBlocks()), ArrayUtils.addAll((BlockState[]) getBasicBlocks(), (BlockState[]) getUpgradeBlocks()));
        } else {
            return (BlockState[]) ArrayUtils.addAll(ArrayUtils.addAll((BlockState[]) getControllerBlocks(), (BlockState[]) getCoreBlocks()), (BlockState[]) getBasicBlocks());
        }
    }

    /**
     * Converts the relative block positions to meet the actual Time Machine Facing
     * @param input An array of relative positions. The positions are represented in an int array of format {x, y, z}
     * @param side The actual facing of the Time Machine
     * @return An array of BlockPos aligned with the Time Machine facing
     */
    public final static BlockPos[] applySide(int[][] input, Direction side){
        BlockPos[] output = new BlockPos[input.length];
        for (int i = 0; i < input.length; i++) {
            switch (side.getName()) {
                case "north":
                    output[i] = new BlockPos(input[i][0], input[i][1], input[i][2]);
                    break;
                case "south":
                    output[i] = new BlockPos(input[i][0], input[i][1], input[i][2] * -1);
                    break;
                case "west":
                    output[i] = new BlockPos(input[i][2], input[i][1], input[i][0]);
                    break;
                case "east":
                    output[i] = new BlockPos(input[i][2] * -1, input[i][1], input[i][0]);
                    break;
            }
        }
        return output;
    }

    /**
     * Checks the Time Machine upgrades and returns the correct TimeMachineHookRunner
     * @param world The world where the Time Machine is built
     * @param controllerPos The position of the Time Machine controller
     * @param side The facing of the Time Machine
     * @return A TimeMachineHookRunner with all the upgrades
     */
    public final TimeMachineHookRunner hook(World world, BlockPos controllerPos, Direction side) throws IncompatibleTimeMachineHooksException {
        TimeMachineHookRunner generated;
        if (!(this instanceof TimeMachineHookRunner)) {
            generated = new TimeMachineHookRunner(this, getUpgrades(world, controllerPos, side));
            HashSet<TimeMachineUpgrade> incompatibilities = generated.checkIncompatibilities();
            if (incompatibilities.isEmpty()) {
                return generated;
            } else {
                throw new IncompatibleTimeMachineHooksException(incompatibilities);
            }
        }
        else
            return (TimeMachineHookRunner)this;
    }

    /**
     * Checks all the Time Machine upgrades applied to the Time Machine
     * @param world The world where the Time Machine is built
     * @param controllerPos The position of the Time Machine controller
     * @param side The facing of the Time Machine
     * @return An array of Time Machine upgrades that are applied to this Time Machine
     */
    public final TimeMachineUpgrade[] getUpgrades(World world, BlockPos controllerPos, Direction side) {
        TimeMachineUpgrade[] upgrades = new TimeMachineUpgrade[0];
        for (BlockPos pos:getBasicBlocksPos(side))
            for (BlockState state:getUpgradeBlocks())
                if (world.getBlockState(controllerPos.add(pos)) == state) {
                    try {
                        int id = upgrades.length;
                        upgrades = Arrays.copyOf(upgrades, id + 1);
                        upgrades[id] = ((AbstractTimeMachineUpgradeBlock)state.getBlock()).getUpgrade();
                    } catch (NullPointerException e) {
                        upgrades = new TimeMachineUpgrade[]{((AbstractTimeMachineUpgradeBlock)state.getBlock()).getUpgrade()};
                    }
                    break;
                }
        return upgrades;
    }

    /**
     * Starts the Time Machine to check if the player can be teleported
     * @param world The player's world
     * @param playerIn The player that triggered the time machine
     * @param controllerPos The position of the TM Controller
     * @param side The facing of the time machine
     */
    public void run(World world, PlayerEntity playerIn, BlockPos controllerPos, Direction side) {
        if (isBuilt(world, controllerPos, side) &&
            isCooledDown(world, controllerPos, side) &&
            isPlayerInside(world, controllerPos, side, playerIn) &&
            !isOverloaded(world, controllerPos, side) &&
            !world.isRemote) {
            if (!triggerTemporalExplosion(world, controllerPos, side))
                if (playerIn instanceof ServerPlayerEntity) {
                    ModTriggers.ACCESS_TIME_MACHINE.trigger((ServerPlayerEntity) playerIn);
                }
                TimeTravelMod.proxy.displayTMGuiScreen(playerIn, this, controllerPos, side);
        }
    }

    /**
     * Triggers a Time Machine core explosion in every Time Machine core of this Time Machine
     * @param world The world of the Time Machine
     * @param controllerPos The position of the Time Machine controller block
     * @param side The facing of the Time Machine
     * @return True if any of the Time Machine cores exploded
     */
    public boolean triggerTemporalExplosion(World world, BlockPos controllerPos, Direction side) {
        for (BlockPos pos:getCoreBlocksPos(side)) {
            AbstractTimeMachineComponentBlock core = (AbstractTimeMachineComponentBlock)world.getBlockState(controllerPos.add(pos)).getBlock();
            if (((AbstractTimeMachineCoreBlock)core).randomExplosion(world, controllerPos.add(pos)))
                return true;
        }
        return false;
    }

    /**
     * Checks if the Time Machine is correctly built
     * @param world The world were the Time Machine is built
     * @param controllerPos The position of the TM Controller
     * @param side The facing of the Time Machine
     * @return Returns true if the Time Machine is correctly built
     */
    public boolean isBuilt(World world, BlockPos controllerPos, Direction side) {
        if (isComponentTypeBuilt(TMComponentType.CORE, world, controllerPos, side) &&
            isComponentTypeBuilt(TMComponentType.BASIC, world, controllerPos, side)) {
            BlockPos[] airPos = getAirBlocksPos(side);
            for (int i = 0; i < airPos.length; i++) {
                if (world.getBlockState(controllerPos.add(airPos[i])) != Blocks.AIR.getDefaultState()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Checks if a particular component of the Time Machine is correctly built (Doesn't check if the core is cooled down)
     * @param type The component type
     * @param world The world where the Time Machine is built
     * @param controllerPos The position of the Time Machine controller
     * @param side The Time Machine facing
     * @return True if the component is correctly built
     */
    public final boolean isComponentTypeBuilt(TMComponentType type, World world, BlockPos controllerPos, Direction side) {
        BlockPos[] positions;
        BlockState[] states;

        switch (type) {
            case CORE:
                positions = getCoreBlocksPos(side);
                states = getCoreBlocks();
                break;
            case BASIC:
            case UPGRADE:
                positions = getBasicBlocksPos(side);
                states = ArrayUtils.addAll(getBasicBlocks(), getUpgradeBlocks());
                break;
            case CONTROLPANEL:
                positions = new BlockPos[]{new BlockPos(0, 0, 0)};
                states = getControllerBlocks();
                break;
            default:
                throw new IllegalArgumentException("EnumMachineComponentType can't be null");
        }

        for (BlockPos pos:positions) {
            boolean coincidence = false;
            for (BlockState state:states) {
                if (type == TMComponentType.CORE ?
                        world.getBlockState(controllerPos.add(pos)).with(TMReadyProperty.ready, true) == state.with(TMReadyProperty.ready, true) :
                        world.getBlockState(controllerPos.add(pos)) == state) {
                    coincidence = true;
                    break;
                }
            }
            if (!coincidence) {return false;}
        }
        return true;
    }

    /**
     * Checks if all the Time Machine cores are cooled down
     * @param world The world where the Time Machine is built
     * @param controllerPos The position of the Time Machine controller
     * @param side The Time Machine facing
     * @return False if any of the cores is not cooled down
     */
    public boolean isCooledDown(World world, BlockPos controllerPos, Direction side) {
        for(BlockPos pos:getCoreBlocksPos(side)) {
            boolean coincidence = false;
            for(BlockState state:getCoreBlocks()) {
                if(world.getBlockState(controllerPos.add(pos)) == state.with(TMReadyProperty.ready, true)) {
                    coincidence = true;
                    break;
                }
            }
            if(!coincidence)
                return false;
        }
        return true;
    }

    /**
     * Detects if the Time Machine has too many Entities inside
     * @param world The world where the Time Machine is
     * @param controllerPos The position of the Time Machine Controller
     * @param side The facing of the Time Machine
     * @return True if the Time Machine is overloaded
     */
    public boolean isOverloaded(World world, BlockPos controllerPos, Direction side) {
        return getEntitiesInside(world, controllerPos, side).size() > getEntityMaxLoad();
    }

    /**
     * Detects if a given player is inside the Time Machine
     * @param world The world where the Time Machine is
     * @param controllerPos The position of the Time Machine Controller
     * @param side The facing of the Time Machine
     * @param player The wanted player
     * @return True if the player is inside the Time Machine
     */
    public boolean isPlayerInside(World world, BlockPos controllerPos, Direction side, PlayerEntity player) {
        System.out.println(getEntitiesInside(world, controllerPos, side));
        for (Entity entity:getEntitiesInside(world, controllerPos, side)){
            if (entity.getEntityId() == (player.getEntityId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets all the Entities inside the Time Machine
     * @param world The world where the Time Machine is
     * @param controllerPos The position of the Time Machine Controller
     * @param side The facing of the Time Machine
     * @return A list with all the Entities inside the Time Machine
     */
    public final List<Entity> getEntitiesInside(World world, BlockPos controllerPos, Direction side) {
        AxisAlignedBB airSpace = getAirSpace(controllerPos, side);
        return world.getEntitiesWithinAABB(Entity.class, airSpace);
    }

    /**
     * Calculates the zone where Entities must be detected
     * @param controllerPos The position of the Time Machine Controller
     * @param side The facing of the Time Machine
     * @return An AxisAlignedBB of the Time Machine Air Blocks
     */
    public AxisAlignedBB getAirSpace(BlockPos controllerPos, Direction side) {
        // Get the air blocks
        BlockPos relativeAirBlocks[] = applySide(airBlocksPos(), side);
        // First block is the min and max block by default
        BlockPos minPos = relativeAirBlocks[0];
        BlockPos maxPos = relativeAirBlocks[0];
        // Check for the correct min and max block
        for (int block = 1; block < relativeAirBlocks.length; block++) {
            if (relativeAirBlocks[block].getX() < minPos.getX() ||
                relativeAirBlocks[block].getY() < minPos.getY() ||
                relativeAirBlocks[block].getZ() < minPos.getZ()) {
                minPos = relativeAirBlocks[block];
            } else
            if (relativeAirBlocks[block].getX() > maxPos.getX() ||
                relativeAirBlocks[block].getY() > maxPos.getY() ||
                relativeAirBlocks[block].getZ() > maxPos.getZ()) {
                maxPos = relativeAirBlocks[block];
            }
        }
        // Convert the relative positions to real ones
        minPos = minPos.add(controllerPos);
        maxPos = maxPos.add(controllerPos);
        // Return the Air Space
        float offset = 0.3f;
        return new AxisAlignedBB(
                minPos.getX() + offset,
                minPos.getY() + offset,
                minPos.getZ() + offset,
                maxPos.getX() + 1-offset,
                maxPos.getY() + 1-offset,
                maxPos.getZ() + 1-offset
        );
    }

    /**
     * Does the tasks of the ITimeMachineTeleporter when a time travel starts
     * @param entity
     * @param worldIn The source world
     * @param worldOut The target world
     * @param controllerPos The position of the TM Controller
     * @param side The facing of the time machine
     */
    public void teleporterTasks(Entity entity, World worldIn, World worldOut, BlockPos controllerPos, Direction side) {
        IChunk chunk = worldIn.getChunk(controllerPos);
        worldIn.getChunkProvider().getChunk(chunk.getPos().x, chunk.getPos().z, ChunkStatus.FULL, false);
        BlockPos[] posData = getPosData(controllerPos, side);
        BlockState[] blockData = getBlockData(worldOut, posData);
        destroyTM(worldOut, posData);
        buildTM(worldIn, posData, blockData);
        doCooldown(worldIn, controllerPos, side);
    }

    /**
     * Copies the Time Machine blocks positions in the source world
     * @param controllerPos The position of the TM Controller
     * @param side The Time Machine facing
     * @return An array with all the Time Machine blocks positions
     */
    public final BlockPos[] getPosData(BlockPos controllerPos, Direction side) {
        BlockPos[] controllerPosA = new BlockPos[]{new BlockPos(0, 0, 0)};
        BlockPos[] corePos = getCoreBlocksPos(side);
        BlockPos[] basePos = getBasicBlocksPos(side);
        BlockPos[] airPos = getAirBlocksPos(side);
        BlockPos[] posData = (BlockPos[]) ArrayUtils.addAll((BlockPos[]) ArrayUtils.addAll(controllerPosA, corePos), (BlockPos[]) ArrayUtils.addAll(basePos, airPos));
        for (int i = 0; i < posData.length; i++) {
            posData[i] = controllerPos.add(posData[i]);
        }
        return posData;
    }

    /**
     * Copies the Time Machine blocks IBlockStates in the source world
     * @param world The world were the Time Machine is now placed
     * @param posData The positions gathered by the getPosData() method
     * @return An array with all the Time Machine blocks IBlockStates
     */
    public final BlockState[] getBlockData(World world, BlockPos[] posData) {
        BlockState[] blockData = new BlockState[posData.length];
        for (int i = 0; i < blockData.length; i++) {
            blockData[i] = world.getBlockState(posData[i]);
        }
        return blockData;
    }

    /**
     * Destroys the Time Machine in the source world
     * @param world The source world
     * @param posData The positions gathered by the getPosData() method
     */
    public final void destroyTM(World world, BlockPos[] posData) {
        for (int i = 0; i < posData.length; i++) {
            world.setBlockState(posData[i], Blocks.AIR.getDefaultState());
        }
    }

    /**
     * Build the Time Machine in the target world
     * @param world The target world
     * @param posData The positions gathered by the getPosData() method
     * @param blockData The IBlockStates gathered by the getBlockData() method
     */
    public final void buildTM(World world, BlockPos[] posData, BlockState[] blockData) {
        for (int i = 0; i < posData.length; i++) {
            world.setBlockState(posData[i], blockData[i]);
        }
    }

    /**
     * Attaches TMCooldownTileEntity to all Time Machine cores in the Time Machine to start the cooling down phase
     * @param worldIn The world of the new Time Machine
     * @param controllerPos The position of the new Time Machine controller block
     * @param side The facing of the Time Machine
     */
    public final void doCooldown(World worldIn, BlockPos controllerPos, Direction side) {
        for (BlockPos block:getCoreBlocksPos(side)) {
            worldIn.setBlockState(controllerPos.add(block), worldIn.getBlockState(controllerPos.add(block)).with(TMReadyProperty.ready, false));
        }
    }

    /**
     * When higher, more corruption will be caused by the time machine
     * @return The corruption multiplier
     */
    public int getCorruptionMultiplier() {
        return 1;
    }

    @Override
    public String toString() {
        return ModRegistries.timeMachinesRegistry.getKey(this).toString();
    }

    public static TimeMachine fromString(String s) {
        return ModRegistries.timeMachinesRegistry.getValue(new ResourceLocation(s));
    }

    public enum TMComponentType {
        BASIC, CORE, CONTROLPANEL, UPGRADE
    }
}
