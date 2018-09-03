package com.rdvdev2.TimeTravelMod;

import com.rdvdev2.TimeTravelMod.api.event.EventRegisterTimeLine;
import com.rdvdev2.TimeTravelMod.api.dimension.TimeLine;
import com.rdvdev2.TimeTravelMod.common.dimension.TimeLineOldWest;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid="timetravelmod")
public class ModTimeLines {

    public static TimeLine oldWest;

    public static void init() {
        oldWest = new TimeLineOldWest();
    }

    @SubscribeEvent
    public static void registerTimeLines(EventRegisterTimeLine event) {
        event.register(oldWest);
    }
}
