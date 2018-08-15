package com.rdvdev2.TimeTravelMod.util;

import com.rdvdev2.TimeTravelMod.ModRegistries;
import com.rdvdev2.TimeTravelMod.common.event.EventSetTimeMachine;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BlockTimeMachineComponent extends Block {

    private EnumTimeMachineComponentType type;
    private TimeMachine timeMachine;

    public BlockTimeMachineComponent(Material material, EnumTimeMachineComponentType type) {
        super(material);
        this.type = type;
    }

    public void setTimeMachine(EventSetTimeMachine event) {
        this.timeMachine = event.getTimeMachine(this.getDefaultState());
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
        if (type == EnumTimeMachineComponentType.CONTROLPANEL) {
            timeMachine.run(worldIn, playerIn, pos, side);
            return true;
        } else {return false;}
    }

    public EnumTimeMachineComponentType getType() {
        return type;
    }

    public TimeMachine getTimeMachine() {
        return timeMachine;
    }
}
