package tk.rdvdev2.TimeTravelMod.common.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import tk.rdvdev2.TimeTravelMod.TimeTravelMod;

public class ItemEngineerBook extends Item {

    private String name = "engineerbook";

    public ItemEngineerBook() {
        super(new Properties()
                .group(TimeTravelMod.tabTTM)
                .maxStackSize(1));
        setRegistryName(name);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        EnumActionResult result = EnumActionResult.PASS;
        NBTTagCompound data = playerIn.getHeldItem(handIn).getOrCreateChildTag("data");
        if (data.isEmpty()) {
            data.setInt("page", 0);
            data.setInt("y", 0);
        }
        if (worldIn.isRemote) {
            TimeTravelMod.proxy.displayEngineerBookGuiScreen(playerIn);
            result = EnumActionResult.SUCCESS;
        }
        return new ActionResult<ItemStack>(result, playerIn.getHeldItem(handIn));
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return true;
    }
}
