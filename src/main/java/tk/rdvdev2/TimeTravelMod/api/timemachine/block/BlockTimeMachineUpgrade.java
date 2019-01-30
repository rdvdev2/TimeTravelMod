package tk.rdvdev2.TimeTravelMod.api.timemachine.block;

import net.minecraft.block.material.Material;
import tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.TimeMachineUpgrade;
import tk.rdvdev2.TimeTravelMod.common.event.EventConfigureTimeMachineBlocks;

import java.util.Arrays;
import java.util.HashMap;

public abstract class BlockTimeMachineUpgrade extends BlockTimeMachineComponent {

    public BlockTimeMachineUpgrade(Material material) {
        super(material);
    }

    /**
     * Links the block with it's corresponding Time Machine
     *
     * @param event The linking event
     */
    @Override
    public void setTimeMachine(EventConfigureTimeMachineBlocks event) {
        HashMap<TimeMachineUpgrade, BlockTimeMachineComponent[]> hm = event.getUpgrades();
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

    /**
     * Returns the attached upgrade
     * @return The attached upgrade
     */
    public abstract TimeMachineUpgrade getUpgrade();
}
