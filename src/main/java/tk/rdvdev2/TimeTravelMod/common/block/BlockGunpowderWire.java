package tk.rdvdev2.TimeTravelMod.common.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.TNTBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Items;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.RedstoneSide;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import tk.rdvdev2.TimeTravelMod.TimeTravelMod;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class BlockGunpowderWire extends Block { // TODO: Highly test to ensure nothing is broken (Will break anyway)

    protected static final VoxelShape[] SHAPES = new VoxelShape[]{Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 1.0D, 13.0D), Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 13.0D, 1.0D, 16.0D), Block.makeCuboidShape(0.0D, 0.0D, 3.0D, 13.0D, 1.0D, 13.0D), Block.makeCuboidShape(0.0D, 0.0D, 3.0D, 13.0D, 1.0D, 16.0D), Block.makeCuboidShape(3.0D, 0.0D, 0.0D, 13.0D, 1.0D, 13.0D), Block.makeCuboidShape(3.0D, 0.0D, 0.0D, 13.0D, 1.0D, 16.0D), Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 13.0D, 1.0D, 13.0D), Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 13.0D, 1.0D, 16.0D), Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 16.0D, 1.0D, 13.0D), Block.makeCuboidShape(3.0D, 0.0D, 3.0D, 16.0D, 1.0D, 16.0D), Block.makeCuboidShape(0.0D, 0.0D, 3.0D, 16.0D, 1.0D, 13.0D), Block.makeCuboidShape(0.0D, 0.0D, 3.0D, 16.0D, 1.0D, 16.0D), Block.makeCuboidShape(3.0D, 0.0D, 0.0D, 16.0D, 1.0D, 13.0D), Block.makeCuboidShape(3.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D), Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 13.0D), Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D)};
    public static final EnumProperty<RedstoneSide> NORTH = BlockStateProperties.REDSTONE_NORTH;
    public static final EnumProperty<RedstoneSide> EAST = BlockStateProperties.REDSTONE_EAST;
    public static final EnumProperty<RedstoneSide> SOUTH = BlockStateProperties.REDSTONE_SOUTH;
    public static final EnumProperty<RedstoneSide> WEST = BlockStateProperties.REDSTONE_WEST;
    public static final BooleanProperty BURNED = BooleanProperty.create("burned");
    public static final Map<Direction, EnumProperty<RedstoneSide>> FACING_PROPERTY_MAP = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, NORTH, Direction.EAST, EAST, Direction.SOUTH, SOUTH, Direction.WEST, WEST));
    private final Set<BlockPos> blocksNeedingUpdate = Sets.newHashSet();

    public BlockGunpowderWire() {
        super(Properties.create(Material.MISCELLANEOUS).doesNotBlockMovement().hardnessAndResistance(0));
        this.setDefaultState(this.getStateContainer().getBaseState().with(NORTH, RedstoneSide.NONE).with(EAST, RedstoneSide.NONE).with(SOUTH, RedstoneSide.NONE).with(WEST, RedstoneSide.NONE).with(BURNED, false));
        this.setRegistryName(TimeTravelMod.MODID, "gunpowderwire");
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPES[getAABBIndex(state)];
    }

    private static int getAABBIndex(BlockState state) {
        int ret = 0;
        boolean north = state.get(NORTH) != RedstoneSide.NONE;
        boolean east = state.get(EAST) != RedstoneSide.NONE;
        boolean south = state.get(SOUTH) != RedstoneSide.NONE;
        boolean west = state.get(WEST) != RedstoneSide.NONE;
        if (north || south && !north && !east && !west) {
            ret |= 1 << Direction.NORTH.getHorizontalIndex();
        }

        if (east || west && !north && !east && !south) {
            ret |= 1 << Direction.EAST.getHorizontalIndex();
        }

        if (south || north && !east && !south && !west) {
            ret |= 1 << Direction.SOUTH.getHorizontalIndex();
        }

        if (west || east && !north && !south && !west) {
            ret |= 1 << Direction.WEST.getHorizontalIndex();
        }

        return ret;
    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        IBlockReader iblockreader = context.getWorld();
        BlockPos blockpos = context.getPos();
        return this.getDefaultState().with(WEST, this.getSide(iblockreader, blockpos, Direction.WEST)).with(EAST, this.getSide(iblockreader, blockpos, Direction.EAST)).with(NORTH, this.getSide(iblockreader, blockpos, Direction.NORTH)).with(SOUTH, this.getSide(iblockreader, blockpos, Direction.SOUTH));
    }

    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (facing == Direction.DOWN || stateIn.get(BURNED)) {
            return stateIn;
        } else {
            return facing == Direction.UP ? stateIn.with(WEST, this.getSide(worldIn, currentPos, Direction.WEST)).with(EAST, this.getSide(worldIn, currentPos, Direction.EAST)).with(NORTH, this.getSide(worldIn, currentPos, Direction.NORTH)).with(SOUTH, this.getSide(worldIn, currentPos, Direction.SOUTH)) : stateIn.with(FACING_PROPERTY_MAP.get(facing), this.getSide(worldIn, currentPos, facing));
        }
    }

    public void updateDiagonalNeighbors(BlockState state, IWorld worldIn, BlockPos pos, int flags) {
        try (BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain()) {
            for(Direction enumfacing : Direction.Plane.HORIZONTAL) {
                RedstoneSide redstoneside = state.get(FACING_PROPERTY_MAP.get(enumfacing));
                if (redstoneside != RedstoneSide.NONE && worldIn.getBlockState(blockpos$pooledmutableblockpos.setPos(pos).move(enumfacing)).getBlock() != this) {
                    blockpos$pooledmutableblockpos.move(Direction.DOWN);
                    BlockState iblockstate = worldIn.getBlockState(blockpos$pooledmutableblockpos);
                    if (iblockstate.getBlock() != Blocks.OBSERVER) {
                        BlockPos blockpos = blockpos$pooledmutableblockpos.offset(enumfacing.getOpposite());
                        BlockState iblockstate1 = iblockstate.updatePostPlacement(enumfacing.getOpposite(), worldIn.getBlockState(blockpos), worldIn, blockpos$pooledmutableblockpos, blockpos);
                        replaceBlock(iblockstate, iblockstate1, worldIn, blockpos$pooledmutableblockpos, flags);
                    }

                    blockpos$pooledmutableblockpos.setPos(pos).move(enumfacing).move(Direction.UP);
                    BlockState iblockstate3 = worldIn.getBlockState(blockpos$pooledmutableblockpos);
                    if (iblockstate3.getBlock() != Blocks.OBSERVER) {
                        BlockPos blockpos1 = blockpos$pooledmutableblockpos.offset(enumfacing.getOpposite());
                        BlockState iblockstate2 = iblockstate3.updatePostPlacement(enumfacing.getOpposite(), worldIn.getBlockState(blockpos1), worldIn, blockpos$pooledmutableblockpos, blockpos1);
                        replaceBlock(iblockstate3, iblockstate2, worldIn, blockpos$pooledmutableblockpos, flags);
                    }
                }
            }
        }
    }

    private RedstoneSide getSide(IBlockReader worldIn, BlockPos pos, Direction face) {
        BlockPos blockpos = pos.offset(face);
        BlockState blockstate = worldIn.getBlockState(blockpos);
        BlockState offsetState = worldIn.getBlockState(pos.offset(face));
        BlockState stateUp = worldIn.getBlockState(pos.up());
        if (!stateUp.func_215686_e(worldIn, pos.up())) {
            boolean flag = Block.func_220056_d(blockstate, worldIn, blockpos, Direction.UP) || blockstate.getBlock() == Blocks.HOPPER;
            if (flag && canConnectTo(worldIn.getBlockState(blockpos.up()))) {
                if (isOpaque(blockstate.getCollisionShape(worldIn, blockpos))) {
                    return RedstoneSide.UP;
                }

                return RedstoneSide.SIDE;
            }
        }

        return !canConnectTo(blockstate) && (blockstate.func_215686_e(worldIn, blockpos) || !canConnectTo(worldIn.getBlockState(blockpos.down()))) ? RedstoneSide.NONE : RedstoneSide.SIDE;
    }

    /*@Override
    public boolean isFullCube(BlockState state) {
        return false;
    }*/

    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos blockpos) {
        BlockState blockstate = worldIn.getBlockState(blockpos.down());
        return Block.func_220056_d(blockstate, worldIn, blockpos, Direction.UP) || blockstate.getBlock() == Blocks.HOPPER;
    }

    private BlockState updateSurroundingWires(World worldIn, BlockPos pos, BlockState state) {
        List<BlockPos> list = Lists.newArrayList(this.blocksNeedingUpdate);
        this.blocksNeedingUpdate.clear();

        for(BlockPos blockpos : list) {
            worldIn.notifyNeighborsOfStateChange(blockpos, this);
        }

        return state;
    }

    private void notifyWireNeighborsOfStateChange(World worldIn, BlockPos pos) {
        if (worldIn.getBlockState(pos).getBlock() == this) {
            worldIn.notifyNeighborsOfStateChange(pos, this);

            for(Direction enumfacing : Direction.values()) {
                worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this);
            }

        }
    }

    @Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean mistery) {
        if (oldState.getBlock() != state.getBlock() && !worldIn.isRemote) {
            this.updateSurroundingWires(worldIn, pos, state);

            for(Direction facing : Direction.Plane.VERTICAL) {
                worldIn.notifyNeighborsOfStateChange(pos.offset(facing), this);
            }

            for(Direction facing : Direction.Plane.HORIZONTAL) {
                this.notifyWireNeighborsOfStateChange(worldIn, pos.offset(facing));
            }

            for(Direction facing : Direction.Plane.HORIZONTAL) {
                BlockPos blockpos = pos.offset(facing);
                if (worldIn.getBlockState(blockpos).func_215686_e(worldIn, blockpos)) {
                    this.notifyWireNeighborsOfStateChange(worldIn, blockpos.up());
                } else {
                    this.notifyWireNeighborsOfStateChange(worldIn, blockpos.down());
                }
            }

        }
    }

    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!isMoving && state.getBlock() != newState.getBlock()) {
            super.onReplaced(state, worldIn, pos, newState, isMoving);
            if (!worldIn.isRemote) {
                for(Direction facing : Direction.values()) {
                    worldIn.notifyNeighborsOfStateChange(pos.offset(facing), this);
                }

                this.updateSurroundingWires(worldIn, pos, state);

                for(Direction facing : Direction.Plane.HORIZONTAL) {
                    this.notifyWireNeighborsOfStateChange(worldIn, pos.offset(facing));
                }

                for(Direction facing : Direction.Plane.HORIZONTAL) {
                    BlockPos blockpos = pos.offset(facing);
                    if (worldIn.getBlockState(blockpos).func_215686_e(worldIn, blockpos)) {
                        this.notifyWireNeighborsOfStateChange(worldIn, blockpos.up());
                    } else {
                        this.notifyWireNeighborsOfStateChange(worldIn, blockpos.down());
                    }
                }

            }
        }
    }

    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean misteryous) {
        if (!worldIn.isRemote) {
            if (state.isValidPosition(worldIn, pos)) {
                this.updateSurroundingWires(worldIn, pos, state);
            } else {
                spawnDrops(state, worldIn, pos);
                worldIn.removeBlock(pos, false);
            }
        }
    }

    protected boolean canConnectTo(BlockState state) {
        return state.getBlock() == this || state.getBlock() == Blocks.TNT;
    }

    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        switch(rot) {
            case CLOCKWISE_180:
                return state.with(NORTH, state.get(SOUTH)).with(EAST, state.get(WEST)).with(SOUTH, state.get(NORTH)).with(WEST, state.get(EAST));
            case COUNTERCLOCKWISE_90:
                return state.with(NORTH, state.get(EAST)).with(EAST, state.get(SOUTH)).with(SOUTH, state.get(WEST)).with(WEST, state.get(NORTH));
            case CLOCKWISE_90:
                return state.with(NORTH, state.get(WEST)).with(EAST, state.get(NORTH)).with(SOUTH, state.get(EAST)).with(WEST, state.get(SOUTH));
            default:
                return state;
        }
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        switch(mirrorIn) {
            case LEFT_RIGHT:
                return state.with(NORTH, state.get(SOUTH)).with(SOUTH, state.get(NORTH));
            case FRONT_BACK:
                return state.with(EAST, state.get(WEST)).with(WEST, state.get(EAST));
            default:
                return super.mirror(state, mirrorIn);
        }
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST, BURNED);
    }

    /*public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }*/

    @OnlyIn(Dist.CLIENT)
    public static int colorMultiplier(boolean burned) {
        float red, blue, green;

        if (!burned) {
            red = 0.5f;
            blue = 0.5f;
            green = 0.5f;
        } else {
            red = 0;
            blue = 0;
            green = 0;
        }

        int i = MathHelper.clamp((int)(red * 255.0F), 0, 255);
        int j = MathHelper.clamp((int)(blue * 255.0F), 0, 255);
        int k = MathHelper.clamp((int)(green * 255.0F), 0, 255);
        return -16777216 | i << 16 | j << 8 | k;
    }

    /*@Override
    public int getItemsToDropCount(BlockState state, int fortune, World worldIn, BlockPos pos, Random random) {
        if (!state.get(BURNED)) return 1;
        else return 0;
    }*/


    public void setBurned(BlockPos pos, World world) {
        BlockState state = world.getBlockState(pos);
        world.setBlockState(pos, state.with(BURNED, true));
        if (world.getBlockState(pos.down()) == Blocks.TNT.getDefaultState()) {
            world.setBlockState(pos.down(), Blocks.AIR.getDefaultState());
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
            ((TNTBlock)Blocks.TNT).explode(world, pos.down());
            return;
        }
        for (Direction facing: Direction.Plane.HORIZONTAL) {
            BlockPos neighboorPos = pos;
            switch (state.get(FACING_PROPERTY_MAP.get(facing))) {
                case UP:
                    neighboorPos = neighboorPos.up();
                case SIDE:
                    neighboorPos = neighboorPos.offset(facing);
                    if (!canConnectTo(world.getBlockState(neighboorPos).getBlockState()))
                        neighboorPos = neighboorPos.down();
                    if (!canConnectTo(world.getBlockState(neighboorPos).getBlockState()))
                        break;
                    if (world.getBlockState(neighboorPos).getBlock() == this && !world.getBlockState(neighboorPos).get(BURNED))
                        setBurned(neighboorPos, world);
                    else if (world.getBlockState(neighboorPos).getBlock() == Blocks.TNT) {
                        world.setBlockState(neighboorPos, Blocks.AIR.getDefaultState());
                        ((TNTBlock)Blocks.TNT).explode(world, neighboorPos);
                    }
            }
        }
    }

    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult blockRayTraceResult) {
        if (player.getHeldItem(hand).getItem() == Items.FLINT_AND_STEEL && !state.get(BURNED)) {
            if (player instanceof ServerPlayerEntity)
            player.getHeldItem(hand).attemptDamageItem(1, worldIn.rand, (ServerPlayerEntity) player);
            setBurned(pos, worldIn);
            return true;
        } else return false;
    }
}