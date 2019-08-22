package tk.rdvdev2.TimeTravelMod.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ToolType;
import tk.rdvdev2.TimeTravelMod.ModRegistries;
import tk.rdvdev2.TimeTravelMod.api.dimension.TimeLine;
import tk.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;
import tk.rdvdev2.TimeTravelMod.api.timemachine.block.TimeMachineControlPanelBlock;
import tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.IncompatibleTimeMachineHooksException;
import tk.rdvdev2.TimeTravelMod.common.block.tileentity.TimeMachineRecallerTileEntity;

import javax.annotation.Nullable;
import java.util.List;

import static tk.rdvdev2.TimeTravelMod.TimeTravelMod.MODID;
import static tk.rdvdev2.TimeTravelMod.common.networking.DimensionTpPKT.Handler.applyCorruption;
import static tk.rdvdev2.TimeTravelMod.common.networking.DimensionTpPKT.Handler.changeEntityDim;

public class TimeMachineRecallerBlock extends Block {

    public static final BooleanProperty CONFIGURED = BooleanProperty.create("configured");
    public static final BooleanProperty TRIGGERED = BooleanProperty.create("triggered");

    public TimeMachineRecallerBlock() {
        super(Properties.create(Material.IRON)
                .sound(SoundType.METAL)
                .hardnessAndResistance(3f)
                .lightValue(0 / 16)
                .variableOpacity()
                .harvestTool(ToolType.PICKAXE)
                .harvestLevel(2)
        );
        setDefaultState(getStateContainer().getBaseState().with(CONFIGURED, false).with(TRIGGERED, false));
        setRegistryName(MODID, "timemachinerecaller");
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(CONFIGURED).add(TRIGGERED);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return state.get(CONFIGURED).booleanValue();
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        if (!hasTileEntity(state)) return null;
        return new TimeMachineRecallerTileEntity();
    }

    @Override
    public void onReplaced(BlockState oldState, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        if (hasTileEntity(oldState) && (oldState == newState || newState.getBlock() != this)) {
            world.removeTileEntity(pos);
        }
    }

    @Override
    public boolean canConnectRedstone(BlockState state, IBlockReader world, BlockPos pos, @Nullable Direction side) {
        return true;
    }



    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block neighbourBlock, BlockPos neighbourPos, boolean bool) {
        super.neighborChanged(state, world, pos, neighbourBlock, neighbourPos, bool);
        if (!world.isRemote) {
            if (world.isBlockPowered(pos)) {
                if (!state.get(TRIGGERED).booleanValue()) {
                    world.setBlockState(pos, state.with(TRIGGERED, true));
                    if (state.get(CONFIGURED).booleanValue()) {
                        TileEntity tile = world.getTileEntity(pos);
                        if (world instanceof ServerWorld && tile instanceof TimeMachineRecallerTileEntity) {
                            BlockPos controllerPos = ((TimeMachineRecallerTileEntity) tile).getControllerPos();
                            Direction side = ((TimeMachineRecallerTileEntity) tile).getSide();
                            DimensionType searchDim = ((TimeMachineRecallerTileEntity) tile).getDest();
                            boolean ret = searchRecall((ServerWorld) world, world.getServer().getWorld(searchDim), controllerPos, side, pos);
                            world.setBlockState(pos, state.with(CONFIGURED, ret).with(TRIGGERED, true));
                        }
                    }
                }
            } else {
                world.setBlockState(pos, state.with(TRIGGERED, false));
            }
        }
    }

    private boolean searchRecall(ServerWorld origin, ServerWorld searchWorld, BlockPos controllerPos, Direction side, BlockPos recallerPos) {
        DimensionManager.keepLoaded(searchWorld.getDimension().getType());
        Block controllerBlock = searchWorld.getBlockState(controllerPos).getBlock();
        if (controllerBlock instanceof TimeMachineControlPanelBlock) {
            TimeMachine tm = ((TimeMachineControlPanelBlock) controllerBlock).getTimeMachine();
            searchWorld.forceChunk(controllerPos.getX() >> 4, controllerPos.getZ() >> 4, true);
            boolean ret = tryRecall(tm, searchWorld, origin, controllerPos, side);
            searchWorld.forceChunk(controllerPos.getX() >> 4, controllerPos.getZ() >> 4, false);
            DimensionManager.keepLoaded(searchWorld.getDimension().getType(), false);
            return !ret;
        } else {
            TileEntity tile = searchWorld.getTileEntity(recallerPos);
            if (tile instanceof TimeMachineRecallerTileEntity) {
                BlockPos _controllerPos = ((TimeMachineRecallerTileEntity) tile).getControllerPos();
                Direction _side = ((TimeMachineRecallerTileEntity) tile).getSide();
                DimensionType _searchDim = ((TimeMachineRecallerTileEntity) tile).getDest();
                if (controllerPos.equals(_controllerPos) && side.equals(_side)) {
                    DimensionManager.keepLoaded(searchWorld.getDimension().getType(), false);
                    return searchRecall(origin, origin.getServer().getWorld(_searchDim), controllerPos, side, recallerPos);
                } else {
                    DimensionManager.keepLoaded(searchWorld.getDimension().getType(), false);
                    return false;
                }
            } else {
                DimensionManager.keepLoaded(searchWorld.getDimension().getType(), false);
                return false;
            }
        }
    }

    private boolean tryRecall(TimeMachine tm, ServerWorld foundWorld, ServerWorld recallWorld, BlockPos controllerPos, Direction side) {
        DimensionType foundDim = foundWorld.getDimension().getType();
        TimeLine tl = null;
        for (TimeLine _tl : ModRegistries.TIME_LINES.getValues()) {
            if (_tl.getDimension() == foundDim) {
                tl = _tl;
                break;
            }
        }
        if (tl == null) return false;
        try {
            tm = tm.hook(foundWorld, controllerPos, side);
        } catch (IncompatibleTimeMachineHooksException e) {
            return false;
        }
        List<Entity> entities = tm.getEntitiesInside(foundWorld, controllerPos, side);
        if (foundWorld.isBlockLoaded(controllerPos) &&
            tm.isBuilt(foundWorld, controllerPos, side) &&
            tm.isCooledDown(foundWorld, controllerPos, side) &&
            !tm.isOverloaded(foundWorld, controllerPos, side)) {
                if (tm.getTier() >= tl.getMinTier()) {
                    applyCorruption(tm, foundDim, recallWorld.getDimension().getType(), foundWorld.getServer());
                    tm.teleporterTasks(null, recallWorld, foundWorld, controllerPos, side, true);
                    final TimeMachine finalTm = tm;
                    entities.forEach(entity -> changeEntityDim(entity, controllerPos, recallWorld.getDimension().getType(), finalTm, side));
                    return true;
                } else {
                    return false;
                }
        } else {
            return false;
        }
    }
}
