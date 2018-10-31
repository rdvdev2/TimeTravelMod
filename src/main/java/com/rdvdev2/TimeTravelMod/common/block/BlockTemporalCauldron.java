package com.rdvdev2.TimeTravelMod.common.block;

import com.rdvdev2.TimeTravelMod.TimeTravelMod;
import net.minecraft.block.BlockCauldron;

public class BlockTemporalCauldron extends BlockCauldron {
    private String name = "temporalcauldron";

    public BlockTemporalCauldron() {
        super();
        this.setRegistryName(name);
        this.setUnlocalizedName(name);
        this.setCreativeTab(TimeTravelMod.tabTTM);
    }
}
