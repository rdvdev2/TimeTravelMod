package com.rdvdev2.TimeTravelMod.common.block;

import com.rdvdev2.TimeTravelMod.TimeTravelMod;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockTimeCrystalOre extends Block {

    String name = "timecrystalore";

    public BlockTimeCrystalOre() {
        super(Material.ROCK);
        setSoundType(SoundType.STONE);
        setHardness(5f);
        setLightLevel (5 / 16f);
        setLightOpacity(15);
        setUnlocalizedName(name);
        setCreativeTab(TimeTravelMod.tabTTM);
        setRegistryName(name);
        setHarvestLevel("pickaxe", 3);

    }


}
