package tk.rdvdev2.TimeTravelMod.common.item;

import net.minecraft.item.Item;
import tk.rdvdev2.TimeTravelMod.TimeTravelMod;

public class ControllerCircuitItem extends Item {

    private String name = "controllercircuit";

    public ControllerCircuitItem() {
        super(new Properties()
                .maxStackSize(64)
                .group(TimeTravelMod.tabTTM)
        );
        setRegistryName(name);
    }
}
