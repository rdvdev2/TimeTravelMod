package com.rdvdev2.TimeTravelMod;

import com.rdvdev2.TimeTravelMod.common.item.CreativeTimeMachineItem;
import com.rdvdev2.TimeTravelMod.common.item.ItemEngineerBook;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {

    private static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, TimeTravelMod.MODID);

    public static final RegistryObject<Item> TIME_CRYSTAL = ITEMS.register("timecrystal", () -> new Item(new Item.Properties().group(TimeTravelMod.TAB_TTM)));
    public static final RegistryObject<Item> CONTROLLER_CIRCUIT = ITEMS.register("controllercircuit", () -> new Item(new Item.Properties().group(TimeTravelMod.TAB_TTM)));
    public static final RegistryObject<Item> HEAVY_INGOT = ITEMS.register("heavyingot", () -> new Item(new Item.Properties().group(TimeTravelMod.TAB_TTM)));
    public static final RegistryObject<Item> CREATIVE_TIME_MACHINE = ITEMS.register("creativetimemachine", CreativeTimeMachineItem::new);
    public static final RegistryObject<Item> ENGINEER_BOOK = ITEMS.register("engineerbook", ItemEngineerBook::new);
    public static final RegistryObject<Item> COMMUNICATIONS_CIRCUIT = ITEMS.register("communicationscircuit", () -> new Item(new Item.Properties().group(TimeTravelMod.TAB_TTM)));

    public static final RegistryObject<Item> TIME_CRYSTAL_ORE = registerBlockItem(ModBlocks.TIME_CRYSTAL_ORE);
    public static final RegistryObject<Item> TIME_MACHINE_BASIC_BLOCK = registerBlockItem(ModBlocks.TIME_MACHINE_BASIC_BLOCK);
    public static final RegistryObject<Item> TIME_MACHINE_CORE = registerBlockItem(ModBlocks.TIME_MACHINE_CORE);
    public static final RegistryObject<Item> TIME_MACHINE_CONTROL_PANEL = registerBlockItem(ModBlocks.TIME_MACHINE_CONTROL_PANEL);
    public static final RegistryObject<Item> HEAVY_BLOCK = registerBlockItem(ModBlocks.HEAVY_BLOCK);
    public static final RegistryObject<Item> REINFORCED_HEAVY_BLOCK = registerBlockItem(ModBlocks.REINFORCED_HEAVY_BLOCK);
    public static final RegistryObject<Item> TEMPORAL_EXPLOSION = registerBlockItem(ModBlocks.TEMPORAL_EXPLOSION);
    public static final RegistryObject<Item> TEMPORAL_CAULDRON = registerBlockItem(ModBlocks.TEMPORAL_CAULDRON);
    public static final RegistryObject<Item> GUNPOWDER_WIRE = registerBlockItem(ModBlocks.GUNPOWDER_WIRE);
    public static final RegistryObject<Item> TIME_MACHINE_TRACKER = registerBlockItem(ModBlocks.TIME_MACHINE_TRACKER);
    public static final RegistryObject<Item> TIME_MACHINE_RECALLER = registerBlockItem(ModBlocks.TIME_MACHINE_RECALLER);

    private static RegistryObject<Item> registerBlockItem(RegistryObject<Block> block) {
        return ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties().group(TimeTravelMod.TAB_TTM)));
    }

    public static void init() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
