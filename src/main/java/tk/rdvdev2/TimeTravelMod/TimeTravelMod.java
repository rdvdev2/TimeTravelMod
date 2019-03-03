package tk.rdvdev2.TimeTravelMod;

import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tk.rdvdev2.TimeTravelMod.client.itemgroup.ItemGroupTTM;
import tk.rdvdev2.TimeTravelMod.common.dimension.oldwest.TimeLineOldWest;
import tk.rdvdev2.TimeTravelMod.proxy.ClientProxy;
import tk.rdvdev2.TimeTravelMod.proxy.CommonProxy;
import tk.rdvdev2.TimeTravelMod.proxy.IProxy;

import static tk.rdvdev2.TimeTravelMod.TimeTravelMod.MODID;

@Mod(MODID)
public class TimeTravelMod {

    public static final String MODID = "timetravelmod";

    public static IProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public static final Logger logger = LogManager.getLogger();

    public static ItemGroup tabTTM = new ItemGroupTTM();

    public TimeTravelMod() {
        // Register FMLCommonSetupEvent
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        // Register FMLDedicatedServerSetupEvent
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::serverSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        TimeTravelMod.logger.info("Time Travel Mod is in common setup state.");
        ModPacketHandler.init();
        DimensionManager.registerDimensionInternal(20, ModTimeLines.OLD_WEST, TimeLineOldWest.modDimension, null);
        //DimensionManager.registerDimension( ModTimeLines.OLD_WEST, TimeLineOldWest.modDimension, null);
        /*ForgeRegistries.BIOMES.forEach((biome ->
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createCompositeFeature(
                        Feature.MINABLE,
                        new MinableConfig(MinableConfig.IS_ROCK, ModBlocks.timeCrystalOre.getDefaultState(), 4),
                        Biome.COUNT_RANGE,
                        new CountRangeConfig(1, 0, 0, 16)
                ))));*/
    }

    private void serverSetup(FMLDedicatedServerSetupEvent event) {

    }
}
