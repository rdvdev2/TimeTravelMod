package tk.rdvdev2.TimeTravelMod.common.item;

import net.minecraft.item.Item;
import tk.rdvdev2.TimeTravelMod.TimeTravelMod;

public class ItemTimeCrystal extends Item {

    String name = "timecrystal";

    public ItemTimeCrystal() {
        super();
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(TimeTravelMod.tabTTM);
        setMaxStackSize(64);
    }
}
