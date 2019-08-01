package tk.rdvdev2.TimeTravelMod.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.IProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import tk.rdvdev2.TimeTravelMod.ModBlocks;
import tk.rdvdev2.TimeTravelMod.ModItems;
import tk.rdvdev2.TimeTravelMod.ModTriggers;
import tk.rdvdev2.TimeTravelMod.common.block.tileentity.TemporalCauldronTileEntity;

import javax.annotation.Nullable;

public class TemporalCauldronBlock extends Block {
    private String name = "temporalcauldron";

    public static final IntegerProperty LEVEL = IntegerProperty.create("level", 0, 3);
    protected static final VoxelShape INSIDE;
    protected static final VoxelShape WALLS;

    public TemporalCauldronBlock() {
        super(Properties.create(Material.IRON, MaterialColor.STONE).harvestTool(ToolType.PICKAXE).harvestLevel(1).hardnessAndResistance(2.0F));
        this.setDefaultState(this.getStateContainer().getBaseState().with(LEVEL, Integer.valueOf(0)));
        this.setRegistryName(name);
    }

    @Override
    public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity playerIn, Hand hand, BlockRayTraceResult blockRayTraceResult) {
        super.onBlockActivated(state, worldIn, pos, playerIn, hand, blockRayTraceResult);
        TemporalCauldronTileEntity te = (TemporalCauldronTileEntity) worldIn.getTileEntity(pos);
        ItemStack playerItemStack = playerIn.getHeldItem(hand);
        if (te == null) {
            return false;
        }
        if (!playerItemStack.isEmpty() && !playerItemStack.isItemEqual(new ItemStack(ModItems.timeCrystal)) && playerItemStack.isDamaged() && !te.containsItem()) {
            if (!worldIn.isRemote) {
                ItemStack copy = playerItemStack.copy();
                playerItemStack.grow(-1);
                playerIn.setHeldItem(hand, playerItemStack);
                te.putItem(copy);
            }
        } else if (playerItemStack.isItemEqual(new ItemStack(ModItems.timeCrystal)) && !te.containsCrystal()) {
            if(!worldIn.isRemote) {
                if (!playerIn.isCreative())
                    playerIn.setHeldItem(hand, new ItemStack(playerItemStack.getItem(), playerItemStack.getCount() - 1));
                te.putCrystal(new ItemStack(ModItems.timeCrystal, 1));
            }
        } else if (te.containsItem()) {
            if(!worldIn.isRemote) {
                ItemStack item = te.removeItem();
                if (!item.isDamaged() && playerIn instanceof ServerPlayerEntity) ModTriggers.BETTER_THAN_MENDING.trigger((ServerPlayerEntity) playerIn);
                worldIn.addEntity(new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), item));
            }
        } else return false; return true;
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        super.onBlockHarvested(worldIn, pos, state, player);
        if (!worldIn.isRemote) {
            ItemStack item = ((TemporalCauldronTileEntity)(worldIn.getTileEntity(pos))).removeItem();
            if (!item.isDamaged() && player instanceof ServerPlayerEntity) ModTriggers.BETTER_THAN_MENDING.trigger((ServerPlayerEntity) player);
            worldIn.addEntity(new ItemEntity(worldIn, pos.getX(), pos.getY(), pos.getZ(), item));
        }
    }

    public VoxelShape getShape(BlockState p_196244_1_, IBlockReader p_196244_2_, BlockPos p_196244_3_, ISelectionContext context) {
        return WALLS;
    }

    public boolean isSolid(BlockState p_200124_1_) {
        return false;
    }

    public VoxelShape getRaytraceShape(BlockState p_199600_1_, IBlockReader p_199600_2_, BlockPos p_199600_3_) {
        return INSIDE;
    }

    public void setTimeFluidLevel(World worldIn, BlockPos pos, BlockState state, int level)
    {
        worldIn.setBlockState(pos, state.with(LEVEL, Integer.valueOf(MathHelper.clamp(level, 0, 3))), 2);
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> p_206840_1_) {
        p_206840_1_.add(new IProperty[]{LEVEL});
    }

    public boolean allowsMovement(BlockState p_196266_1_, IBlockReader p_196266_2_, BlockPos p_196266_3_, PathType p_196266_4_) {
        return false;
    }

    @Override
    public ItemStack getItem(IBlockReader p_185473_1_, BlockPos p_185473_2_, BlockState p_185473_3_) {
        return new ItemStack(Item.getItemFromBlock(ModBlocks.temporalCauldron));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TemporalCauldronTileEntity();
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            super.onReplaced(state, worldIn, pos, newState, isMoving);
            worldIn.removeTileEntity(pos);
        }
    }

    static {
        INSIDE = Block.makeCuboidShape(2.0D, 4.0D, 2.0D, 14.0D, 16.0D, 14.0D);
        WALLS = VoxelShapes.combineAndSimplify(VoxelShapes.fullCube(), INSIDE, IBooleanFunction.AND);
    }
}
