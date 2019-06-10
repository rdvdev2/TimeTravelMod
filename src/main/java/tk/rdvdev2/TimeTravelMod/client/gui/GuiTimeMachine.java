package tk.rdvdev2.TimeTravelMod.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.AbstractButton;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
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
public class GuiTimeMachine extends Screen {

    private Button[] buttons;
    private PlayerEntity player;
    private TimeMachine tm;
    private BlockPos pos;
    private Direction side;

    public GuiTimeMachine(PlayerEntity player, TimeMachine tm, BlockPos pos, Direction side){
        super(new StringTextComponent("TITLE PLACEHOLDER"));
        this.player = player;
        this.tm = tm.hook(player.world, pos, side);
        this.pos = pos;
        this.side = side;
    }

    @Override
    public void init() {
        TimeLine[] tls = iteratorToArray(ModRegistries.timeLinesRegistry.iterator(), TimeLine.class);
        Arrays.sort(tls, (o1, o2) -> o1.getMinTier() - o2.getMinTier());
        int buttoncount = tls.length;
        buttons = new Button[buttoncount];
        for(int id = 0; id < tls.length; id++) {
            addButton(new TimeLineButton(id, this.width / 2 -100, (this.height / (buttoncount+1))*(id+1), tls[id]));
        }
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean isPauseScreen() {
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

    protected class TimeLineButton extends AbstractButton { // TODO: Rewrite with AbstractButton

        TimeLine tl;

        TimeLineButton(int id, int x, int y, TimeLine tl) {
            super(id, x, y, I18n.format("gui.tm."+tl.getRegistryName().getPath()+".text"));
            this.tl = tl;
            this.active = tl.getMinTier() <= tm.getTier();
        }

        @Override
        public void onClick(double p_194829_1_, double p_194829_3_) {
            Minecraft.getInstance().displayGuiScreen(null);
            if (tl.getDimension() != player.dimension.getModType() && TimeLine.isValidTimeLine(player.world)) {
                ModPacketHandler.CHANNEL.sendToServer(new DimensionTpPKT(tl, tm, pos, side));
            } else {
                player.sendMessage(new TranslationTextComponent("gui.tm.error.text"));
            }
        }

        @Override
        public void onPress() {

        }
    }
}
