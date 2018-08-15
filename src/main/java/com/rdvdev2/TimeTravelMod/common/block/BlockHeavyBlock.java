package com.rdvdev2.TimeTravelMod.common.block;

import com.rdvdev2.TimeTravelMod.TimeTravelMod;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockHeavyBlock extends Block {

    private String name = "heavyblock";

    public BlockHeavyBlock() {
        super(Material.IRON);
        setSoundType(SoundType.METAL);
        setHardness(7f);
        setLightLevel (0 / 16f);
        setLightOpacity(15);
        setUnlocalizedName(name);
        setCreativeTab(TimeTravelMod.tabTTM);
        setRegistryName(name);
        setHarvestLevel("pickaxe", 3);
    }
}
