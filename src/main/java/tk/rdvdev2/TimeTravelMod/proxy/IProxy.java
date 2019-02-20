package tk.rdvdev2.TimeTravelMod.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.network.NetworkEvent;
import tk.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;
import tk.rdvdev2.TimeTravelMod.common.networking.OpenTmGuiPKT;

public interface IProxy {
    void serverSetup(FMLDedicatedServerSetupEvent event); // Server setup event
    void displayTMGuiScreen(EntityPlayer player, TimeMachine tm, BlockPos pos, EnumFacing side); // Show the TM GUI to the player
    void handleOpenTMGUI(OpenTmGuiPKT message, NetworkEvent.Context ctx); // Handle the OpenTmGuiPKT network packet (Client Only)
}
