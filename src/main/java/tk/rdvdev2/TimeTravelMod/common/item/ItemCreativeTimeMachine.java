package tk.rdvdev2.TimeTravelMod.common.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import tk.rdvdev2.TimeTravelMod.ModTimeMachines;
import tk.rdvdev2.TimeTravelMod.TimeTravelMod;
import tk.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;
import tk.rdvdev2.TimeTravelMod.common.event.EventSetTimeMachine;

public class ItemCreativeTimeMachine extends Item {
    private String name = "creativetimemachine";
    private TimeMachine timeMachine;

    public ItemCreativeTimeMachine() {
        super();
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(TimeTravelMod.tabTTM);
        setMaxStackSize(1);
    }

    public void setTimeMachine(EventSetTimeMachine event) {
        this.timeMachine = ModTimeMachines.timeMachineCreative;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        this.timeMachine.run(worldIn, playerIn, playerIn.getPosition(), EnumFacing.NORTH);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }
}
