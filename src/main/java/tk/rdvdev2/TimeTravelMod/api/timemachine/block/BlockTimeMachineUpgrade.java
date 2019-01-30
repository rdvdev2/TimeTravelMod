package tk.rdvdev2.TimeTravelMod.api.timemachine.block;

import net.minecraft.block.material.Material;
import tk.rdvdev2.TimeTravelMod.ModRegistries;
import tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.TimeMachineUpgrade;
import tk.rdvdev2.TimeTravelMod.common.event.EventSetTimeMachine;

import java.util.Arrays;
import java.util.HashMap;

public class BlockTimeMachineUpgrade extends BlockTimeMachineComponent {

    public BlockTimeMachineUpgrade(Material material) {
        super(material);
    }

    /**
     * Links the block with it's corresponding Time Machine
     *
     * @param event The linking event
     */
    @Override
    public void setTimeMachine(EventSetTimeMachine event) {
        HashMap<TimeMachineUpgrade, BlockTimeMachineComponent[]> hm = (HashMap<TimeMachineUpgrade, BlockTimeMachineComponent[]>) ModRegistries.upgradesRegistry.getSlaveMap(ModRegistries.UPGRADETOBLOCK, HashMap.class);
        if (hm.containsKey(getUpgrade())) {
            BlockTimeMachineComponent[] blocks = hm.get(getUpgrade());
            int index = blocks.length;
            blocks = Arrays.copyOf(blocks, index+1);
            blocks[index] = this;
            hm.put(getUpgrade(), blocks);
        } else {
            hm.put(getUpgrade(), new BlockTimeMachineComponent[]{this});
        }
    }
}
