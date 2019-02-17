package tk.rdvdev2.TimeTravelMod;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;
import tk.rdvdev2.TimeTravelMod.api.timemachine.entity.TileEntityTMCooldown;
import tk.rdvdev2.TimeTravelMod.common.block.*;
import tk.rdvdev2.TimeTravelMod.common.block.tileentity.TileEntityTemporalCauldron;

@Mod.EventBusSubscriber(modid = "timetravelmod")
public class ModBlocks {

    public static Block timeCrystalOre = new BlockTimeCrystalOre();
    public static Block timeMachineBasicBlock = new BlockTimeMachineBasicBlock();
    public static Block timeMachineCore = new BlockTimeMachineCore();
    public static Block timeMachineControlPanel = new BlockTimeMachineControlPanel();
    public static Block heavyBlock = new BlockHeavyBlock();
    public static Block reinforcedHeavyBlock = new BlockReinforcedHeavyBlock();
    public static Block temporalExplosion = new BlockTemporalExplosion();
    public static Block temporalCauldron = new BlockTemporalCauldron();

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
}
