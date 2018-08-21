package com.rdvdev2.TimeTravelMod.client.gui;

import com.rdvdev2.TimeTravelMod.ModDimensions;
import com.rdvdev2.TimeTravelMod.common.dimension.ITeleporterTimeMachine;
import com.rdvdev2.TimeTravelMod.util.TimeMachine;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiTimeMachine extends GuiScreen {

    private GuiButton ButtonPresent;
    private GuiButton ButtonOldWest;
    private int buttons = 2;
    private EntityPlayer player;
    private TimeMachine tm;
    private BlockPos pos;
    private EnumFacing side;

    public GuiTimeMachine(EntityPlayer player, TimeMachine tm, BlockPos pos, EnumFacing side){
        this.player = player;
        this.tm = tm;
        this.pos = pos;
        this.side = side;
    }

    @Override
    public void initGui() {
        ButtonPresent = new GuiButton(0, this.width / 2 -100, (this.height / (buttons+1))*1,"Present");
        ButtonOldWest = new GuiButton(1, this.width / 2 -100, (this.height / (buttons+1))*2,"Old West");
        this.buttonList.add(ButtonPresent);
        this.buttonList.add(ButtonOldWest);

        switch (tm.tier()){
            case 0:
                ButtonOldWest.enabled=false;
            case 1:
                break;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY,partialTicks);
        Mouse.setGrabbed(false);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void actionPerformed(GuiButton button) throws IOException {
        this.mc.displayGuiScreen(null);
        int id;
        if (button == ButtonPresent) {
            id = 0;
        }
        else if (button == ButtonOldWest) {
            id = ModDimensions.OldWestId;
        } else {
            id = -1;
        }

        if (id != player.dimension && player.dimension != 1 && player.dimension != -1) {
            if (player instanceof EntityPlayerMP) {
                EntityPlayerMP mp = (EntityPlayerMP) player;
                mp.getServer().getPlayerList().transferPlayerToDimension(mp, id, new ITeleporterTimeMachine(mp.getServer().getWorld(id), mp.getServer().getWorld(mp.dimension), tm, pos, side));
            }
        } else {
            this.sendChatMessage("You can't travel to your travel line or from nether or end!", false);
        }
    }
}
