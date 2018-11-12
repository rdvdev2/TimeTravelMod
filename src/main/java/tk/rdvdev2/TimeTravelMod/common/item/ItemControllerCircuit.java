package tk.rdvdev2.TimeTravelMod.common.item;

import net.minecraft.item.Item;
import tk.rdvdev2.TimeTravelMod.TimeTravelMod;

public class ItemControllerCircuit extends Item {

    private String name = "controllercircuit";

    public ItemControllerCircuit() {
        super();
        setUnlocalizedName(name);
        setRegistryName(name);
        setCreativeTab(TimeTravelMod.tabTTM);
        setMaxStackSize(64);
    }
}
