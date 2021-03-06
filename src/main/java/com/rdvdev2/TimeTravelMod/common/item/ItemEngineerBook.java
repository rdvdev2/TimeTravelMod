package com.rdvdev2.TimeTravelMod.common.item;

import com.rdvdev2.TimeTravelMod.TimeTravelMod;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class ItemEngineerBook extends Item {

    public ItemEngineerBook() {
        super(new Properties()
                .group(TimeTravelMod.TAB_TTM)
                .maxStackSize(1));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ActionResultType result = ActionResultType.PASS;
        if (worldIn.isRemote) {
            TimeTravelMod.PROXY.displayEngineerBookGuiScreen(playerIn);
            result = ActionResultType.PASS;
        }
        return new ActionResult<ItemStack>(result, playerIn.getHeldItem(handIn));
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return true;
    }
}
