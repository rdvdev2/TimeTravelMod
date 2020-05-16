package com.rdvdev2.TimeTravelMod.api.dimension;

import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * This interface defines a Time Line. It must be registered in the Time Line registry.
 * To get an instance use the provided method, never implement the interface by yourself.
 * The constructor takes a ModDimension object that can later be retrived by the {@link #getModDimension()} method to
 * register a hokked version with the {@link DimensionManager}. This is required to retrive the {@link DimensionType}.
 */
public interface TimeLine extends IForgeRegistryEntry<TimeLine> {

    /**
     * Gets the minimum Time Machine tier required to travel to this Time Line
     * @return The minimum tier
     */
    int getMinTier();

    /**
     * Gets the hooked ModDimension object that should be registered with the {@link DimensionManager}
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
     * @param modDimension The {@link ModDimension} object that you would normally register with the {@link DimensionManager}
     * @return The new Time Line
     */
    static TimeLine getNew(int minTier, ModDimension modDimension) {
        return new com.rdvdev2.TimeTravelMod.common.world.dimension.TimeLine(minTier, modDimension);
    }
}
