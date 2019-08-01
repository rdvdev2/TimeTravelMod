package tk.rdvdev2.TimeTravelMod;

import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.RegisterDimensionsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import tk.rdvdev2.TimeTravelMod.api.dimension.TimeLine;
import tk.rdvdev2.TimeTravelMod.common.world.dimension.PresentTimeLine;
import tk.rdvdev2.TimeTravelMod.common.world.dimension.oldwest.OldWestTimeLine;

import static tk.rdvdev2.TimeTravelMod.TimeTravelMod.MODID;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class ModTimeLines {

    public static final TimeLine PRESENT = new PresentTimeLine().setRegistryName(MODID, "present");
    public static final TimeLine OLDWEST = new OldWestTimeLine().setRegistryName(MODID, "oldwest");

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
