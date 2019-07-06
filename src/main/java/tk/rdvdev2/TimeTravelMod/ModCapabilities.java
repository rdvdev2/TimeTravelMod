package tk.rdvdev2.TimeTravelMod;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import tk.rdvdev2.TimeTravelMod.common.world.corruption.CorruptionHandler;
import tk.rdvdev2.TimeTravelMod.common.world.corruption.ICorruption;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static tk.rdvdev2.TimeTravelMod.TimeTravelMod.MODID;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.FORGE)
public class ModCapabilities {

    public static void register() {
        CapabilityManager.INSTANCE.register(ICorruption.class, new Capability.IStorage<ICorruption>() {
            @Nullable
            @Override
            public INBT writeNBT(Capability<ICorruption> capability, ICorruption instance, Direction side) {
                return new IntNBT(instance.getCorruptionLevel());
            }

            @Override
            public void readNBT(Capability<ICorruption> capability, ICorruption instance, Direction side, INBT nbt) {
                instance.setCorruptionLevel(((IntNBT)nbt).getInt());
            }
        }, CorruptionHandler::new);
    }

    @SubscribeEvent
    public static void attachToWorld(AttachCapabilitiesEvent<World> event) {
        if (event.getObject().getDimension().getType() == DimensionType.NETHER || event.getObject().getDimension().getType() == DimensionType.THE_END) return;
        event.addCapability(new ResourceLocation(MODID, "corruption"), new ICapabilityProvider() {
            @Nonnull
            @Override
            public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
                return LazyOptional.of(() -> (T)new CorruptionHandler(event.getObject()));
            }
        });
    }
}
