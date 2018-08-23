package com.rdvdev2.TimeTravelMod.common.worldgen.villages;

import net.minecraft.world.World;

public interface IVillage {

    // Returns it's frequency in a given world
    // A village will be generated every x chunks
    int frequency(World world);
}
