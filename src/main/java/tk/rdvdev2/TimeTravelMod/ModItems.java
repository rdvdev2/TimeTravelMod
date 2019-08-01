package tk.rdvdev2.TimeTravelMod;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import tk.rdvdev2.TimeTravelMod.common.item.CreativeTimeMachineItem;
import tk.rdvdev2.TimeTravelMod.common.item.ItemEngineerBook;

import static tk.rdvdev2.TimeTravelMod.TimeTravelMod.MODID;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {

    public static final Item TIME_CRYSTAL = new Item(new Item.Properties().group(TimeTravelMod.TAB_TTM)).setRegistryName(MODID, "timecrystal");
    public static final Item CONTROLLER_CIRCUIT = new Item(new Item.Properties().group(TimeTravelMod.TAB_TTM)).setRegistryName(MODID, "controllercircuit");
    public static final Item HEAVY_INGOT = new Item(new Item.Properties().group(TimeTravelMod.TAB_TTM)).setRegistryName(MODID, "heavyingot");
    public static final Item CREATIVE_TIME_MACHINE = new CreativeTimeMachineItem();
    public static final Item ENGINEER_BOOK = new ItemEngineerBook();

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                TIME_CRYSTAL,
                CONTROLLER_CIRCUIT,
                HEAVY_INGOT,
                CREATIVE_TIME_MACHINE,
                ENGINEER_BOOK
        );
    }
}
