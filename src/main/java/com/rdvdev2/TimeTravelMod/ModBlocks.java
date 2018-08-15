package com.rdvdev2.TimeTravelMod;

import com.rdvdev2.TimeTravelMod.common.block.*;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = "timetravelmod")
public class ModBlocks {

    public static Block timeCrystalOre;
    public static Block timeMachineBasicBlock;
    public static Block timeMachineCore;
    public static Block timeMachineControlPanel;
    public static Block heavyBlock;
    public static Block reinforcedHeavyBlock;

    public static void init() {
        timeCrystalOre = new BlockTimeCrystalOre();
        if (ModConfigs.unimplementedBlocks) {
            timeMachineBasicBlock = new BlockTimeMachineBasicBlock().setNames();
            timeMachineCore = new BlockTimeMachineCore().setNames();
            timeMachineControlPanel = new BlockTimeMachineControlPanel().setNames();
            heavyBlock = new BlockHeavyBlock();
            reinforcedHeavyBlock = new BlockReinforcedHeavyBlock();
        }
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(
                timeCrystalOre
        );
        if (ModConfigs.unimplementedBlocks) {
            event.getRegistry().registerAll(
                    timeMachineBasicBlock,
                    timeMachineCore,
                    timeMachineControlPanel,
                    heavyBlock,
                    reinforcedHeavyBlock
            );
        }
    }

    @SubscribeEvent
    public static void registerItemBlocks(RegistryEvent.Register<Item> event) {
        registerItemBlock(event,
                timeCrystalOre
        );
        if (ModConfigs.unimplementedBlocks) {
            registerItemBlock(event,
                    timeMachineBasicBlock,
                    timeMachineCore,
                    timeMachineControlPanel,
                    heavyBlock,
                    reinforcedHeavyBlock
            );
        }
    }

    private static void registerItemBlock(RegistryEvent.Register<Item> event, Block... blocks) {
        for (int i = 0; i < blocks.length; i++) {
            event.getRegistry().register(new ItemBlock(blocks[i]).setRegistryName(blocks[i].getRegistryName()));
        }
    }

    @SubscribeEvent
    public static void registerRenders(ModelRegistryEvent event) {
        registerRender(
                timeCrystalOre
        );
        if (ModConfigs.unimplementedBlocks) {
            registerRender(
                    timeMachineBasicBlock,
                    timeMachineCore,
                    timeMachineControlPanel,
                    heavyBlock,
                    reinforcedHeavyBlock
            );
        }
    }

    private static void registerRender(Block... blocks) {
        for (int i = 0; i < blocks.length; i++) {
            Item item = Item.getItemFromBlock(blocks[i]);
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
        }
    }
}
