package com.rdvdev2.TimeTravelMod.common.world.dimension;

import com.rdvdev2.TimeTravelMod.common.event.WorldCorruptionChangedEvent;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class Corruption implements com.rdvdev2.TimeTravelMod.api.dimension.Corruption {

    private int corruptionLevel;
    private World world;

    @Deprecated
    public Corruption() { // Only for registering, NEVER use it
        this(null);
    }

    public Corruption(World world) {
        this.world = world;
        this.corruptionLevel = 0;
    }

    @Override
    public int increaseCorruptionLevel(int amount) {
        corruptionLevel += amount;
        MinecraftForge.EVENT_BUS.post(new WorldCorruptionChangedEvent(world));
        return corruptionLevel;
    }

    @Override
    public int decreaseCorruptionLevel(int amount) {
        corruptionLevel -= amount;
        MinecraftForge.EVENT_BUS.post(new WorldCorruptionChangedEvent(world));
        return corruptionLevel;
    }

    @Override
    public int getCorruptionLevel() {
        return corruptionLevel;
    }

    public void setCorruptionLevel(int level) {
        corruptionLevel = level;
    }
}
