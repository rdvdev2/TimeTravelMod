package com.rdvdev2.TimeTravelMod.common.block;

import com.rdvdev2.TimeTravelMod.ModRegistries;
import com.rdvdev2.TimeTravelMod.TimeTravelMod;
import com.rdvdev2.TimeTravelMod.util.BlockTimeMachineComponent;
import com.rdvdev2.TimeTravelMod.util.EnumTimeMachineComponentType;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockTimeMachineControlPanel extends BlockTimeMachineComponent {

    private String name = "timemachinecontrolpanel";

    public BlockTimeMachineControlPanel() {
        super(Material.IRON, EnumTimeMachineComponentType.CONTROLPANEL);
        setSoundType(SoundType.METAL);
        setHardness(3f);
        setLightLevel (0 / 16f);
        setLightOpacity(15);
        setCreativeTab(TimeTravelMod.tabTTM);
        setHarvestLevel("pickaxe", 2);
        setUnlocalizedName(name);
        setRegistryName(name);
    }
}
