package com.rdvdev2.TimeTravelMod.common.registry;

import com.rdvdev2.TimeTravelMod.ModRegistries;
import com.rdvdev2.TimeTravelMod.TimeTravelMod;
import com.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;
import com.rdvdev2.TimeTravelMod.common.event.EventSetTimeMachine;
import net.minecraft.block.state.IBlockState;

import java.util.Iterator;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

public class RegistryTimeMachines {

    public RegistryTimeMachines(){}

    public void link() {
        EVENT_BUS.post(new EventSetTimeMachine(this));
    }

    public TimeMachine getCompatibleTimeMachine(IBlockState block) {
        Iterator<TimeMachine> tms = ModRegistries.timeMachinesRegistry.iterator();
        while(tms.hasNext()) {
            TimeMachine tm = tms.next();
            IBlockState[] blocks = tm.getBlocks();
            for (int j = 0; j < blocks.length; j++) {
                if (blocks[j] == block) {
                    TimeTravelMod.logger.info("Found an appropiate Time Machine");
                    return tm;
                }
            }
        }
        TimeTravelMod.logger.info("This is not registered :( "+block.toString());
        return null;
    }
}
