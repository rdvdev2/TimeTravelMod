package com.rdvdev2.TimeTravelMod.common.block;

import net.minecraft.block.Block;

public class BlockTimeMachineCore extends BlockTimeMachineBasicBlock {

    public BlockTimeMachineCore() {
        super();
        setHarvestLevel("pickaxe", 3);
        setLightLevel(5 / 16f);
        setHardness(4f);
    }

    public Block setNames() {
        String name = "timemachinecore";
        setUnlocalizedName(name);
        setRegistryName(name);
        return this;
    }
}
