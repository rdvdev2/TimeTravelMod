package com.rdvdev2.TimeTravelMod;

import com.rdvdev2.TimeTravelMod.common.event.EventRegisterTimeMachine;
import com.rdvdev2.TimeTravelMod.util.TimeMachine;
import com.rdvdev2.TimeTravelMod.util.TimeMachineTier1;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid="timetravelmod")
public class ModTimeMachines {

    public static TimeMachine timeMachineTier1;

    public static void init() {
        timeMachineTier1 = new TimeMachineTier1();
    }
    @SubscribeEvent
    public static void registerTimeMachines(EventRegisterTimeMachine event) {
        event.register(timeMachineTier1, ModBlocks.timeMachineControlPanel.getDefaultState());
    }
}
