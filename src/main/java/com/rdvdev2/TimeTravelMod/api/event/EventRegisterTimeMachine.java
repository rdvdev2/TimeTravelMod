package com.rdvdev2.TimeTravelMod.api.event;

import com.rdvdev2.TimeTravelMod.api.timemachine.ITimeMachine;
import com.rdvdev2.TimeTravelMod.common.registry.RegistryTimeMachines;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * This event is triggered when the mod requires the Time Machines to be registered
 */
public class EventRegisterTimeMachine extends Event {

    private RegistryTimeMachines registry;

    public EventRegisterTimeMachine(RegistryTimeMachines registry) {
        super();
        this.registry = registry;
    }

    /**
     * Use this method to register every time machine
     * @param tm The time machine to be registered
     * @return Returns the time machine ID in the registry
     */
    public int register(ITimeMachine tm) {
        return registry.register(tm);
    }
}
