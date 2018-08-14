package com.rdvdev2.TimeTravelMod.common.block;

import com.rdvdev2.TimeTravelMod.util.TimeMachineTier1;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockTimeMachineControlPanel extends BlockTimeMachineBasicBlock{

    public BlockTimeMachineControlPanel() {
        super();
    }

    public Block setNames() {
        String name = "timemachinecontrolpanel";
        setUnlocalizedName(name);
        setRegistryName(name);
        return this;
    }

    @Override
    public boolean onBlockActivated(World worldIn,
                                    BlockPos pos,
                                    IBlockState state,
                                    EntityPlayer playerIn,
                                    EnumHand hand,
                                    EnumFacing side,
                                    float hitX,
                                    float hitY,
                                    float hitZ) {
        new TimeMachineTier1().run(worldIn, playerIn, pos, side);
        return true;
    }
}
