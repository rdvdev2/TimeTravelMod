package tk.rdvdev2.TimeTravelMod.api.dimension;

import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * This class defines a time line. It's the dimension's world provider. It must be registered in the time line registry instead of the dimension manager.
 */
public abstract class TimeLine implements IForgeRegistryEntry<TimeLine> {

    private int minTier;
    private ModDimension dimension;
    private TimeLine registeredInstance = null;

    public int getDimId() {
        return DimensionManager.getRegistry().getId(DimensionType.byName(this.getRegistryName())); // TODO: Check this works
    }

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
}
