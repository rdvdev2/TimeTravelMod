package tk.rdvdev2.TimeTravelMod.api.dimension;

import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.registries.ForgeRegistryEntry;
import tk.rdvdev2.TimeTravelMod.ModRegistries;

import java.util.Iterator;

/**
 * This class defines a time line. It's the dimension's world provider. It must be registered in the time line registry instead of the dimension manager.
 */
public abstract class TimeLine extends ForgeRegistryEntry<TimeLine> {

    private int minTier;
    private ModDimension dimension;
    private TimeLine registeredInstance = null;

    /**
     * Gets the minimum Time Machine tier required to travel to this time line
     * @return The minimum tier
     */
    public int getMinTier() {
        return minTier;
    }

    public ModDimension getDimension() {
        return dimension;
    }

    /**
     * Constructor of the Time Line
     * @param minTier The desired minimum tier
     */
    public TimeLine(ModDimension dimension, int minTier) {
        super();
        this.dimension = dimension;
        this.minTier = minTier;
    }

    public static boolean isValidTimeLine(World world) {
        Iterator<TimeLine> iterator = ModRegistries.timeLinesRegistry.iterator();
        while (iterator.hasNext()) {
            TimeLine tl = iterator.next();
            if (tl.getDimension() == world.getDimension().getType().getModType() || world.getDimension().getType() == DimensionType.field_223227_a_) return true;
        }
        return false;
    }
}
