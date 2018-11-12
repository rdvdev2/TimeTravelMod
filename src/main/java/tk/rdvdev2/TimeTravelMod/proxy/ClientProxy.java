package tk.rdvdev2.TimeTravelMod.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import tk.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;
import tk.rdvdev2.TimeTravelMod.client.gui.GuiTimeMachine;

public class ClientProxy extends CommonProxy {
    @Override
    public void displayTMGuiScreen(EntityPlayer player, TimeMachine tm, BlockPos pos, EnumFacing side) {
        Minecraft.getMinecraft().displayGuiScreen(new GuiTimeMachine(player, tm, pos, side));
    }
}
