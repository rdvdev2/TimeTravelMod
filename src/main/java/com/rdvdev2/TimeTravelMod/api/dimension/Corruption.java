package com.rdvdev2.TimeTravelMod.api.dimension;

/**
 * This interface defines a way to get and modify the corruption level of any {@link TimeLine}. You should never implement it by yourself, but obtain it using {@link TimeLine#getCorruption()}.
 */
public interface Corruption {

    /**
     * Increases the corruption level by the specified amount.
     * @param amount Amount of corruption to add.
     * @return The new corruption level.
     */
    int increaseCorruptionLevel(int amount);

    /**
     * Decreases the corruption level by the specified amount.
     * @param amount Amount of corruption to subtract.
     * @return The new corruption level.
     */
    int decreaseCorruptionLevel(int amount);

    /**
     * Gets the actual corruption level.
     * @return The corruption level.
     */
    int getCorruptionLevel();
}
