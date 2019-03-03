package tk.rdvdev2.TimeTravelMod.client.gui;

import net.minecraft.client.gui.GuiButton;
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

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;

@OnlyIn(Dist.CLIENT)
public class GuiTimeMachine extends GuiScreen { // TODO: Fix GUI approaching custom GuiButton class

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
        TimeLine[] tls = iteratorToArray(ModRegistries.timeLinesRegistry.iterator(), TimeLine.class);
        Arrays.sort(tls, (o1, o2) -> o1.getMinTier() - o2.getMinTier());
        int buttoncount = tls.length;
        buttons = new GuiButton[buttoncount];
        for(int id = 0; id < tls.length; id++) {
            addButton(new TimeLineButton(id, this.width / 2 -100, (this.height / (buttoncount+1))*(id+1), tls[id]));
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

    protected class TimeLineButton extends net.minecraft.client.gui.GuiButton {

        TimeLine tl;

        TimeLineButton(int id, int x, int y, TimeLine tl) {
            super(id, x, y, I18n.format("gui.tm."+tl.getRegistryName().getPath()+".text"));
            this.tl = tl;
            this.enabled = tl.getMinTier() <= tm.getTier();
        }

        @Override
        public void onClick(double p_194829_1_, double p_194829_3_) {
            mc.displayGuiScreen(null);
            if (tl.getDimension() != player.dimension && TimeLine.isValidTimeLine(player.world)) {
                ModPacketHandler.CHANNEL.sendToServer(new DimensionTpPKT(tl, tm, pos, side));
            } else {
                player.sendMessage(new TextComponentTranslation("gui.tm.error.text"));
            }
        }
    }
}
