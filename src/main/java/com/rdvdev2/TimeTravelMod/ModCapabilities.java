package com.rdvdev2.TimeTravelMod;

import com.rdvdev2.TimeTravelMod.api.dimension.Corruption;
import com.rdvdev2.TimeTravelMod.common.world.capability.CorruptionCapabilityProvider;
import com.rdvdev2.TimeTravelMod.common.world.dimension.TimeLine;
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

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.FORGE)
public class ModCapabilities {

    public static void register() {
        CapabilityManager.INSTANCE.register(Corruption.class, new Capability.IStorage<Corruption>() {
            @Nullable
            @Override
            public INBT writeNBT(Capability<Corruption> capability, Corruption instance, Direction side) {
                return IntNBT.valueOf(instance.getCorruptionLevel());
            }

            @Override
            public void readNBT(Capability<Corruption> capability, Corruption instance, Direction side, INBT nbt) {
                ((com.rdvdev2.TimeTravelMod.common.world.dimension.Corruption) instance).setCorruptionLevel(((IntNBT)nbt).getInt());
            }
        }, com.rdvdev2.TimeTravelMod.common.world.dimension.Corruption::new);
    }

    @SubscribeEvent
    public static void attachToWorld(AttachCapabilitiesEvent<World> event) {
        if (!TimeLine.isValidTimeLine(event.getObject())) return;
        event.addCapability(new ResourceLocation(TimeTravelMod.MODID, "corruption"), new CorruptionCapabilityProvider(event.getObject()));
    }
}
