package tk.rdvdev2.TimeTravelMod;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import tk.rdvdev2.TimeTravelMod.api.timemachine.block.AbstractTimeMachineCoreBlock;
import tk.rdvdev2.TimeTravelMod.api.timemachine.block.tileentity.TMCooldownTileEntity;
import tk.rdvdev2.TimeTravelMod.common.block.*;
import tk.rdvdev2.TimeTravelMod.common.block.tileentity.TemporalCauldronTileEntity;

import java.util.HashSet;
import java.util.Iterator;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class ModBlocks {

    public static Block timeCrystalOre = new TimeCrystalOreBlock();
    public static Block timeMachineBasicBlock = new TimeMachineBasicBlock();
    public static Block timeMachineCore = new tk.rdvdev2.TimeTravelMod.common.block.TimeMachineCoreBlock();
    public static Block timeMachineControlPanel = new TimeMachineControlPanelBlock();
    public static Block heavyBlock = new HeavyBlock();
    public static Block reinforcedHeavyBlock = new ReinforcedHeavyBlock();
    public static Block temporalExplosion = new TemporalExplosionBlock();
    public static Block temporalCauldron = new TemporalCauldronBlock();
    public static Block gunpowderWire = new GunpowderWireBlock();

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
    public static void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> event) {
        TemporalCauldronTileEntity.type = TileEntityType.Builder.create(TemporalCauldronTileEntity::new, ModBlocks.temporalCauldron).build(null);
        TemporalCauldronTileEntity.type.setRegistryName("timetravelmod", "temporalcauldron");
        TMCooldownTileEntity.type = TileEntityType.Builder.create(TMCooldownTileEntity::new, getAllCoreBlocks()).build(null);
        TMCooldownTileEntity.type.setRegistryName("timetravelmod", "tmcooldown");
        event.getRegistry().registerAll(
                TemporalCauldronTileEntity.type,
                TMCooldownTileEntity.type
        );
    }

    private static Block[] getAllCoreBlocks() {
        HashSet<Block> blocks = new HashSet<Block>();
        Iterator<Block> blockIterator = ForgeRegistries.BLOCKS.getValues().iterator();
        while (blockIterator.hasNext()) {
            Block block = blockIterator.next();
            if (block instanceof AbstractTimeMachineCoreBlock) blocks.add(block);
        }
        return blocks.toArray(new Block[]{});
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

    @OnlyIn(Dist.CLIENT)
    public static void registerBlockColor(ColorHandlerEvent.Block event) {
        event.getBlockColors().register((state, world, pos, num) -> GunpowderWireBlock.colorMultiplier(state.get(GunpowderWireBlock.BURNED)), ModBlocks.gunpowderWire);
    }
}
