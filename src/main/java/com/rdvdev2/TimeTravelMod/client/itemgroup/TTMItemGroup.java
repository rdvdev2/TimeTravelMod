package com.rdvdev2.TimeTravelMod.client.itemgroup;

import com.rdvdev2.TimeTravelMod.ModItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TTMItemGroup extends ItemGroup {

    public TTMItemGroup(){
        super("timetravelmod");
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public ItemStack createIcon(){
        return new ItemStack(ModItems.TIME_CRYSTAL.get());
    }
}
