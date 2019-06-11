package tk.rdvdev2.TimeTravelMod;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import tk.rdvdev2.TimeTravelMod.api.timemachine.entity.TileEntityTMCooldown;
import tk.rdvdev2.TimeTravelMod.common.block.*;
import tk.rdvdev2.TimeTravelMod.common.block.tileentity.TileEntityTemporalCauldron;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class ModBlocks {

    public static Block timeCrystalOre = new BlockTimeCrystalOre();
    public static Block timeMachineBasicBlock = new BlockTimeMachineBasicBlock();
    public static Block timeMachineCore = new BlockTimeMachineCore();
    public static Block timeMachineControlPanel = new BlockTimeMachineControlPanel();
    public static Block heavyBlock = new BlockHeavyBlock();
    public static Block reinforcedHeavyBlock = new BlockReinforcedHeavyBlock();
    public static Block temporalExplosion = new BlockTemporalExplosion();
    public static Block temporalCauldron = new BlockTemporalCauldron();
    public static Block gunpowderWire = new BlockGunpowderWire();

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
                temporalCauldron,
                gunpowderWire
        );
    }

    @SubscribeEvent
    public static void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> event) { // TODO: Check all this works
        TileEntityTemporalCauldron.type = TileEntityType.Builder.func_223042_a(TileEntityTemporalCauldron::new).build(null);
        TileEntityTemporalCauldron.type.setRegistryName("timetravelmod", "temporalcauldron");
        TileEntityTMCooldown.type = TileEntityType.Builder.func_223042_a(TileEntityTMCooldown::new).build(null);
        TileEntityTMCooldown.type.setRegistryName("timetravelmod", "tmcooldown");
        event.getRegistry().registerAll(
                TileEntityTemporalCauldron.type,
                TileEntityTMCooldown.type
        );
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
        final String gunpowderTranslationKey = Items.GUNPOWDER.getTranslationKey();
        event.getRegistry().register(new BlockItem(gunpowderWire, new Item.Properties().maxStackSize(64).group(Items.GUNPOWDER.getGroup())){
            @Override public String getTranslationKey() { return gunpowderTranslationKey; }
        }.setRegistryName(Items.GUNPOWDER.getRegistryName()));
    }

    private static void registerItemBlock(RegistryEvent.Register<Item> event, Block... blocks) {
        for (int i = 0; i < blocks.length; i++) {
            event.getRegistry().register(new BlockItem(blocks[i], new Item.Properties().maxStackSize(64).group(TimeTravelMod.tabTTM)).setRegistryName(blocks[i].getRegistryName()));
        }
    }

    public static void registerBlockColor(ColorHandlerEvent.Block event) {
        event.getBlockColors().register((state, world, pos, num) -> BlockGunpowderWire.colorMultiplier(state.get(BlockGunpowderWire.BURNED)), ModBlocks.gunpowderWire);
    }
}
