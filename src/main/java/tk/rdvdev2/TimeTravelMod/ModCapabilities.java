package tk.rdvdev2.TimeTravelMod;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import tk.rdvdev2.TimeTravelMod.common.world.corruption.CorruptionCapabilityProvider;
import tk.rdvdev2.TimeTravelMod.common.world.corruption.CorruptionHandler;
import tk.rdvdev2.TimeTravelMod.common.world.corruption.ICorruption;
import tk.rdvdev2.TimeTravelMod.common.world.dimension.TimeLine;

import javax.annotation.Nullable;

import static tk.rdvdev2.TimeTravelMod.TimeTravelMod.MODID;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.FORGE)
public class ModCapabilities {

    public static void register() {
        CapabilityManager.INSTANCE.register(ICorruption.class, new Capability.IStorage<ICorruption>() {
            @Nullable
            @Override
            public INBT writeNBT(Capability<ICorruption> capability, ICorruption instance, Direction side) {
                return IntNBT.valueOf(instance.getCorruptionLevel());
            }

            @Override
            public void readNBT(Capability<ICorruption> capability, ICorruption instance, Direction side, INBT nbt) {
                instance.setCorruptionLevel(((IntNBT)nbt).getInt());
            }
        }, CorruptionHandler::new);
    }

    @SubscribeEvent
    public static void attachToWorld(AttachCapabilitiesEvent<World> event) {
        if (!TimeLine.isValidTimeLine(event.getObject())) return;
        event.addCapability(new ResourceLocation(MODID, "corruption"), new CorruptionCapabilityProvider(event.getObject()));
    }
}
