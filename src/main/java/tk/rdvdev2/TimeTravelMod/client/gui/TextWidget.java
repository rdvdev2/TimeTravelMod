package tk.rdvdev2.TimeTravelMod.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.math.MathHelper;

public class TextWidget extends Widget {

    private final boolean centered;

    public TextWidget(int x, int y, String text, boolean centered) {
        super(x, y, text);
        this.centered = centered;
    }

    @Override
    public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
        if (centered) this.drawCenteredString(Minecraft.getInstance().fontRenderer, getMessage(), x, y, getFGColor() | MathHelper.ceil(this.alpha * 255.0F) << 24);
        else this.drawString(Minecraft.getInstance().fontRenderer, getMessage(), x, y, getFGColor() | MathHelper.ceil(this.alpha * 255.0F) << 24);
    }
}
