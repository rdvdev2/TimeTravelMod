package tk.rdvdev2.TimeTravelMod;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import tk.rdvdev2.TimeTravelMod.api.timemachine.block.TimeMachineBasicBlock;
import tk.rdvdev2.TimeTravelMod.api.timemachine.block.TimeMachineControlPanelBlock;
import tk.rdvdev2.TimeTravelMod.api.timemachine.block.TimeMachineCoreBlock;
import tk.rdvdev2.TimeTravelMod.api.timemachine.block.TimeMachineUpgradeBlock;
import tk.rdvdev2.TimeTravelMod.api.timemachine.block.tileentity.TMCooldownTileEntity;
import tk.rdvdev2.TimeTravelMod.common.block.GunpowderWireBlock;
import tk.rdvdev2.TimeTravelMod.common.block.TemporalCauldronBlock;
import tk.rdvdev2.TimeTravelMod.common.block.TemporalExplosionBlock;
import tk.rdvdev2.TimeTravelMod.common.block.TimeMachineRecallerBlock;
import tk.rdvdev2.TimeTravelMod.common.block.tileentity.TemporalCauldronTileEntity;
import tk.rdvdev2.TimeTravelMod.common.block.tileentity.TimeMachineRecallerTileEntity;

import java.util.HashSet;
import java.util.Iterator;

import static tk.rdvdev2.TimeTravelMod.TimeTravelMod.MODID;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class ModBlocks {

    public static final Block TIME_CRYSTAL_ORE = new Block((Block.Properties.create(Material.ROCK).sound(SoundType.STONE).hardnessAndResistance(5f).lightValue(5/16).variableOpacity().harvestTool(ToolType.PICKAXE).harvestLevel(3))).setRegistryName(MODID, "timecrystalore");
    public static final Block TIME_MACHINE_BASIC_BLOCK = new TimeMachineBasicBlock(Block.Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(3f).lightValue(0 / 16).variableOpacity().harvestTool(ToolType.PICKAXE).harvestLevel(2)).setRegistryName(MODID, "timemachinebasicblock");
    public static final Block TIME_MACHINE_CORE = new TimeMachineCoreBlock(Block.Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(4f).lightValue(5 / 16).variableOpacity().harvestTool(ToolType.PICKAXE).harvestLevel(3)).setRegistryName(MODID, "timemachinecore");
    public static final Block TIME_MACHINE_CONTROL_PANEL = new TimeMachineControlPanelBlock(Block.Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(3f).lightValue(0 / 16).variableOpacity().harvestTool(ToolType.PICKAXE).harvestLevel(2)).setRegistryName(MODID, "timemachinecontrolpanel");
    public static final Block HEAVY_BLOCK = new Block(Block.Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(7f).lightValue(0/16).variableOpacity().harvestTool(ToolType.PICKAXE).harvestLevel(3)).setRegistryName(MODID, "heavyblock");
    public static final Block REINFORCED_HEAVY_BLOCK = new Block(Block.Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(10f).lightValue(10).variableOpacity().harvestTool(ToolType.PICKAXE).harvestLevel(3)).setRegistryName(MODID, "reinforcedheavyblock");
    public static final Block TEMPORAL_EXPLOSION = new TemporalExplosionBlock();
    public static final Block TEMPORAL_CAULDRON = new TemporalCauldronBlock();
    public static final Block GUNPOWDER_WIRE = new GunpowderWireBlock();
    public static final Block TIME_MACHINE_TRACKER = new TimeMachineUpgradeBlock(Block.Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(3f).lightValue(0 / 16).variableOpacity().harvestTool(ToolType.PICKAXE).harvestLevel(2)).setUpgrade(ModTimeMachines.Upgrades.TRACKER).setRegistryName(MODID, "timemachinetracker");
    public static final Block TIME_MACHINE_RECALLER = new TimeMachineRecallerBlock();

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(
                TIME_CRYSTAL_ORE,
                TIME_MACHINE_BASIC_BLOCK,
                TIME_MACHINE_CORE,
                TIME_MACHINE_CONTROL_PANEL,
                HEAVY_BLOCK,
                REINFORCED_HEAVY_BLOCK,
                TEMPORAL_EXPLOSION,
                TEMPORAL_CAULDRON,
                GUNPOWDER_WIRE,
                TIME_MACHINE_TRACKER,
                TIME_MACHINE_RECALLER
        );
    }

    @SubscribeEvent
    public static void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> event) {
        TemporalCauldronTileEntity.type = TileEntityType.Builder.create(TemporalCauldronTileEntity::new, ModBlocks.TEMPORAL_CAULDRON).build(null);
        TemporalCauldronTileEntity.type.setRegistryName(MODID, "temporalcauldron");
        TMCooldownTileEntity.type = TileEntityType.Builder.create(TMCooldownTileEntity::new, getAllCoreBlocks()).build(null);
        TMCooldownTileEntity.type.setRegistryName(MODID, "tmcooldown");
        TimeMachineRecallerTileEntity.type = TileEntityType.Builder.create(TimeMachineRecallerTileEntity::new, ModBlocks.TIME_MACHINE_RECALLER).build(null);
        TimeMachineRecallerTileEntity.type.setRegistryName("tmrecaller");
        event.getRegistry().registerAll(
                TemporalCauldronTileEntity.type,
                TMCooldownTileEntity.type,
                TimeMachineRecallerTileEntity.type
        );
    }

    private static Block[] getAllCoreBlocks() {
        HashSet<Block> blocks = new HashSet<Block>();
        Iterator<Block> blockIterator = ForgeRegistries.BLOCKS.getValues().iterator();
        while (blockIterator.hasNext()) {
            Block block = blockIterator.next();
            if (block instanceof TimeMachineCoreBlock) blocks.add(block);
        }
        return blocks.toArray(new Block[]{});
    }

    @SubscribeEvent
    public static void registerItemBlocks(RegistryEvent.Register<Item> event) {
        registerItemBlock(event,
                TIME_CRYSTAL_ORE,
                TIME_MACHINE_BASIC_BLOCK,
                TIME_MACHINE_CORE,
                TIME_MACHINE_CONTROL_PANEL,
                HEAVY_BLOCK,
                REINFORCED_HEAVY_BLOCK,
                TEMPORAL_EXPLOSION,
                TEMPORAL_CAULDRON,
                TIME_MACHINE_TRACKER,
                TIME_MACHINE_RECALLER
        );
        final String gunpowderTranslationKey = Items.GUNPOWDER.getTranslationKey();
        event.getRegistry().register(new BlockItem(GUNPOWDER_WIRE, new Item.Properties().maxStackSize(64).group(Items.GUNPOWDER.getGroup())){
            @Override public String getTranslationKey() { return gunpowderTranslationKey; }
        }.setRegistryName(Items.GUNPOWDER.getRegistryName()));
    }

    private static void registerItemBlock(RegistryEvent.Register<Item> event, Block... blocks) {
        for (int i = 0; i < blocks.length; i++) {
            event.getRegistry().register(new BlockItem(blocks[i], new Item.Properties().maxStackSize(64).group(TimeTravelMod.TAB_TTM)).setRegistryName(blocks[i].getRegistryName()));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerBlockColor(ColorHandlerEvent.Block event) {
        event.getBlockColors().register((state, world, pos, num) -> GunpowderWireBlock.colorMultiplier(state.get(GunpowderWireBlock.BURNED).booleanValue()), ModBlocks.GUNPOWDER_WIRE);
    }
}
