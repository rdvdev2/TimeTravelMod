package tk.rdvdev2.TimeTravelMod;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tk.rdvdev2.TimeTravelMod.common.event.EventSetTimeMachine;
import tk.rdvdev2.TimeTravelMod.common.item.ItemControllerCircuit;
import tk.rdvdev2.TimeTravelMod.common.item.ItemCreativeTimeMachine;
import tk.rdvdev2.TimeTravelMod.common.item.ItemHeavyIngot;
import tk.rdvdev2.TimeTravelMod.common.item.ItemTimeCrystal;

@Mod.EventBusSubscriber(modid="timetravelmod")
public class ModItems {

    public static Item timeCrystal;
    public static Item controllerCircuit;
    public static Item heavyIngot;
    public static Item creativeTimeMachine;

    public static void init() {
        timeCrystal = new ItemTimeCrystal();
        controllerCircuit = new ItemControllerCircuit();
        heavyIngot = new ItemHeavyIngot();
        creativeTimeMachine = new ItemCreativeTimeMachine();
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                timeCrystal,
                controllerCircuit,
                heavyIngot,
                creativeTimeMachine
        );
    }

    @SubscribeEvent
    public static void registerRenders(ModelRegistryEvent event) {
        registerRender(
                timeCrystal,
                controllerCircuit,
                heavyIngot,
                creativeTimeMachine
        );
    }

    private static void registerRender(Item... item) {
        for (int i = 0; i < item.length; i++) {
            ModelLoader.setCustomModelResourceLocation(item[i], 0, new ModelResourceLocation(item[i].getRegistryName(), "inventory"));
        }
    }

    @SubscribeEvent
    public static void linkTimeMachines(EventSetTimeMachine event) {
        if (ModConfigs.unimplementedBlocks) {
            ((ItemCreativeTimeMachine)creativeTimeMachine).setTimeMachine(event);
        }
    }
}
