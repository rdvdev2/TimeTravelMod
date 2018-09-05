package com.rdvdev2.TimeTravelMod;

import com.rdvdev2.TimeTravelMod.api.event.EventRegisterTimeMachine;
import com.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;
import com.rdvdev2.TimeTravelMod.common.timemachine.TimeMachineCreative;
import com.rdvdev2.TimeTravelMod.common.timemachine.TimeMachineTier1;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid="timetravelmod")
public class ModTimeMachines {

    public static TimeMachine timeMachineTier1;
    public static TimeMachine timeMachineCreative;

    public static void init() {
        timeMachineTier1 = new TimeMachineTier1();
        timeMachineCreative = new TimeMachineCreative();
    }
    @SubscribeEvent
    public static void registerTimeMachines(EventRegisterTimeMachine event) {
        event.register(timeMachineTier1);
        event.register(timeMachineCreative);
    }
}
