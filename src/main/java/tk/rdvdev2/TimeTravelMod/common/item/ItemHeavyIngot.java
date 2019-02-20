package tk.rdvdev2.TimeTravelMod.common.item;

import net.minecraft.item.Item;
import tk.rdvdev2.TimeTravelMod.TimeTravelMod;

public class ItemHeavyIngot extends Item {

    private String name = "heavyingot";

    public ItemHeavyIngot() {
        super(new Properties()
                .maxStackSize(64)
                .group(TimeTravelMod.tabTTM)
        );
        setRegistryName(name);
    }
}
