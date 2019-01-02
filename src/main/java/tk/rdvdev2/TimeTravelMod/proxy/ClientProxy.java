package tk.rdvdev2.TimeTravelMod.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import tk.rdvdev2.TimeTravelMod.TimeTravelMod;
import tk.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;
import tk.rdvdev2.TimeTravelMod.client.gui.GuiTimeMachine;
import tk.rdvdev2.TimeTravelMod.common.networking.OpenTMGUI;

public class ClientProxy extends CommonProxy {
    @Override
    public void displayTMGuiScreen(EntityPlayer player, TimeMachine tm, BlockPos pos, EnumFacing side) {
        Minecraft.getMinecraft().displayGuiScreen(new GuiTimeMachine(player, tm, pos, side));
    }

    @Override
    public void handleOpenTMGUI(OpenTMGUI message, MessageContext ctx) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        TimeTravelMod.proxy.displayTMGuiScreen(player, message.tm.hook(player.world, message.pos, message.side), message.pos, message.side);
    }
}
