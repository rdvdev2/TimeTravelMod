package tk.rdvdev2.TimeTravelMod.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.registry.GameRegistry;
import tk.rdvdev2.TimeTravelMod.*;
import tk.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;
import tk.rdvdev2.TimeTravelMod.api.timemachine.block.PropertyTMReady;
import tk.rdvdev2.TimeTravelMod.common.event.EventSetTimeMachine;
import tk.rdvdev2.TimeTravelMod.common.networking.OpenTMGUI;
import tk.rdvdev2.TimeTravelMod.common.worldgen.OreGen;

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

    @Override
    public void handleOpenTMGUI(OpenTMGUI message, MessageContext ctx) {
        // Server is not going to handle this
        TimeTravelMod.logger.warn("Server is trying to handle the OpenTMGUI packet. That's weird!");
    }
}
