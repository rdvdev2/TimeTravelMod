package tk.rdvdev2.TimeTravelMod;

import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tk.rdvdev2.TimeTravelMod.client.itemgroup.ItemGroupTTM;
import tk.rdvdev2.TimeTravelMod.common.world.VanillaBiomesFeatures;
import tk.rdvdev2.TimeTravelMod.proxy.ClientProxy;
import tk.rdvdev2.TimeTravelMod.proxy.CommonProxy;
import tk.rdvdev2.TimeTravelMod.proxy.IProxy;

import static tk.rdvdev2.TimeTravelMod.ModConfig.clientSpec;
import static tk.rdvdev2.TimeTravelMod.ModConfig.commonSpec;
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
        // Register ColorHandlerEvent#Block
        MinecraftForge.EVENT_BUS.addListener(ModBlocks::registerBlockColor);
        // Register RegisterDimensionsEvent
        MinecraftForge.EVENT_BUS.addListener(ModTimeLines::registerDimension);
        // Register PlaySoundEvent
        MinecraftForge.EVENT_BUS.addListener(ModSounds::onPlaySound);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the client config
        ModLoadingContext.get().getActiveContainer().addConfig(new ModConfig(ModConfig.Type.CLIENT, clientSpec, ModLoadingContext.get().getActiveContainer()));
        // Register the common config
        ModLoadingContext.get().getActiveContainer().addConfig(new ModConfig(ModConfig.Type.COMMON, commonSpec, ModLoadingContext.get().getActiveContainer()));
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        TimeTravelMod.logger.info("Time Travel Mod is in common setup state.");
        ModPacketHandler.init();
        VanillaBiomesFeatures.register();
    }
}
