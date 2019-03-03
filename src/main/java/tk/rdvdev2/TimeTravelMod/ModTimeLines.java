package tk.rdvdev2.TimeTravelMod;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import tk.rdvdev2.TimeTravelMod.api.dimension.TimeLine;
import tk.rdvdev2.TimeTravelMod.common.dimension.oldwest.TimeLineOldWest;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class ModTimeLines {

    public static TimeLine oldWest = new TimeLineOldWest();
    public static final ResourceLocation OLD_WEST = new ResourceLocation("timetravelmod:oldwest");

    @SubscribeEvent
    public static void registerTimeLines(RegistryEvent.Register<TimeLine> event) {
        event.getRegistry().registerAll(oldWest.setRegistryName(OLD_WEST));
    }

    @SubscribeEvent
    public static void registerDimensionTypes(RegistryEvent.Register<DimensionType> event) {
        event.getRegistry().registerAll(
                TimeLineOldWest.type.setRegistryName(OLD_WEST)
        );
    }

    @SubscribeEvent
    public static void registerModDimensions(RegistryEvent.Register<ModDimension> event) {
        event.getRegistry().registerAll(
                TimeLineOldWest.modDimension
        );
    }
}
