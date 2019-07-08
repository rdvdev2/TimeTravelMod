package tk.rdvdev2.TimeTravelMod.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
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
import tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.IncompatibleTimeMachineHooksException;
import tk.rdvdev2.TimeTravelMod.common.networking.DimensionTpPKT;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

@OnlyIn(Dist.CLIENT)
public class TimeMachineScreen extends Screen {

    private PlayerEntity player;
    private TimeMachine tm;
    private BlockPos pos;
    private Direction side;

    public TimeMachineScreen(PlayerEntity player, TimeMachine tm, BlockPos pos, Direction side){
        super(new StringTextComponent("TITLE PLACEHOLDER"));
        this.player = player;
        try {
            this.tm = tm.hook(player.world, pos, side);
        } catch (IncompatibleTimeMachineHooksException e) {
            throw new RuntimeException("Time Machine GUI opened with invalid upgrade configuration");
        }
        this.pos = pos;
        this.side = side;
    }

    @Override
    public void init() {
        TimeLine[] tls = iteratorToArray(ModRegistries.timeLinesRegistry.iterator(), TimeLine.class);
        Arrays.sort(tls, Comparator.comparingInt(TimeLine::getMinTier));
        int buttoncount = tls.length;
        for(int id = 0; id < tls.length; id++) {
            addButton(new TimeLineButton(this.width / 2 -100, (this.height / (buttoncount+1))*(id+1), tls[id], this));
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

    protected class TimeLineButton extends Button {

        TimeLine tl;
        TimeMachineScreen screen;

        TimeLineButton(int x, int y, TimeLine tl, TimeMachineScreen screen) {
            super(x, y, 200, 20, I18n.format("gui.tm."+tl.getRegistryName().getPath()+".text"), TimeMachineScreen::clickHandler);
            this.screen = screen;
            this.tl = tl;
            this.active = tl.getMinTier() <= tm.getTier();
        }
    }

    private static void clickHandler(Button button) {
        TimeLineButton b = (TimeLineButton) button;
        Minecraft.getInstance().displayGuiScreen(null);
        if (b.tl.getDimension() != b.screen.player.dimension.getModType() && TimeLine.isValidTimeLine(b.screen.player.world)) {
            ModPacketHandler.CHANNEL.sendToServer(new DimensionTpPKT(b.tl, b.screen.tm, b.screen.pos, b.screen.side));
        } else {
            b.screen.player.sendMessage(new TranslationTextComponent("gui.tm.error.text"));
        }
    }
}
