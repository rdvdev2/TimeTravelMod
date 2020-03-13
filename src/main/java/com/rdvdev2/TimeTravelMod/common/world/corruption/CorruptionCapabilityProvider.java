package com.rdvdev2.TimeTravelMod.common.world.corruption;

import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CorruptionCapabilityProvider implements ICapabilityProvider {

    @CapabilityInject(ICorruption.class)
    static Capability<ICorruption> CORRUPTION_CAPABILITY = null;
    private CorruptionHandler corruptionHandler;
    private World world;

    public CorruptionCapabilityProvider(World world) {
        this.world = world;
    }

    /**
     * Retrieves the Optional handler for the capability requested on the specific side.
     * The return value <strong>CAN</strong> be the same for multiple faces.
     * Modders are encouraged to cache this value, using the listener capabilities of the Optional to
     * be notified if the requested capability get lost.
     *
     * @param cap
     * @param side
     * @return The requested an optional holding the requested capability.
     */
    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CORRUPTION_CAPABILITY) {
            if (corruptionHandler == null) corruptionHandler = new CorruptionHandler(this.world);
            return LazyOptional.of(() -> (T) corruptionHandler);
        } else return LazyOptional.empty();
    }
}
