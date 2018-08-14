package com.rdvdev2.TimeTravelMod.common.registry;

import com.rdvdev2.TimeTravelMod.TimeTravelMod;
import com.rdvdev2.TimeTravelMod.common.event.EventRegisterTimeMachine;
import com.rdvdev2.TimeTravelMod.util.TimeMachine;
import net.minecraft.block.state.IBlockState;

import java.util.Arrays;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

public class RegistryTimeMachines {

    private TimeMachine[] timeMachines = new TimeMachine[]{};
    private IBlockState[] compatibleControllers = new IBlockState[]{};

    public RegistryTimeMachines(){}

    public void start() {
        EVENT_BUS.post(new EventRegisterTimeMachine(this));
    }

    public int register(TimeMachine tm, IBlockState controller) {
        int id = timeMachines.length;
        timeMachines = Arrays.copyOf(timeMachines, id+1);
        compatibleControllers = Arrays.copyOf(compatibleControllers, id+1);
        timeMachines[id] = tm;
        compatibleControllers[id] = controller;
        TimeTravelMod.logger.info("Registered a Time Machine "+controller.toString());
        return id;
    }

    public TimeMachine getCompatibleTimeMachine(IBlockState controller) {
        for (int i = 0; i < compatibleControllers.length; i++) {
            if (compatibleControllers[i] == controller) {
                TimeTravelMod.logger.info("Found an appropiate Time Machine");
                return timeMachines[i];
            }
        }
        TimeTravelMod.logger.info("This is not registered :( "+controller.toString());
        return null;
    }
}
