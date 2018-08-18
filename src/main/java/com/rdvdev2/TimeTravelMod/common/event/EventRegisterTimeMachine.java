package com.rdvdev2.TimeTravelMod.common.event;

import com.rdvdev2.TimeTravelMod.common.registry.RegistryTimeMachines;
import com.rdvdev2.TimeTravelMod.util.TimeMachine;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.eventhandler.Event;

public class EventRegisterTimeMachine extends Event {

    private RegistryTimeMachines registry;

    public EventRegisterTimeMachine(RegistryTimeMachines registry) {
        super();
        this.registry = registry;
    }

    public int register(TimeMachine tm) {
        return registry.register(tm);
    }
}
