package tk.rdvdev2.TimeTravelMod;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tk.rdvdev2.TimeTravelMod.api.dimension.TimeLine;
import tk.rdvdev2.TimeTravelMod.common.dimension.oldwest.TimeLineOldWest;

@Mod.EventBusSubscriber(modid="timetravelmod")
public class ModTimeLines {

    public static TimeLine oldWest;

    public static void init() {
        oldWest = new TimeLineOldWest();
    }

    @SubscribeEvent
    public static void registerTimeLines(RegistryEvent.Register<TimeLine> event) {
        event.getRegistry().registerAll(oldWest.setRegistryName("oldwest"));
    }
}
