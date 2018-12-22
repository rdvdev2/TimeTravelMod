package tk.rdvdev2.TimeTravelMod.common.item;

import net.minecraft.item.Item;
import tk.rdvdev2.TimeTravelMod.TimeTravelMod;

public class ItemHeavyIngot extends Item {

    private String name = "heavyingot";

    public ItemHeavyIngot() {
        super();
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(TimeTravelMod.tabTTM);
        setMaxStackSize(64);
    }
}
