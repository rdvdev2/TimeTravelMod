package tk.rdvdev2.TimeTravelMod.common.item;

import net.minecraft.item.Item;
import tk.rdvdev2.TimeTravelMod.TimeTravelMod;

public class ItemTimeCrystal extends Item {

    String name = "timecrystal";

    public ItemTimeCrystal() {
        super(new Properties()
                .maxStackSize(64)
                .group(TimeTravelMod.tabTTM)
        );
        setRegistryName(name);
    }
}
