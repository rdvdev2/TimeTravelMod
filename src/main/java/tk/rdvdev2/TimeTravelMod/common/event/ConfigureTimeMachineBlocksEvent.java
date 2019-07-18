package tk.rdvdev2.TimeTravelMod.common.event;

import net.minecraft.block.BlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.Event;
import tk.rdvdev2.TimeTravelMod.ModRegistries;
import tk.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;
import tk.rdvdev2.TimeTravelMod.api.timemachine.block.AbstractTimeMachineComponentBlock;
import tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.TimeMachineUpgrade;

import java.util.HashMap;

public class ConfigureTimeMachineBlocksEvent extends Event {

    public TimeMachine getTimeMachine(BlockState state) {
        return ModRegistries.timeMachinesRegistry.getValue(((HashMap<BlockState, ResourceLocation>) ModRegistries.timeMachinesRegistry.getSlaveMap(ModRegistries.CONTROLLERTOTM, HashMap.class)).get(state));
    }

    public HashMap<TimeMachineUpgrade, AbstractTimeMachineComponentBlock[]> getUpgrades() {
        return ModRegistries.upgradesRegistry.getSlaveMap(ModRegistries.UPGRADETOBLOCK, HashMap.class);
    }

    public ConfigureTimeMachineBlocksEvent() {
        super();
    }
}
