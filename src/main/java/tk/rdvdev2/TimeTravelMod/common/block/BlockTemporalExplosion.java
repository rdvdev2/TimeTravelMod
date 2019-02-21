package tk.rdvdev2.TimeTravelMod.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import tk.rdvdev2.TimeTravelMod.ModBlocks;

import java.util.Random;

public class BlockTemporalExplosion extends Block {
    private String name = "temporalexplosion";
    public static DamageSource damage = new DamageSource("temporalerror").setDamageIsAbsolute();

    public BlockTemporalExplosion() {
        super(Properties.create(Material.PORTAL)
                .sound(SoundType.METAL)
                .hardnessAndResistance(-1)
                .lightValue(0)
                .variableOpacity()
        );
        setRegistryName(name);
    }

    @Override
    public int getItemsToDropCount(IBlockState p_196251_1_, int p_196251_2_, World p_196251_3_, BlockPos p_196251_4_, Random p_196251_5_) {
        return 0;
    }

    @Override
    public boolean canEntityDestroy(IBlockState state, IBlockReader world, BlockPos pos, Entity entity) {
        if (entity instanceof EntityPlayer) {
            if (((EntityPlayer)entity).isCreative()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onEntityCollision(IBlockState p_196262_1_, World p_196262_2_, BlockPos p_196262_3_, Entity entity) {
        entity.attackEntityFrom(damage, 1000000);
    }

    @Override
    public VoxelShape getCollisionShape(IBlockState p_196268_1_, IBlockReader p_196268_2_, BlockPos p_196268_3_) {
        return VoxelShapes.create(VoxelShapes.fullCube().getBoundingBox().shrink(0.1));
    }

    @Override
    public boolean onBlockActivated(IBlockState state, World worldIn, BlockPos pos, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (ItemStack.areItemStacksEqual(playerIn.inventory.getCurrentItem(), new ItemStack(ModBlocks.reinforcedHeavyBlock, playerIn.inventory.getCurrentItem().getCount()))) {
            if(!playerIn.isCreative()) playerIn.inventory.getCurrentItem().grow(-1);
            worldIn.setBlockState(pos, ModBlocks.reinforcedHeavyBlock.getDefaultState());
            worldIn.playSound(null, pos, SoundEvents.BLOCK_METAL_PLACE, SoundCategory.BLOCKS, 3.0F, 1);
            return true;
        }
        return false;
    }
}
