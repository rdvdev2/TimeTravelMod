package com.rdvdev2.TimeTravelMod.common.block;

import com.rdvdev2.TimeTravelMod.TimeTravelMod;
import com.rdvdev2.TimeTravelMod.api.timemachine.block.BlockTimeMachineComponent;
import com.rdvdev2.TimeTravelMod.api.timemachine.block.EnumTimeMachineComponentType;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockTimeMachineBasicBlock extends BlockTimeMachineComponent {

    String name = "timemachinebasicblock";

    public BlockTimeMachineBasicBlock() {
        super(Material.IRON, EnumTimeMachineComponentType.BASIC);
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
