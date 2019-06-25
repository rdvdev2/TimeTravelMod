package tk.rdvdev2.TimeTravelMod.common.world.corruption;

import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import tk.rdvdev2.TimeTravelMod.common.event.WorldCorruptionChangedEvent;

public class CorruptionHandler implements ICorruption {

    private int corruptionLevel;
    private World world;

    @Deprecated
    public CorruptionHandler() { // Only for registering, NEVER use it
        this(null);
    }

    public CorruptionHandler(World world) {
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

    @Deprecated
    @Override
    public void setCorruptionLevel(int level) {
        corruptionLevel = level;
    }
}
