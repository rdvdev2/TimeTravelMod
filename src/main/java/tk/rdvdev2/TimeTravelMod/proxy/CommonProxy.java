package tk.rdvdev2.TimeTravelMod.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import tk.rdvdev2.TimeTravelMod.ModPacketHandler;
import tk.rdvdev2.TimeTravelMod.ModRegistries;
import tk.rdvdev2.TimeTravelMod.TimeTravelMod;
import tk.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;
import tk.rdvdev2.TimeTravelMod.common.networking.OpenTmGuiPKT;

public class CommonProxy implements IProxy {

    @Override
    public void serverSetup(FMLDedicatedServerSetupEvent event) {
        // Register Time Lines
        ModRegistries.timeLinesRegistry.forEach(tl -> {
            DimensionManager.registerDimension(tl.getRegistryName(), tl.getDimension(), null);
        });
    }

    @Override
    public void displayTMGuiScreen(EntityPlayer player, TimeMachine tm, BlockPos pos, EnumFacing side) {
        ModPacketHandler.CHANNEL.sendTo(new OpenTmGuiPKT(tm, pos, side), ((EntityPlayerMP)player).connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
    }

    @Override
    public void handleOpenTMGUI(OpenTmGuiPKT message, NetworkEvent.Context ctx) {
        // Server is not going to handle this
        TimeTravelMod.logger.warn("Server is trying to handle the OpenTmGuiPKT packet. That's weird!");
    }
}
