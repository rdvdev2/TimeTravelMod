package com.rdvdev2.TimeTravelMod;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModRecipes {

    public static void init() {
        GameRegistry.addSmelting(ModBlocks.timeCrystalOre, new ItemStack(ModItems.timeCrystal, 1), 3.0f);
    }
}
