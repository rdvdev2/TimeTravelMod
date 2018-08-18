package com.rdvdev2.TimeTravelMod.common.block;

import com.rdvdev2.TimeTravelMod.TimeTravelMod;
import com.rdvdev2.TimeTravelMod.util.BlockTimeMachineComponent;
import com.rdvdev2.TimeTravelMod.util.EnumTimeMachineComponentType;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockTimeMachineCore extends BlockTimeMachineComponent {

    private String name = "timemachinecore";

    public BlockTimeMachineCore() {
        super(Material.IRON, EnumTimeMachineComponentType.CORE);
        setSoundType(SoundType.METAL);
        setHarvestLevel("pickaxe", 3);
        setLightLevel(5 / 16f);
        setLightOpacity(15);
        setHardness(4f);
        setCreativeTab(TimeTravelMod.tabTTM);
        setUnlocalizedName(name);
        setRegistryName(name);
    }
}
