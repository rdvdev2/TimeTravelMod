package com.rdvdev2.TimeTravelMod;

import com.rdvdev2.TimeTravelMod.api.timemachine.block.TimeMachineControlPanelBlock;
import com.rdvdev2.TimeTravelMod.api.timemachine.block.TimeMachineCoreBlock;
import com.rdvdev2.TimeTravelMod.api.timemachine.block.TimeMachineUpgradeBlock;
import com.rdvdev2.TimeTravelMod.common.block.GunpowderWireBlock;
import com.rdvdev2.TimeTravelMod.common.block.TemporalCauldronBlock;
import com.rdvdev2.TimeTravelMod.common.block.TemporalExplosionBlock;
import com.rdvdev2.TimeTravelMod.common.block.TimeMachineRecallerBlock;
import com.rdvdev2.TimeTravelMod.common.block.tileentity.TMCooldownTileEntity;
import com.rdvdev2.TimeTravelMod.common.block.tileentity.TemporalCauldronTileEntity;
import com.rdvdev2.TimeTravelMod.common.block.tileentity.TimeMachineRecallerTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashSet;

public class ModBlocks {

    private static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, TimeTravelMod.MODID);

    public static final RegistryObject<Block> TIME_CRYSTAL_ORE = BLOCKS.register("timecrystalore", () -> new Block((Block.Properties.create(Material.ROCK).sound(SoundType.STONE).hardnessAndResistance(5f).lightValue(5/16).variableOpacity().harvestTool(ToolType.PICKAXE).harvestLevel(3))));
    public static final RegistryObject<Block> TIME_MACHINE_BASIC_BLOCK = BLOCKS.register("timemachinebasicblock", () -> new Block(Block.Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(3f).lightValue(0 / 16).variableOpacity().harvestTool(ToolType.PICKAXE).harvestLevel(2)));
    public static final RegistryObject<Block> TIME_MACHINE_CORE = BLOCKS.register("timemachinecore", () -> new TimeMachineCoreBlock(Block.Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(4f).lightValue(5 / 16).variableOpacity().harvestTool(ToolType.PICKAXE).harvestLevel(3)));
    public static final RegistryObject<Block> TIME_MACHINE_CONTROL_PANEL = BLOCKS.register("timemachinecontrolpanel", () -> new TimeMachineControlPanelBlock(Block.Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(3f).lightValue(0 / 16).variableOpacity().harvestTool(ToolType.PICKAXE).harvestLevel(2)));
    public static final RegistryObject<Block> HEAVY_BLOCK = BLOCKS.register("heavyblock", () -> new Block(Block.Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(7f).lightValue(0/16).variableOpacity().harvestTool(ToolType.PICKAXE).harvestLevel(3)));
    public static final RegistryObject<Block> REINFORCED_HEAVY_BLOCK = BLOCKS.register("reinforcedheavyblock", () -> new Block(Block.Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(10f).lightValue(10).variableOpacity().harvestTool(ToolType.PICKAXE).harvestLevel(3)));
    public static final RegistryObject<Block> TEMPORAL_EXPLOSION = BLOCKS.register("temporalexplosion", TemporalExplosionBlock::new);
    public static final RegistryObject<Block> TEMPORAL_CAULDRON = BLOCKS.register("temporalcauldron", TemporalCauldronBlock::new);
    public static final RegistryObject<Block> GUNPOWDER_WIRE = BLOCKS.register("gunpowderwire", GunpowderWireBlock::new);
    public static final RegistryObject<Block> TIME_MACHINE_TRACKER = BLOCKS.register("timemachinetracker", () -> new TimeMachineUpgradeBlock(Block.Properties.create(Material.IRON).sound(SoundType.METAL).hardnessAndResistance(3f).lightValue(0 / 16).variableOpacity().harvestTool(ToolType.PICKAXE).harvestLevel(2)).setUpgrade(ModTimeMachines.Upgrades.TRACKER));
    public static final RegistryObject<Block> TIME_MACHINE_RECALLER = BLOCKS.register("timemachinerecaller", TimeMachineRecallerBlock::new);

    @OnlyIn(Dist.CLIENT)
    public static void registerBlockColor(ColorHandlerEvent.Block event) {
        event.getBlockColors().register((state, world, pos, num) -> GunpowderWireBlock.colorMultiplier(state.get(GunpowderWireBlock.BURNED)), ModBlocks.GUNPOWDER_WIRE.get());
    }

    public static void init() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TileEntities.TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static class TileEntities {

        private static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, TimeTravelMod.MODID);

        public static final RegistryObject<TileEntityType<TemporalCauldronTileEntity>> TEMPORAL_CAULDRON = TILE_ENTITIES.register("temporalcauldron", () -> TileEntityType.Builder.create(TemporalCauldronTileEntity::new, ModBlocks.TEMPORAL_CAULDRON.get()).build(null));
        public static final RegistryObject<TileEntityType<TMCooldownTileEntity>> TM_COOLDOWN = TILE_ENTITIES.register("tmcooldown", () -> TileEntityType.Builder.create(TMCooldownTileEntity::new, getAllCoreBlocks()).build(null));
        public static final RegistryObject<TileEntityType<TimeMachineRecallerTileEntity>> TM_RECALLER = TILE_ENTITIES.register("tmrecaller", () -> TileEntityType.Builder.create(TimeMachineRecallerTileEntity::new, ModBlocks.TIME_MACHINE_RECALLER.get()).build(null));

        private static Block[] getAllCoreBlocks() {
            HashSet<Block> blocks = new HashSet<Block>();
            for (Block block: ForgeRegistries.BLOCKS.getValues()) {
                if (block instanceof TimeMachineCoreBlock) blocks.add(block);
            }
            return blocks.toArray(new Block[]{});
        }
    }
}
