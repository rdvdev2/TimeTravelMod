package tk.rdvdev2.TimeTravelMod.common.item;

import net.minecraft.item.Item;
import tk.rdvdev2.TimeTravelMod.TimeTravelMod;

public class TimeCrystalItem extends Item {

    String name = "timecrystal";

    public TimeCrystalItem() {
        super(new Properties()
                .maxStackSize(64)
                .group(TimeTravelMod.tabTTM)
        );
        setRegistryName(name);
    }
}