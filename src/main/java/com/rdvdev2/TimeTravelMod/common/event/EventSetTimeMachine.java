package com.rdvdev2.TimeTravelMod.common.event;

import com.rdvdev2.TimeTravelMod.common.registry.RegistryTimeMachines;
import com.rdvdev2.TimeTravelMod.api.timemachine.ITimeMachine;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.eventhandler.Event;

public class EventSetTimeMachine extends Event {

    private RegistryTimeMachines registry;

    public EventSetTimeMachine(RegistryTimeMachines registry) {
        super();
        this.registry = registry;
    }

    public ITimeMachine getTimeMachine(IBlockState block) {
        return registry.getCompatibleTimeMachine(block);
    }
}
