package com.rdvdev2.TimeTravelMod.common.item;

import com.rdvdev2.TimeTravelMod.TimeTravelMod;
import net.minecraft.item.Item;

public class ItemControllerCircuit extends Item {

    private String name = "controllercircuit";

    public ItemControllerCircuit() {
        super();
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(TimeTravelMod.tabTTM);
        setMaxStackSize(64);
    }
}
