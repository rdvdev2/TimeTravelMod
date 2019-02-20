package tk.rdvdev2.TimeTravelMod.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.network.NetworkEvent;
import tk.rdvdev2.TimeTravelMod.TimeTravelMod;
import tk.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;
import tk.rdvdev2.TimeTravelMod.client.gui.GuiTimeMachine;
import tk.rdvdev2.TimeTravelMod.common.networking.OpenTmGuiPKT;

public class ClientProxy extends CommonProxy {
    @Override
    public void serverSetup(FMLDedicatedServerSetupEvent event) {
        // Do nothing
    }

    @Override
    public void displayTMGuiScreen(EntityPlayer player, TimeMachine tm, BlockPos pos, EnumFacing side) {
        Minecraft.getInstance().addScheduledTask(()->Minecraft.getInstance().displayGuiScreen(new GuiTimeMachine(player, tm, pos, side)));
    }

    @Override
    public void handleOpenTMGUI(OpenTmGuiPKT message, NetworkEvent.Context ctx) {
        EntityPlayer player = Minecraft.getInstance().player;
        TimeTravelMod.proxy.displayTMGuiScreen(player, message.tm.hook(player.world, message.pos, message.side), message.pos, message.side);
    }
}
