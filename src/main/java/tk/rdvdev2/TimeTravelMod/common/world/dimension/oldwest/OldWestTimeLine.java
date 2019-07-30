package tk.rdvdev2.TimeTravelMod.common.world.dimension.oldwest;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.ModDimension;
import tk.rdvdev2.TimeTravelMod.api.dimension.TimeLine;

import java.util.function.BiFunction;

public class OldWestTimeLine extends TimeLine {

    private static ModDimension modDimension = new ModDimension() {
        @Override
        public BiFunction<World, DimensionType, ? extends Dimension> getFactory() {
            return OldWestDimension::new;
        }
    }.setRegistryName(new ResourceLocation("timetravelmod:oldwest"));

    public OldWestTimeLine() {
        super(1, modDimension);
    }
}
