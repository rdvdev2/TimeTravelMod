package tk.rdvdev2.TimeTravelMod.common.dimension.oldwest;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.ModDimension;
import tk.rdvdev2.TimeTravelMod.api.dimension.TimeLine;

import java.util.function.Function;

public class TimeLineOldWest extends TimeLine {

    public static DimensionType type = new DimensionType(20, "_oldwest", "OLDWEST",DimensionOldWest::new)
            .setRegistryName(new ResourceLocation("timetravelmod:oldwest"));
    public static ModDimension modDimension = new ModDimension() {
        @Override
        public Function<DimensionType, ? extends net.minecraft.world.dimension.Dimension> getFactory() {
            return DimensionType::create;
        }
    }.setRegistryName(new ResourceLocation("timetravelmod:oldwest"));

    public TimeLineOldWest() {
        super(type, 1);
    }
}
