package tk.rdvdev2.TimeTravelMod.common.entity.origintl;

import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class OriginTLCapabilityProvider implements ICapabilityProvider {

    @CapabilityInject(IOriginTL.class)
    private static Capability<IOriginTL> ORIGIN_CAPABILITY = null;
    private OriginHandler originHandler;

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
        if (cap == ORIGIN_CAPABILITY) {
            if (originHandler == null) originHandler = new OriginHandler();
            return LazyOptional.of(() -> (T) originHandler);
        } else return LazyOptional.empty();
    }
}
