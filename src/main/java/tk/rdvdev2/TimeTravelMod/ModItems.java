package tk.rdvdev2.TimeTravelMod;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import tk.rdvdev2.TimeTravelMod.common.item.CreativeTimeMachineItem;
import tk.rdvdev2.TimeTravelMod.common.item.ItemEngineerBook;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {

    public static Item timeCrystal = new Item(new Item.Properties().group(TimeTravelMod.tabTTM)).setRegistryName("timecrystal");
    public static Item controllerCircuit = new Item(new Item.Properties().group(TimeTravelMod.tabTTM)).setRegistryName("controllercircuit");
    public static Item heavyIngot = new Item(new Item.Properties().group(TimeTravelMod.tabTTM)).setRegistryName("heavyingot");
    public static Item creativeTimeMachine = new CreativeTimeMachineItem();
    public static Item engineerBook = new ItemEngineerBook();

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                timeCrystal,
                controllerCircuit,
                heavyIngot,
                creativeTimeMachine,
                engineerBook
        );
    }
}
