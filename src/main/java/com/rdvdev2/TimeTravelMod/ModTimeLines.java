package com.rdvdev2.TimeTravelMod;

import com.rdvdev2.TimeTravelMod.api.dimension.TimeLine;
import com.rdvdev2.TimeTravelMod.common.world.dimension.oldwest.OldWestDimension;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.RegisterDimensionsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.BiFunction;

import static com.rdvdev2.TimeTravelMod.TimeTravelMod.MODID;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class ModTimeLines {

    public static final TimeLine PRESENT = TimeLine.getNew(0, null).setRegistryName(MODID, "present");
    public static final TimeLine OLDWEST = TimeLine.getNew(1, new ModDimension() {
        @Override
        public BiFunction<World, DimensionType, ? extends Dimension> getFactory() {
            return OldWestDimension::new;
        }
    }.setRegistryName(new ResourceLocation(MODID, "oldwest"))).setRegistryName(MODID, "oldwest");

    @SubscribeEvent
    public static void registerTimeLines(RegistryEvent.Register<TimeLine> event) {
        event.getRegistry().registerAll(
                PRESENT,
                OLDWEST
        );
    }

    @SubscribeEvent
    public static void registerModDimensions(RegistryEvent.Register<ModDimension> event) {
        event.getRegistry().registerAll(
                OLDWEST.getModDimension()
        );
    }

    public static void registerDimension (RegisterDimensionsEvent event) {
        if (DimensionType.byName(OLDWEST.getRegistryName()) == null) DimensionManager.registerDimension(OLDWEST.getRegistryName(), OLDWEST.getModDimension(), null, true);
    }
}
