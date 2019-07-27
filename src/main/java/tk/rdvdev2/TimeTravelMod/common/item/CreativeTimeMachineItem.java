package tk.rdvdev2.TimeTravelMod.common.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import tk.rdvdev2.TimeTravelMod.ModTimeMachines;
import tk.rdvdev2.TimeTravelMod.TimeTravelMod;
import tk.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;

public class CreativeTimeMachineItem extends Item {
    private String name = "creativetimemachine";
    private TimeMachine timeMachine = ModTimeMachines.timeMachineCreative;

    public CreativeTimeMachineItem() {
        super(new Properties()
                .maxStackSize(1)
                .group(TimeTravelMod.tabTTM)
        );
        setRegistryName(name);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (!worldIn.isRemote) this.timeMachine.run(worldIn, playerIn, playerIn.getPosition(), Direction.NORTH);
        return new ActionResult<ItemStack>(ActionResultType.SUCCESS, playerIn.getHeldItem(handIn));
    }
}
