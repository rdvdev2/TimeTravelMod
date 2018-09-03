package com.rdvdev2.TimeTravelMod.proxy;

import com.rdvdev2.TimeTravelMod.api.timemachine.ITimeMachine;
import com.rdvdev2.TimeTravelMod.client.gui.GuiTimeMachine;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class ClientProxy extends CommonProxy {
    @Override
    public void displayTMGuiScreen(EntityPlayer player, ITimeMachine tm, BlockPos pos, EnumFacing side) {
        Minecraft.getMinecraft().displayGuiScreen(new GuiTimeMachine(player, tm, pos, side));
    }
}
