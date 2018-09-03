package com.rdvdev2.TimeTravelMod.client.gui;

import com.rdvdev2.TimeTravelMod.ModPacketHandler;
import com.rdvdev2.TimeTravelMod.api.dimension.TimeLine;
import com.rdvdev2.TimeTravelMod.common.networking.DimensionTP;
import com.rdvdev2.TimeTravelMod.api.timemachine.ITimeMachine;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

import java.io.IOException;

import static com.rdvdev2.TimeTravelMod.ModRegistries.timeLines;

@SideOnly(Side.CLIENT)
public class GuiTimeMachine extends GuiScreen {

    /*
    private GuiButton ButtonPresent;
    private GuiButton ButtonOldWest;
    private int buttons = 2;
    */
    TimeLine[] tls;
    private GuiButton[] buttons;
    private EntityPlayer player;
    private ITimeMachine tm;
    private BlockPos pos;
    private EnumFacing side;

    public GuiTimeMachine(EntityPlayer player, ITimeMachine tm, BlockPos pos, EnumFacing side){
        this.player = player;
        this.tm = tm;
        this.pos = pos;
        this.side = side;
    }

    @Override
    public void initGui() {
        tls = timeLines.getTimeLines();
        TimeLine[] atls = timeLines.getAvailableTimeLines(tm.getTier());
        int buttoncount = tls.length+1;
        buttons = new GuiButton[buttoncount];
        buttons[0] = new GuiButton(0, this.width / 2 -100, (this.height / (buttoncount+1)), I18n.format("gui.tm.present.text"));
        this.buttonList.add(buttons[0]);
        for(int i = 1; i < tls.length+1; i++) {
            buttons[i] = new GuiButton(i, this.width / 2 -100, (this.height / (buttoncount+1)*(i+1)), I18n.format("gui.tm."+tls[i-1].getDimensionType().getName().toLowerCase()+".text"));
            buttons[i].enabled=false;
            for (TimeLine tl:atls) {
                if (tl.getDimId() == tls[i-1].getDimId()) {
                    buttons[i].enabled=true;
                }
            }
            this.buttonList.add(buttons[i]);
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
        if (button.id == 0) {
            id = 0;
        } else if (1 <= button.id && button.id <= tls.length) {
            id = tls[button.id - 1].getDimId();
        } else {
            id = -1;
        }
        if (id != player.dimension && player.dimension != 1 && player.dimension != -1) {
            ModPacketHandler.INSTANCE.sendToServer(new DimensionTP(id, tm, pos, side));
        } else {
            this.sendChatMessage(I18n.format("gui.tm.error.text"), false);
        }
    }
}
