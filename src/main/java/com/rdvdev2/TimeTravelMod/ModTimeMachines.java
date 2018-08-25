package com.rdvdev2.TimeTravelMod;

import com.rdvdev2.TimeTravelMod.api.event.EventRegisterTimeMachine;
import com.rdvdev2.TimeTravelMod.api.timemachine.ITimeMachine;
import com.rdvdev2.TimeTravelMod.common.timemachine.TimeMachineTier1;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid="timetravelmod")
public class ModTimeMachines {

    public static ITimeMachine timeMachineTier1;

    public static void init() {
        timeMachineTier1 = new TimeMachineTier1();
    }
    @SubscribeEvent
    public static void registerTimeMachines(EventRegisterTimeMachine event) {
        event.register(timeMachineTier1);
    }
}
