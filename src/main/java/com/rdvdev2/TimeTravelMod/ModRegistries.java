package com.rdvdev2.TimeTravelMod;

import com.rdvdev2.TimeTravelMod.api.dimension.TimeLine;
import com.rdvdev2.TimeTravelMod.common.registry.RegistryTimeLines;
import com.rdvdev2.TimeTravelMod.common.registry.RegistryTimeMachines;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryInternal;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryManager;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid="timetravelmod")
public class ModRegistries {

    public static RegistryTimeMachines timeMachines;
    public static RegistryTimeLines timeLines;
    public static IForgeRegistry<TimeLine> timeLinesRegistry;

    public static void init() {
        timeMachines = new RegistryTimeMachines();
        timeLines = new RegistryTimeLines();
    }

    public static void start() {
        timeMachines.start();
    }

    @SubscribeEvent
    public static void addRegistries(RegistryEvent.NewRegistry event) {
        timeLinesRegistry = new RegistryBuilder<TimeLine>()
                .setType(TimeLine.class)
                .setName(new ResourceLocation("timetravelmod:timelines"))
                .add(new IForgeRegistry.AddCallback<TimeLine>() {
                    @Override
                    public void onAdd(IForgeRegistryInternal<TimeLine> owner, RegistryManager stage, int id, TimeLine obj, @Nullable TimeLine oldObj) {
                        if (!ArrayUtils.contains(DimensionManager.getDimensions(obj.getDimensionType()), obj.getDimId()))
                            DimensionManager.registerDimension(obj.getDimId(), obj.getDimensionType());
                    }
                })
                .create();
    }
}
