package tk.rdvdev2.TimeTravelMod;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import tk.rdvdev2.TimeTravelMod.api.dimension.TimeLine;
import tk.rdvdev2.TimeTravelMod.common.entity.origintl.IOriginTL;
import tk.rdvdev2.TimeTravelMod.common.entity.origintl.OriginHandler;
import tk.rdvdev2.TimeTravelMod.common.entity.origintl.OriginTLCapabilityProvider;
import tk.rdvdev2.TimeTravelMod.common.world.corruption.CorruptionCapabilityProvider;
import tk.rdvdev2.TimeTravelMod.common.world.corruption.CorruptionHandler;
import tk.rdvdev2.TimeTravelMod.common.world.corruption.ICorruption;

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

        CapabilityManager.INSTANCE.register(IOriginTL.class, new Capability.IStorage<IOriginTL>() {
            @Nullable
            @Override
            public INBT writeNBT(Capability<IOriginTL> capability, IOriginTL instance, Direction side) {
                return new StringNBT(instance.getOriginTimeLine().getRegistryName().toString());
            }

            @Override
            public void readNBT(Capability<IOriginTL> capability, IOriginTL instance, Direction side, INBT nbt) {
                instance.setOriginTimeLine(ModRegistries.TIME_LINES.getValue(ResourceLocation.tryCreate(((StringNBT) nbt).getString())));
            }
        }, OriginHandler::new);
    }

    @SubscribeEvent
    public static void attachToWorld(AttachCapabilitiesEvent<World> event) {
        if (!TimeLine.isValidTimeLine(event.getObject())) return;
        event.addCapability(new ResourceLocation(MODID, "corruption"), new CorruptionCapabilityProvider(event.getObject()));
    }

    @SubscribeEvent
    public static void attachToEntity(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof LivingEntity) {
            event.addCapability(new ResourceLocation(MODID, "origin_tl"), new OriginTLCapabilityProvider());
        }
    }
}
