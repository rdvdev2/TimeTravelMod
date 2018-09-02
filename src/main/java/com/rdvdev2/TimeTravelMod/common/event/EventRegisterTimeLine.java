package com.rdvdev2.TimeTravelMod.common.event;

import com.rdvdev2.TimeTravelMod.common.dimension.TimeLine;
import com.rdvdev2.TimeTravelMod.common.registry.RegistryTimeLines;
import net.minecraftforge.fml.common.eventhandler.Event;

public class EventRegisterTimeLine extends Event {

    private RegistryTimeLines registry;

    public EventRegisterTimeLine(RegistryTimeLines registry) {
        super();
        this.registry = registry;
    }

    public int register(TimeLine tl) {
        return registry.register(tl);
    }
}
