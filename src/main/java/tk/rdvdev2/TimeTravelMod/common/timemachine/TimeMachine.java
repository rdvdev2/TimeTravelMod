package tk.rdvdev2.TimeTravelMod.common.timemachine;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.IChunk;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.apache.commons.lang3.ArrayUtils;
import tk.rdvdev2.TimeTravelMod.ModRegistries;
import tk.rdvdev2.TimeTravelMod.ModTriggers;
import tk.rdvdev2.TimeTravelMod.TimeTravelMod;
import tk.rdvdev2.TimeTravelMod.api.timemachine.TimeMachineTemplate;
import tk.rdvdev2.TimeTravelMod.api.timemachine.block.TimeMachineCoreBlock;
import tk.rdvdev2.TimeTravelMod.api.timemachine.block.TimeMachineUpgradeBlock;
import tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.TimeMachineUpgrade;
import tk.rdvdev2.TimeTravelMod.common.block.tileentity.TMCooldownTileEntity;
import tk.rdvdev2.TimeTravelMod.common.timemachine.exception.IncompatibleTimeMachineHooksException;
import tk.rdvdev2.TimeTravelMod.common.util.TimeMachineChecker;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

import static tk.rdvdev2.TimeTravelMod.common.block.properties.TMReadyProperty.TMReadyProperty;

public class TimeMachine extends ForgeRegistryEntry<tk.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine> implements tk.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine {

    private final TimeMachineTemplate template;
    private ArrayList<tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.TimeMachineUpgrade> upgrades;

    public TimeMachine(TimeMachineTemplate template) {
        this.template = template;
    }

    @Override
    public TranslationTextComponent getName() {
        return new TranslationTextComponent("tm."+getRegistryName().getNamespace()+"."+getRegistryName().getPath()+".name");
    }

    @Override
    public TranslationTextComponent getDescription() {
        return new TranslationTextComponent("tm."+getRegistryName().getNamespace()+"."+getRegistryName().getPath()+".description");
    }

