package tk.rdvdev2.TimeTravelMod;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import tk.rdvdev2.TimeTravelMod.common.item.ControllerCircuitItem;
import tk.rdvdev2.TimeTravelMod.common.item.CreativeTimeMachineItem;
import tk.rdvdev2.TimeTravelMod.common.item.HeavyIngotItem;
import tk.rdvdev2.TimeTravelMod.common.item.TimeCrystalItem;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {

    public static Item timeCrystal = new TimeCrystalItem();
    public static Item controllerCircuit = new ControllerCircuitItem();
    public static Item heavyIngot = new HeavyIngotItem();
    public static Item creativeTimeMachine = new CreativeTimeMachineItem();

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
