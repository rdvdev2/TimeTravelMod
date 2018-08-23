package com.rdvdev2.TimeTravelMod;

import com.rdvdev2.TimeTravelMod.common.block.BlockTimeCrystalOre;
import com.rdvdev2.TimeTravelMod.common.block.BlockTimeMachineBasicBlock;
import com.rdvdev2.TimeTravelMod.common.block.BlockTimeMachineControlPanel;
import com.rdvdev2.TimeTravelMod.common.block.BlockTimeMachineCore;
import com.rdvdev2.TimeTravelMod.common.event.EventSetTimeMachine;
import com.rdvdev2.TimeTravelMod.util.BlockTimeMachineComponent;
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
    public static BlockTimeMachineComponent timeMachineBasicBlock;
    public static BlockTimeMachineComponent timeMachineCore;
    public static BlockTimeMachineComponent timeMachineControlPanel;
    public static Block heavyBlock;
    public static Block reinforcedHeavyBlock;

    public static void init() {
        timeCrystalOre = new BlockTimeCrystalOre();
        timeMachineBasicBlock = new BlockTimeMachineBasicBlock();
        timeMachineCore = new BlockTimeMachineCore();
        timeMachineControlPanel = new BlockTimeMachineControlPanel();
        if (ModConfigs.unimplementedBlocks) {
            heavyBlock = new BlockHeavyBlock();
            reinforcedHeavyBlock = new BlockReinforcedHeavyBlock();
        }
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(
                timeCrystalOre,
                timeMachineBasicBlock,
                timeMachineCore,
                timeMachineControlPanel
        );
        if (ModConfigs.unimplementedBlocks) {
            event.getRegistry().registerAll(
                    heavyBlock,
                    reinforcedHeavyBlock
            );
        }
    }

    @SubscribeEvent
    public static void registerItemBlocks(RegistryEvent.Register<Item> event) {
        registerItemBlock(event,
                timeCrystalOre,
                timeMachineBasicBlock,
                timeMachineCore,
                timeMachineControlPanel
        );
        if (ModConfigs.unimplementedBlocks) {
            registerItemBlock(event,
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
                timeCrystalOre,
                timeMachineBasicBlock,
                timeMachineCore,
                timeMachineControlPanel
        );
        if (ModConfigs.unimplementedBlocks) {
            registerRender(
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

    @SubscribeEvent
    public static void linkTimeMachines(EventSetTimeMachine event) {
        linkTimeMachine( event,
                timeMachineBasicBlock,
                timeMachineCore,
                timeMachineControlPanel
        );
    }

    private static void linkTimeMachine(EventSetTimeMachine event, BlockTimeMachineComponent... blocks) {
        for (int i = 0; i < blocks.length; i++) {
            blocks[i].setTimeMachine(event);
        }
    }
}
