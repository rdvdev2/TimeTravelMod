package tk.rdvdev2.TimeTravelMod;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import tk.rdvdev2.TimeTravelMod.api.timemachine.block.BlockTimeMachineComponent;
import tk.rdvdev2.TimeTravelMod.api.timemachine.entity.TileEntityTMCooldown;
import tk.rdvdev2.TimeTravelMod.common.block.*;
import tk.rdvdev2.TimeTravelMod.common.block.tileentity.TileEntityTemporalCauldron;

@Mod.EventBusSubscriber(modid = "timetravelmod")
public class ModBlocks {

    public static Block timeCrystalOre;
    public static BlockTimeMachineComponent timeMachineBasicBlock;
    public static BlockTimeMachineComponent timeMachineCore;
    public static BlockTimeMachineComponent timeMachineControlPanel;
    public static Block heavyBlock;
    public static Block reinforcedHeavyBlock;
    public static Block temporalExplosion;
    public static Block temporalCauldron;

    public static void init() {
        timeCrystalOre = new BlockTimeCrystalOre();
        timeMachineBasicBlock = new BlockTimeMachineBasicBlock();
        timeMachineCore = new BlockTimeMachineCore();
        timeMachineControlPanel = new BlockTimeMachineControlPanel();
        heavyBlock = new BlockHeavyBlock();
        reinforcedHeavyBlock = new BlockReinforcedHeavyBlock();
        temporalExplosion = new BlockTemporalExplosion();
        temporalCauldron = new BlockTemporalCauldron();
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(
                timeCrystalOre,
                timeMachineBasicBlock,
                timeMachineCore,
                timeMachineControlPanel,
                heavyBlock,
                reinforcedHeavyBlock,
                temporalExplosion,
                temporalCauldron
        );
        GameRegistry.registerTileEntity(TileEntityTMCooldown.class, new ResourceLocation("timetravelmod:entity.tmcooldown"));
        GameRegistry.registerTileEntity(TileEntityTemporalCauldron.class, new ResourceLocation("timetravelmod:entity.temporalcauldron"));
    }

    @SubscribeEvent
    public static void registerItemBlocks(RegistryEvent.Register<Item> event) {
        registerItemBlock(event,
                timeCrystalOre,
                timeMachineBasicBlock,
                timeMachineCore,
                timeMachineControlPanel,
                heavyBlock,
                reinforcedHeavyBlock,
                temporalExplosion,
                temporalCauldron
        );
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
                timeMachineControlPanel,
                heavyBlock,
                reinforcedHeavyBlock,
                temporalExplosion,
                temporalCauldron
        );
        if(!ModConfigs.vanillaCauldronTexture)
            ModelLoader.setCustomModelResourceLocation(Items.CAULDRON, 0, new ModelResourceLocation("timetravelmod:cauldron", "inventory"));
    }

    private static void registerRender(Block... blocks) {
        for (int i = 0; i < blocks.length; i++) {
            Item item = Item.getItemFromBlock(blocks[i]);
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
        }
    }
}
