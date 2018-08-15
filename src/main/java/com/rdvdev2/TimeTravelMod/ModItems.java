package com.rdvdev2.TimeTravelMod;

import com.rdvdev2.TimeTravelMod.common.item.ItemControllerCircuit;
import com.rdvdev2.TimeTravelMod.common.item.ItemHeavyIngot;
import com.rdvdev2.TimeTravelMod.common.item.ItemTimeCrystal;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid="timetravelmod")
public class ModItems {

    public static Item timeCrystal;
    public static Item controllerCircuit;
    public static Item heavyIngot;

    public static void init() {
        timeCrystal = new ItemTimeCrystal();
        if (ModConfigs.unimplementedBlocks) {
            controllerCircuit = new ItemControllerCircuit();
            heavyIngot = new ItemHeavyIngot();
        }
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                timeCrystal
        );
        if (ModConfigs.unimplementedBlocks) {
            event.getRegistry().registerAll(
                    controllerCircuit,
                    heavyIngot
            );
        }
    }

    @SubscribeEvent
    public static void registerRenders(ModelRegistryEvent event) {
        registerRender(
                timeCrystal
        );
        if (ModConfigs.unimplementedBlocks) {
            registerRender(
                    controllerCircuit,
                    heavyIngot
            );
        }
    }

    private static void registerRender(Item... item) {
        for (int i = 0; i < item.length; i++) {
            ModelLoader.setCustomModelResourceLocation(item[i], 0, new ModelResourceLocation(item[i].getRegistryName(), "inventory"));
        }
    }
}
