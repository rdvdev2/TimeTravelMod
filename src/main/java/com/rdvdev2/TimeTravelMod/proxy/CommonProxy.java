package com.rdvdev2.TimeTravelMod.proxy;

import com.rdvdev2.TimeTravelMod.*;
import com.rdvdev2.TimeTravelMod.common.worldgen.OreGen;
import com.rdvdev2.TimeTravelMod.api.timemachine.ITimeMachine;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy implements IProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        TimeTravelMod.logger = event.getModLog();
        TimeTravelMod.logger.info("Time Travel Mod is in preinit state.");
        ModBlocks.init();
        ModItems.init();
        ModRegistries.init();
        ModTimeMachines.init();
        ModTimeLines.init();
        ModPacketHandler.init();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        TimeTravelMod.logger.info("Time Travel Mod is in init state.");
        GameRegistry.registerWorldGenerator(new OreGen(), 3);
        ModRecipes.init();
        ModRegistries.start();
        ModRegistries.timeMachines.link();
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {

    }

    @Override
    public void displayTMGuiScreen(EntityPlayer player, ITimeMachine tm, BlockPos pos, EnumFacing side) {

    }
}
