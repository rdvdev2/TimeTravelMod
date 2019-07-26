package tk.rdvdev2.TimeTravelMod.api.dimension;

import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.registries.ForgeRegistryEntry;
import tk.rdvdev2.TimeTravelMod.ModRegistries;

import java.util.Iterator;
import java.util.function.BiFunction;

/**
 * This class defines a time line. It's the dimension's world provider. It must be registered in the time line registry instead of the dimension manager.
 */
public abstract class TimeLine extends ForgeRegistryEntry<TimeLine> {

    private int minTier;
    private DimensionType dimension;

    /**
     * Gets the minimum Time Machine tier required to travel to this time line
     * @return The minimum tier
     */
    public int getMinTier() {
        return minTier;
    }

    // Time Travel Mod internal: use hook() instead
    public void setDimension(DimensionType dimension) {
        this.dimension = dimension;
    }

    public DimensionType getDimension() {
        return dimension;
    }

    /**
     * Constructor of the Time Line
     * @param minTier The desired minimum tier
     */
    public TimeLine(int minTier) {
        this.minTier = minTier;
    }

    public BiFunction<World, DimensionType, ? extends Dimension> hook(BiFunction<World, DimensionType, ? extends Dimension> originial) {
        return (world, dimensionType) -> {
            this.dimension = dimensionType;
            return originial.apply(world, dimensionType);
        };
    }

    public static boolean isValidTimeLine(World world) {
        Iterator<TimeLine> iterator = ModRegistries.timeLinesRegistry.iterator();
        while (iterator.hasNext()) {
            TimeLine tl = iterator.next();
            if (tl.getDimension() == world.getDimension().getType()) return true;
        }
        return false;
    }
}
