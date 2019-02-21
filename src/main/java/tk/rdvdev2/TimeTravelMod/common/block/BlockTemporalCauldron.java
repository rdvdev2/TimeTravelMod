package tk.rdvdev2.TimeTravelMod.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.IProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import tk.rdvdev2.TimeTravelMod.ModBlocks;
import tk.rdvdev2.TimeTravelMod.ModItems;
import tk.rdvdev2.TimeTravelMod.common.block.tileentity.TileEntityTemporalCauldron;

import javax.annotation.Nullable;

public class BlockTemporalCauldron extends Block {
    private String name = "temporalcauldron";

    public static final IntegerProperty LEVEL = IntegerProperty.create("level", 0, 3);
    protected static final VoxelShape INSIDE;
    protected static final VoxelShape WALLS;

    public BlockTemporalCauldron() {
        super(Properties.create(Material.IRON, MaterialColor.STONE));
        this.setDefaultState(this.getStateContainer().getBaseState().with(LEVEL, Integer.valueOf(0)));
        this.setRegistryName(name);
    }

    @Override
    public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        super.onBlockActivated(state, worldIn, pos, playerIn, hand, facing, hitX, hitY, hitZ);
        TileEntityTemporalCauldron te = (TileEntityTemporalCauldron) worldIn.getTileEntity(pos);
        ItemStack playerItemStack = playerIn.getHeldItem(hand);
        if (te == null) {
            return false;
        }
        if (!playerItemStack.isEmpty() && !playerItemStack.isItemEqual(new ItemStack(ModItems.timeCrystal)) && playerItemStack.isDamageable() && !te.containsItem()) {
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
                worldIn.spawnEntity(new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), te.removeItem()));
            }
        } else return false; return true;
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player) {
        super.onBlockHarvested(worldIn, pos, state, player);
        if (!worldIn.isRemote) {
            worldIn.spawnEntity(new EntityItem(worldIn, pos.getX(), pos.getY(), pos.getZ(), ((TileEntityTemporalCauldron)(worldIn.getTileEntity(pos))).removeItem()));
        }
    }

    public VoxelShape getShape(IBlockState p_196244_1_, IBlockReader p_196244_2_, BlockPos p_196244_3_) {
        return WALLS;
    }

    public boolean isSolid(IBlockState p_200124_1_) {
        return false;
    }

    public VoxelShape getRaytraceShape(IBlockState p_199600_1_, IBlockReader p_199600_2_, BlockPos p_199600_3_) {
        return INSIDE;
    }

    public boolean isFullCube(IBlockState p_149686_1_) {
        return false;
    }

    public void setTimeFluidLevel(World worldIn, BlockPos pos, IBlockState state, int level)
    {
        worldIn.setBlockState(pos, state.with(LEVEL, Integer.valueOf(MathHelper.clamp(level, 0, 3))), 2);
    }

    protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> p_206840_1_) {
        p_206840_1_.add(new IProperty[]{LEVEL});
    }

    public BlockFaceShape getBlockFaceShape(IBlockReader p_193383_1_, IBlockState p_193383_2_, BlockPos p_193383_3_, EnumFacing p_193383_4_) {
        if (p_193383_4_ == EnumFacing.UP) {
            return BlockFaceShape.BOWL;
        } else {
            return p_193383_4_ == EnumFacing.DOWN ? BlockFaceShape.UNDEFINED : BlockFaceShape.SOLID;
        }
    }

    public boolean allowsMovement(IBlockState p_196266_1_, IBlockReader p_196266_2_, BlockPos p_196266_3_, PathType p_196266_4_) {
        return false;
    }

    @Override
    public IItemProvider getItemDropped(IBlockState p_199769_1_, World p_199769_2_, BlockPos p_199769_3_, int p_199769_4_) {
        return Item.getItemFromBlock(ModBlocks.temporalCauldron);
    }

    @Override
    public ItemStack getItem(IBlockReader p_185473_1_, BlockPos p_185473_2_, IBlockState p_185473_3_) {
        return new ItemStack(Item.getItemFromBlock(ModBlocks.temporalCauldron));
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(IBlockState state, IBlockReader world) {
        return new TileEntityTemporalCauldron();
    }

    static {
        INSIDE = Block.makeCuboidShape(2.0D, 4.0D, 2.0D, 14.0D, 16.0D, 14.0D);
        WALLS = VoxelShapes.combineAndSimplify(VoxelShapes.fullCube(), INSIDE, IBooleanFunction.ONLY_FIRST);
    }
}
