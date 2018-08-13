package com.rdvdev2.TimeTravelMod.common.item;

import com.rdvdev2.TimeTravelMod.TimeTravelMod;
import net.minecraft.item.Item;

public class ItemTimeCrystal extends Item {

    String name = "timecrystal";

    public ItemTimeCrystal() {
        super();
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(TimeTravelMod.tabTTM);
        setMaxStackSize(64);
    }
}