    @SuppressWarnings("unchecked")
    @Override
    public final BlockState[] getUpgradeBlocks() {
        TimeMachineUpgradeBlock[] blocks = new TimeMachineUpgradeBlock[0];
        try {
            for (tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.TimeMachineUpgrade upgrade : getCompatibleUpgrades()) {
                HashMap<tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.TimeMachineUpgrade, TimeMachineUpgradeBlock[]> hm = (HashMap<tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.TimeMachineUpgrade, TimeMachineUpgradeBlock[]>) ModRegistries.UPGRADES.getSlaveMap(ModRegistries.UPGRADETOBLOCK, HashMap.class);
                blocks = blocks == null ? hm.get(upgrade) : ArrayUtils.addAll(blocks, hm.get(upgrade));
            }
            BlockState[] states = new BlockState[0];
            for (TimeMachineUpgradeBlock block : blocks) {
                states = states == null ? new BlockState[]{block.getDefaultState()} : ArrayUtils.addAll(states, new BlockState[]{block.getDefaultState()});
            }
            return states;
        } catch (NullPointerException e) {
            return new BlockState[]{};
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public final TimeMachineUpgrade[] getCompatibleUpgrades() {
        if (upgrades == null) {
            upgrades = new ArrayList<>();
            ModRegistries.UPGRADES.forEach(upgrade -> {
                for(tk.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine tm: upgrade.getCompatibleTMs()) {
                    if (tm == this) {
                        upgrades.add(upgrade);
                        break;
                    }
                }
            });
        }
        return upgrades.toArray(new tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.TimeMachineUpgrade[0]);
    }

    @Override
    public List<BlockPos> getCoreBlocksPos(Direction side) {
        return applySide(coreBlocksPos(), side);
    }

    @Override
    public List<BlockPos> getBasicBlocksPos(Direction side) {
        return applySide(basicBlocksPos(), side);
    }

    @Override
    public List<BlockPos> getAirBlocksPos(Direction side) {
        return applySide(airBlocksPos(), side);
    }

    @Override
    public BlockState[] getBlocks() {
        if (getUpgradeBlocks().length != 0) {
            return (BlockState[]) ArrayUtils.addAll(ArrayUtils.addAll((BlockState[]) getControllerBlocks(), (BlockState[]) getCoreBlocks()), ArrayUtils.addAll((BlockState[]) getBasicBlocks(), (BlockState[]) getUpgradeBlocks()));
        } else {
            return (BlockState[]) ArrayUtils.addAll(ArrayUtils.addAll((BlockState[]) getControllerBlocks(), (BlockState[]) getCoreBlocks()), (BlockState[]) getBasicBlocks());
        }
    }

    @Override
    public final tk.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine hook(World world, BlockPos controllerPos, Direction side) throws IncompatibleTimeMachineHooksException {
        TimeMachineHookRunner generated;
        generated = new TimeMachineHookRunner(this, getUpgrades(world, controllerPos, side));
        HashSet<tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.TimeMachineUpgrade> incompatibilities = generated.checkIncompatibilities();
        if (incompatibilities.isEmpty()) {
            return generated;
        } else {
            throw new IncompatibleTimeMachineHooksException(incompatibilities);
        }
    }

    @Override
    public final HashMap<tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.TimeMachineUpgrade, HashSet<BlockPos>> getUpgrades(World world, BlockPos controllerPos, Direction side) {
        HashMap<tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.TimeMachineUpgrade, HashSet<BlockPos>> upgrades = new HashMap<>(0);
        for (BlockPos pos:getBasicBlocksPos(side))
            for (BlockState state:getUpgradeBlocks())
                if (world.getBlockState(controllerPos.add(pos)) == state) {
                    tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.TimeMachineUpgrade upgrade = ((TimeMachineUpgradeBlock) state.getBlock()).getUpgrade();
                    upgrades.putIfAbsent(upgrade, new HashSet<>());
                    upgrades.get(upgrade).add(controllerPos.add(pos));
                    break;
                }
        return upgrades;
    }

    @Override
    public void run(World world, PlayerEntity playerIn, BlockPos controllerPos, Direction side) {
        if (!world.isRemote) {
            TimeMachineChecker.Check error = TimeMachineChecker.check(this, world, playerIn, controllerPos, side);
            if (error == null) {
                if (!triggerTemporalExplosion(world, controllerPos, side)) {
                    if (playerIn instanceof ServerPlayerEntity) {
                        ModTriggers.ACCESS_TIME_MACHINE.trigger((ServerPlayerEntity) playerIn);
                    }
                    TimeTravelMod.PROXY.displayTMGuiScreen(playerIn, this, controllerPos, side, getEntitiesInside(world, controllerPos, side).stream()
                            .filter(entity -> !entity.equals(playerIn))
                            .map(Entity::getUniqueID)
                            .collect(Collectors.toList()).toArray(new UUID[]{}));
                }
            } else {
                if (playerIn instanceof ServerPlayerEntity) {
                    playerIn.sendStatusMessage(error.getClientError(), true);
                }
            }
        }
    }

    @Override
    public boolean triggerTemporalExplosion(World world, BlockPos controllerPos, Direction side) {
        for (BlockPos pos:getCoreBlocksPos(side)) {
            TimeMachineCoreBlock core = (TimeMachineCoreBlock) world.getBlockState(controllerPos.add(pos)).getBlock();
            if (core.randomExplosion(world, controllerPos.add(pos)))
                return true;
        }
        return false;
    }

    @Override
    public boolean isBuilt(World world, BlockPos controllerPos, Direction side) {
        if (isComponentTypeBuilt(TimeMachineComponentType.CORE, world, controllerPos, side) &&
                isComponentTypeBuilt(TimeMachineComponentType.BASIC, world, controllerPos, side)) {
            List<BlockPos> airPos = getAirBlocksPos(side);
            for (BlockPos pos: airPos) {
                if (world.getBlockState(controllerPos.add(pos)) != Blocks.AIR.getDefaultState()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean isCooledDown(World world, BlockPos controllerPos, Direction side) {
        for(BlockPos pos:getCoreBlocksPos(side)) {
            boolean coincidence = false;
            for(BlockState state:getCoreBlocks()) {
                if (world.getBlockState(controllerPos.add(pos)).getBlock() instanceof TimeMachineCoreBlock) {
                    if (world.getBlockState(controllerPos.add(pos)) == state.with(TMReadyProperty, true)) {
                        coincidence = true;
                        break;
                    }
                }
            }
            if(!coincidence)
                return false;
        }
        return true;
    }

    @Override
    public boolean isOverloaded(World world, BlockPos controllerPos, Direction side) {
        return getEntitiesInside(world, controllerPos, side).size() > getEntityMaxLoad();
    }

    @Override
    public boolean isPlayerInside(World world, BlockPos controllerPos, Direction side, PlayerEntity player) {
        for (Entity entity:getEntitiesInside(world, controllerPos, side)){
            if (entity.getEntityId() == (player.getEntityId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Entity> getEntitiesInside(World world, BlockPos controllerPos, Direction side) {
        AxisAlignedBB airSpace = getAirSpace(controllerPos, side);
        return world.getEntitiesWithinAABB(Entity.class, airSpace);
    }

    @Override
    public AxisAlignedBB getAirSpace(BlockPos controllerPos, Direction side) {
        // Get the air blocks
        List<BlockPos> relativeAirBlocks = applySide(airBlocksPos(), side);
        // First block is the min and max block by default
        BlockPos minPos = relativeAirBlocks.get(0);
        BlockPos maxPos = relativeAirBlocks.get(0);
        // Check for the correct min and max block
        for (BlockPos pos: relativeAirBlocks) {
            if (pos.getX() < minPos.getX() ||
                    pos.getY() < minPos.getY() ||
                    pos.getZ() < minPos.getZ()) {
                minPos = pos;
            } else
            if (pos.getX() > maxPos.getX() ||
                    pos.getY() > maxPos.getY() ||
                    pos.getZ() > maxPos.getZ()) {
                maxPos = pos;
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

    @Override
    public void teleporterTasks(@Nullable Entity entity, World worldIn, World worldOut, BlockPos controllerPos, Direction side, boolean shouldBuild) {
        IChunk chunk = worldIn.getChunk(controllerPos);
        worldIn.getChunkProvider().getChunk(chunk.getPos().x, chunk.getPos().z, ChunkStatus.FULL, true);
        if (shouldBuild) {
            List<BlockPos> posData = getPosData(controllerPos, side);
            Map<BlockPos, BlockState> blockData = getBlockData(worldOut, posData);
            destroyTM(worldOut, posData);
            buildTM(worldIn, blockData);
            doCooldown(worldIn, controllerPos, side);
        }
    }

    @Override
    public final void doCooldown(World worldIn, BlockPos controllerPos, Direction side) {
        for (BlockPos relativePos:getCoreBlocksPos(side)) {
            worldIn.setBlockState(controllerPos.add(relativePos), worldIn.getBlockState(controllerPos.add(relativePos)).with(TMReadyProperty, false));
            ((TMCooldownTileEntity)worldIn.getTileEntity(controllerPos.add(relativePos))).setTime(getCooldownTime());
        }
    }

    @Override
    public String toString() {
        return ModRegistries.TIME_MACHINES.getKey(this).toString();
    }

    @Override
    public tk.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine removeHooks() {
        return this;
    }

    // Private utils methods

    private static final List<BlockPos> applySide(List<BlockPos> posList, Direction side) {
        posList = new ArrayList<>(posList);
        if (side == Direction.NORTH) return posList;
        for (int i = 0; i < posList.size(); i++) {
            switch (side) {
                case SOUTH:
                    posList.set(i, posList.get(i).rotate(Rotation.CLOCKWISE_180));
                    break;
                case WEST:
                    posList.set(i, posList.get(i).rotate(Rotation.COUNTERCLOCKWISE_90));
                    break;
                case EAST:
                    posList.set(i, posList.get(i).rotate(Rotation.CLOCKWISE_90));
                    break;
            }
        }
        return posList;
    }

    private final boolean isComponentTypeBuilt(TimeMachineComponentType type, World world, BlockPos controllerPos, Direction side) {
        List<BlockPos> positions;
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
                positions = Collections.singletonList(BlockPos.ZERO);
                states = getControllerBlocks();
                break;
            default:
                throw new IllegalArgumentException("EnumMachineComponentType can't be null");
        }

        for (BlockPos pos:positions) {
            boolean coincidence = false;
            for (BlockState state:states) {
                if (type == TimeMachineComponentType.CORE ?
                        world.getBlockState(controllerPos.add(pos)).getBlock().getDefaultState() == state.getBlock().getDefaultState() :
                        world.getBlockState(controllerPos.add(pos)) == state) {
                    coincidence = true;
                    break;
                }
            }
            if (!coincidence) return false;
        }
        return true;
    }

    private final List<BlockPos> getPosData(BlockPos controllerPos, Direction side) {
        ArrayList<BlockPos> posData = new ArrayList<BlockPos>();
        posData.add(BlockPos.ZERO);
        posData.addAll(getCoreBlocksPos(side));
        posData.addAll(getBasicBlocksPos(side));
        posData.addAll(getAirBlocksPos(side));
        for (int i = 0; i < posData.size(); i++) {
            posData.set(i, controllerPos.add(posData.get(i)));
        }
        return posData;
    }

    private final Map<BlockPos, BlockState> getBlockData(World world, List<BlockPos> posData) {
        Map<BlockPos, BlockState> blockData = new HashMap<>(posData.size());
        for (BlockPos pos: posData) {
            blockData.put(pos, world.getBlockState(pos));
        }
        return blockData;
    }

    private final void destroyTM(World world, List<BlockPos> posData) {
        for (BlockPos pos: posData) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
        }
    }

    private final void buildTM(World world, Map<BlockPos, BlockState> blockData) {
        for (BlockPos pos: blockData.keySet()) {
            world.setBlockState(pos, blockData.get(pos));
        }
    }

    // Template delegates

    @Override
    public int getCooldownTime() {
        return template.getCooldownTime();
    }

    @Override
    public int getTier() {
        return template.getTier();
    }

    @Override
    public List<BlockPos> coreBlocksPos() {
        return template.coreBlocksPos();
    }

    @Override
    public List<BlockPos> basicBlocksPos() {
        return template.basicBlocksPos();
    }

    @Override
    public List<BlockPos> airBlocksPos() {
        return template.airBlocksPos();
    }

    @Override
    public BlockState[] getControllerBlocks() {
        return template.getControllerBlocks();
    }

    @Override
    public BlockState[] getCoreBlocks() {
        return template.getCoreBlocks();
    }

    @Override
    public BlockState[] getBasicBlocks() {
        return template.getBasicBlocks();
    }

    @Override
    public int getEntityMaxLoad() {
        return template.getEntityMaxLoad();
    }

    @Override
    public int getCorruptionMultiplier() {
        return template.getCorruptionMultiplier();
    }
}