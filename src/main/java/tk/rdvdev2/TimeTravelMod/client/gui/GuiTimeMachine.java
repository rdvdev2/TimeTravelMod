package tk.rdvdev2.TimeTravelMod.client.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import tk.rdvdev2.TimeTravelMod.ModPacketHandler;
import tk.rdvdev2.TimeTravelMod.ModRegistries;
import tk.rdvdev2.TimeTravelMod.api.dimension.TimeLine;
import tk.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;
import tk.rdvdev2.TimeTravelMod.common.networking.DimensionTpPKT;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;

@OnlyIn(Dist.CLIENT)
public class GuiTimeMachine extends GuiScreen {

    TimeLine[] tls;
    private GuiButton[] buttons;
    private EntityPlayer player;
    private TimeMachine tm;
    private BlockPos pos;
    private EnumFacing side;

    public GuiTimeMachine(EntityPlayer player, TimeMachine tm, BlockPos pos, EnumFacing side){
        this.player = player;
        this.tm = tm.hook(player.world, pos, side);
        this.pos = pos;
        this.side = side;
    }

    @Override
    public void initGui() {
        tls = iteratorToArray(ModRegistries.timeLinesRegistry.iterator(), TimeLine.class);
        TimeLine[] atls = ModRegistries.timeLinesRegistry.getSlaveMap(ModRegistries.TIERTOTIMELINE, TimeLine[][].class)[tm.getTier()];
        int buttoncount = tls.length+1;
        buttons = new GuiButton[buttoncount];
        buttons[0] = new GuiButton(0, this.width / 2 -100, (this.height / (buttoncount+1)), I18n.format("gui.tm.present.text"));
        addButton(buttons[0]);
        for(int i = 1; i < tls.length+1; i++) {
            buttons[i] = new GuiButton(i, this.width / 2 -100, (this.height / (buttoncount+1)*(i+1)), I18n.format("gui.tm."+tls[i-1].getRegistryName()+".text"));
            buttons[i].enabled=false;
            for (TimeLine tl:atls) {
                if (tl.getDimId() == tls[i-1].getDimId()) {
                    buttons[i].enabled=true;
                }
            }
            addButton(buttons[i]);
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.render(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

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
        if (id != player.dimension.getId() && player.dimension.getId() != 1 && player.dimension.getId() != -1) {
            ModPacketHandler.CHANNEL.sendToServer(new DimensionTpPKT(id, tm, pos, side));
        } else {
            this.player.sendMessage(new TextComponentTranslation("gui.tm.error.text"));
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T[] iteratorToArray(Iterator<T> iterator, Class<T> clazz) {
        T[] array = (T[]) Array.newInstance(clazz, 0);
        while (iterator.hasNext()) {
            int i = array.length;
            array = Arrays.copyOf(array, i+1);
            array[i] = iterator.next();
        }
        return array;
    }

    protected class GuiButton extends net.minecraft.client.gui.GuiButton {
        public GuiButton(int p_i1020_1_, int p_i1020_2_, int p_i1020_3_, String p_i1020_4_) {
            super(p_i1020_1_, p_i1020_2_, p_i1020_3_, p_i1020_4_);
        }

        public GuiButton(int p_i46323_1_, int p_i46323_2_, int p_i46323_3_, int p_i46323_4_, int p_i46323_5_, String p_i46323_6_) {
            super(p_i46323_1_, p_i46323_2_, p_i46323_3_, p_i46323_4_, p_i46323_5_, p_i46323_6_);
        }

        @Override
        public void onClick(double p_194829_1_, double p_194829_3_) {
            super.onClick(p_194829_1_, p_194829_3_);
            try {
                actionPerformed(this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
