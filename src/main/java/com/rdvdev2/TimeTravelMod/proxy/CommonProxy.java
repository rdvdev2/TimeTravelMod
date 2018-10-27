package com.rdvdev2.TimeTravelMod.proxy;

import com.rdvdev2.TimeTravelMod.*;
import com.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;
import com.rdvdev2.TimeTravelMod.api.timemachine.block.PropertyTMReady;
import com.rdvdev2.TimeTravelMod.common.event.EventSetTimeMachine;
import com.rdvdev2.TimeTravelMod.common.networking.OpenTMGUI;
import com.rdvdev2.TimeTravelMod.common.worldgen.OreGen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

public class CommonProxy implements IProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        TimeTravelMod.logger = event.getModLog();
        TimeTravelMod.logger.info("Time Travel Mod is in preinit state.");
        PropertyTMReady.init();
        ModBlocks.init();
        ModItems.init();
        ModTimeMachines.init();
        ModBiomes.init();
        ModTimeLines.init();
        ModPacketHandler.init();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        TimeTravelMod.logger.info("Time Travel Mod is in init state.");
        GameRegistry.registerWorldGenerator(new OreGen(), 3);
        ModStructures.init();
        ModRecipes.init();
        EVENT_BUS.post(new EventSetTimeMachine());
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {

    }

    @Override
    public void displayTMGuiScreen(EntityPlayer player, TimeMachine tm, BlockPos pos, EnumFacing side) {
        ModPacketHandler.INSTANCE.sendTo(new OpenTMGUI(tm, pos, side), (EntityPlayerMP) player);
    }
}
