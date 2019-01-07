package tk.rdvdev2.TimeTravelMod;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;
import tk.rdvdev2.TimeTravelMod.client.creativetab.tabTTM;
import tk.rdvdev2.TimeTravelMod.proxy.IProxy;

@Mod(modid = "timetravelmod", useMetadata = true)
public class TimeTravelMod {

    @SidedProxy(serverSide = "tk.rdvdev2.TimeTravelMod.proxy.CommonProxy", clientSide = "tk.rdvdev2.TimeTravelMod.proxy.ClientProxy")
    public static IProxy proxy;

    public static Logger logger;

    public static CreativeTabs tabTTM = new tabTTM(CreativeTabs.getNextID());

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }
}
