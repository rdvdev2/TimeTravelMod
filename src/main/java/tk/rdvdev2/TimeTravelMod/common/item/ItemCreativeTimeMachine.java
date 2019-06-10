package tk.rdvdev2.TimeTravelMod.common.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import tk.rdvdev2.TimeTravelMod.ModTimeMachines;
import tk.rdvdev2.TimeTravelMod.TimeTravelMod;
import tk.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;
import tk.rdvdev2.TimeTravelMod.common.event.EventConfigureTimeMachineBlocks;

public class ItemCreativeTimeMachine extends Item {
    private String name = "creativetimemachine";
    private TimeMachine timeMachine;

    public ItemCreativeTimeMachine() {
        super(new Properties()
                .maxStackSize(1)
                .group(TimeTravelMod.tabTTM)
        );
        setRegistryName(name);
        MinecraftForge.EVENT_BUS.addListener(this::setTimeMachine);
    }

    public void setTimeMachine(EventConfigureTimeMachineBlocks event) {
        this.timeMachine = ModTimeMachines.timeMachineCreative;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (!worldIn.isRemote) this.timeMachine.run(worldIn, playerIn, playerIn.getPosition(), Direction.NORTH);
        return new ActionResult<ItemStack>(ActionResultType.SUCCESS, playerIn.getHeldItem(handIn));
    }
}
