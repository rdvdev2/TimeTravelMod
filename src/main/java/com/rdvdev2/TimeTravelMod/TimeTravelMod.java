package com.rdvdev2.TimeTravelMod;

import com.rdvdev2.TimeTravelMod.client.creativetabs.tabTTM;
import com.rdvdev2.TimeTravelMod.common.worldgen.OreGen;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Logger;

@Mod(modid = "timetravelmod", useMetadata = true)
public class TimeTravelMod {

    public static Logger logger;

    public static CreativeTabs tabTTM = new tabTTM(CreativeTabs.getNextID());

    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
        logger = event.getModLog();
        logger.info("Time Travel Mod is in preinit state.");
        ModBlocks.init();
        ModItems.init();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        logger.info("Time Travel Mod is in init state.");
        GameRegistry.registerWorldGenerator(new OreGen(), 3);
        ModDimensions.init();
        ModRecipes.init();
    }
}
