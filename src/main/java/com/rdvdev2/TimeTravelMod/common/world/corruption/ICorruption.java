package com.rdvdev2.TimeTravelMod.common.world.corruption;

public interface ICorruption {

    int increaseCorruptionLevel(int amount);

    int decreaseCorruptionLevel(int amount);

    int getCorruptionLevel();

    @Deprecated
    void setCorruptionLevel(int level); // Only for data restoring from nbt
}
