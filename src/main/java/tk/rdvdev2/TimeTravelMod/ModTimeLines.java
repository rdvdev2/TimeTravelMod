package tk.rdvdev2.TimeTravelMod;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.RegisterDimensionsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import tk.rdvdev2.TimeTravelMod.api.dimension.TimeLine;
import tk.rdvdev2.TimeTravelMod.common.world.dimension.PresentTimeLine;
import tk.rdvdev2.TimeTravelMod.common.world.dimension.oldwest.OldWestTimeLine;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class ModTimeLines {

    public static TimeLine present = new PresentTimeLine();
    public static final ResourceLocation PRESENT = new ResourceLocation("timetravelmod:present");
    public static TimeLine oldWest = new OldWestTimeLine();
    public static final ResourceLocation OLD_WEST = new ResourceLocation("timetravelmod:oldwest");

    @SubscribeEvent
    public static void registerTimeLines(RegistryEvent.Register<TimeLine> event) {
        event.getRegistry().registerAll(
                present.setRegistryName(PRESENT),
                oldWest.setRegistryName(OLD_WEST)
        );
    }

    @SubscribeEvent
    public static void registerModDimensions(RegistryEvent.Register<ModDimension> event) {
        event.getRegistry().registerAll(
                oldWest.getModDimension()
        );
    }

    public static void registerDimension (RegisterDimensionsEvent event) {
        DimensionManager.registerDimension(OLD_WEST, oldWest.getModDimension(), null, true);
    }
}
