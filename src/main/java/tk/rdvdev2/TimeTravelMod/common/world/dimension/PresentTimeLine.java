package tk.rdvdev2.TimeTravelMod.common.world.dimension;

import net.minecraft.world.dimension.DimensionType;
import tk.rdvdev2.TimeTravelMod.api.dimension.TimeLine;

public class PresentTimeLine extends TimeLine {
    public PresentTimeLine() {
        super(DimensionType.OVERWORLD.getModType(), 0);
    }
}
