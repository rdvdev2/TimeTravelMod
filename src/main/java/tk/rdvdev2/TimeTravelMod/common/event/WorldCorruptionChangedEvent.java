package tk.rdvdev2.TimeTravelMod.common.event;

import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.eventbus.api.Event;
import tk.rdvdev2.TimeTravelMod.common.world.corruption.ICorruption;

public class WorldCorruptionChangedEvent extends Event {

    @CapabilityInject(ICorruption.class)
    static Capability<ICorruption> CORRUPTION_CAPABILITY = null;

    private World world;

    public WorldCorruptionChangedEvent(World world) {
        this.world = world;
    }

    public World getWorld() {
        return this.world;
    }

    public int getCorruptionLevel() {
        return this.world.getCapability(CORRUPTION_CAPABILITY).orElseThrow(RuntimeException::new).getCorruptionLevel();
    }
}
