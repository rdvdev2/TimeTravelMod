package tk.rdvdev2.TimeTravelMod;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import tk.rdvdev2.TimeTravelMod.common.item.ItemControllerCircuit;
import tk.rdvdev2.TimeTravelMod.common.item.ItemCreativeTimeMachine;
import tk.rdvdev2.TimeTravelMod.common.item.ItemHeavyIngot;
import tk.rdvdev2.TimeTravelMod.common.item.ItemTimeCrystal;

@Mod.EventBusSubscriber(modid="timetravelmod")
public class ModItems {

    public static Item timeCrystal = new ItemTimeCrystal();
    public static Item controllerCircuit = new ItemControllerCircuit();
    public static Item heavyIngot = new ItemHeavyIngot();
    public static Item creativeTimeMachine = new ItemCreativeTimeMachine();

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                timeCrystal,
                controllerCircuit,
                heavyIngot,
                creativeTimeMachine
        );
    }
}
