package com.rdvdev2.TimeTravelMod.common.item;

import com.rdvdev2.TimeTravelMod.ModTimeMachines;
import com.rdvdev2.TimeTravelMod.TimeTravelMod;
import com.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class CreativeTimeMachineItem extends Item {

    private TimeMachine timeMachine = ModTimeMachines.CREATIVE;

    public CreativeTimeMachineItem() {
        super(new Properties()
                .maxStackSize(1)
                .group(TimeTravelMod.TAB_TTM)
        );
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (!worldIn.isRemote) this.timeMachine.run(worldIn, playerIn, playerIn.getPosition(), Direction.NORTH);
        return new ActionResult<ItemStack>(ActionResultType.SUCCESS, playerIn.getHeldItem(handIn));
    }
}
