package com.rdvdev2.TimeTravelMod;

import com.rdvdev2.TimeTravelMod.api.dimension.TimeLine;
import com.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;
import com.rdvdev2.TimeTravelMod.common.registry.TimeLinesCallbacks;
import com.rdvdev2.TimeTravelMod.common.registry.TimeMachinesCallbacks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

@Mod.EventBusSubscriber(modid="timetravelmod")
public class ModRegistries {

    public static IForgeRegistry<TimeMachine> timeMachinesRegistry;
    public static IForgeRegistry<TimeLine> timeLinesRegistry;

    @SubscribeEvent
    public static void addRegistries(RegistryEvent.NewRegistry event) {
        timeLinesRegistry = new RegistryBuilder<TimeLine>()
                .setType(TimeLine.class)
                .setName(new ResourceLocation("timetravelmod:timelines"))
                .addCallback(new TimeLinesCallbacks())
                .create();

        timeMachinesRegistry = new RegistryBuilder<TimeMachine>()
                .setType(TimeMachine.class)
                .setName(new ResourceLocation("timetravelmod:timemachines"))
                .addCallback(new TimeMachinesCallbacks())
                .create();
    }
}
