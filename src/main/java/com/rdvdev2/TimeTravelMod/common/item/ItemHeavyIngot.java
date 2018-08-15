package com.rdvdev2.TimeTravelMod.common.item;

import com.rdvdev2.TimeTravelMod.TimeTravelMod;
import net.minecraft.item.Item;

public class ItemHeavyIngot extends Item {

    private String name = "heavyingot";

    public ItemHeavyIngot() {
        super();
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(TimeTravelMod.tabTTM);
        setMaxStackSize(64);
    }
}
