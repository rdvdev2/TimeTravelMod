package tk.rdvdev2.TimeTravelMod.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import tk.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;
import tk.rdvdev2.TimeTravelMod.common.networking.OpenTMGUI;

public interface IProxy {
    void preInit(FMLPreInitializationEvent event); // FMLPreInitializationEvent
    void init (FMLInitializationEvent event); // FMLInitializationEvent
    void postInit (FMLPostInitializationEvent event); // FMLPostInitializationEvent
    void displayTMGuiScreen(EntityPlayer player, TimeMachine tm, BlockPos pos, EnumFacing side); // Show the TM GUI to the player
    void handleOpenTMGUI(OpenTMGUI message, MessageContext ctx); // Handle the OpenTMGUI network packet (Client Only)
}
