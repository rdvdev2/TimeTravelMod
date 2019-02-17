package tk.rdvdev2.TimeTravelMod;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import tk.rdvdev2.TimeTravelMod.api.dimension.TimeLine;
import tk.rdvdev2.TimeTravelMod.common.dimension.oldwest.TimeLineOldWest;

@Mod.EventBusSubscriber(modid="timetravelmod")
public class ModTimeLines {

    public static TimeLine oldWest = new TimeLineOldWest();

    @SubscribeEvent
    public static void registerTimeLines(RegistryEvent.Register<TimeLine> event) {
        event.getRegistry().registerAll(oldWest.setRegistryName("oldwest"));
    }
}
