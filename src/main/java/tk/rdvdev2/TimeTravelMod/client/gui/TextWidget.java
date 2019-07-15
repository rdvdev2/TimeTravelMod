package tk.rdvdev2.TimeTravelMod.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;

public class TextWidget extends Widget {

    private final boolean centered;
    private final String hexColor;

    public TextWidget(int x, int y, String text, boolean centered) {
        this(x, y, text, centered, "E0E0E0");
    }

    public TextWidget(int x, int y, String text, boolean centered, String hexColor) {
        super(x, y, text);
        setMessage(text);
        this.centered = centered;
        this.hexColor = hexColor;
    }

    @Override
    public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
        if (centered) this.drawCenteredString(Minecraft.getInstance().fontRenderer, getMessage(), x, y, Integer.parseInt("FFAA00", 16));
        else this.drawString(Minecraft.getInstance().fontRenderer, getMessage(), x, y, Integer.parseInt("FFAA00", 16));
    }
}
