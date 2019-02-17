package tk.rdvdev2.TimeTravelMod;

import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tk.rdvdev2.TimeTravelMod.client.creativetab.tabTTM;
import tk.rdvdev2.TimeTravelMod.proxy.ClientProxy;
import tk.rdvdev2.TimeTravelMod.proxy.CommonProxy;
import tk.rdvdev2.TimeTravelMod.proxy.IProxy;

import static tk.rdvdev2.TimeTravelMod.TimeTravelMod.MODID;

@Mod(MODID)
public class TimeTravelMod {

    public static final String MODID = "timetravelmod";

    public static IProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public static final Logger logger = LogManager.getLogger();

    public static ItemGroup tabTTM = new tabTTM(ItemGroup.getGroupCountSafe());

    public TimeTravelMod() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        proxy.commonSetup(event);
    }
}
