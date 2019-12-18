package tk.rdvdev2.TimeTravelMod.api.dimension;

import net.minecraftforge.common.ModDimension;
import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * This interface defines a time line. It's the dimension's world provider.
 * To get an instance use the provided method, never implement the interface by yourself.
 * It must be registered in the time line registry instead of the dimension manager.
 */
public interface TimeLine extends IForgeRegistryEntry<TimeLine> {

    /**
     * Gets the minimum Time Machine tier required to travel to this time line
     * @return The minimum tier
     */
    int getMinTier();

    /**
     * Gets the special ModDimension object that should be registered into the game registry
     * @return The hooked ModDimension object
     */
    ModDimension getModDimension();

    /**
     * Support method for registration
     * @see net.minecraftforge.registries.ForgeRegistryEntry#setRegistryName(String, String)
     */
    TimeLine setRegistryName(String name);

    /**
     * Support method for registration
     * @see net.minecraftforge.registries.ForgeRegistryEntry#setRegistryName(String, String)
     */
    TimeLine setRegistryName(String modID, String name);

    /**
     * Creates a new Time Line
     * @param minTier The minimum tier a Time Machine needs to get to the Time Line
     * @param modDimension The ModDimension object that you would normally register with the DimensionManager
     * @return The new Time Line
     */
    static TimeLine getNew(int minTier, ModDimension modDimension) {
        return new tk.rdvdev2.TimeTravelMod.common.world.dimension.TimeLine(minTier, modDimension);
    }
}
