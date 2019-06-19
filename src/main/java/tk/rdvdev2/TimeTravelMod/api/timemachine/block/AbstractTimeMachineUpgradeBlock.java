package tk.rdvdev2.TimeTravelMod.api.timemachine.block;

import tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.TimeMachineUpgrade;
import tk.rdvdev2.TimeTravelMod.common.event.ConfigureTimeMachineBlocksEvent;

import java.util.Arrays;
import java.util.HashMap;

public abstract class AbstractTimeMachineUpgradeBlock extends AbstractTimeMachineComponentBlock {

    public AbstractTimeMachineUpgradeBlock(Properties properties) {
        super(properties);
    }

    /**
     * Links the block with it's corresponding Time Machine
     *
     * @param event The linking event
     */
    @Override
    public void setTimeMachine(ConfigureTimeMachineBlocksEvent event) {
        HashMap<TimeMachineUpgrade, AbstractTimeMachineComponentBlock[]> hm = event.getUpgrades();
        if (hm.containsKey(getUpgrade())) {
            AbstractTimeMachineComponentBlock[] blocks = hm.get(getUpgrade());
            int index = blocks.length;
            blocks = Arrays.copyOf(blocks, index+1);
            blocks[index] = this;
            hm.put(getUpgrade(), blocks);
        } else {
            hm.put(getUpgrade(), new AbstractTimeMachineComponentBlock[]{this});
        }
    }

    /**
     * Returns the attached upgrade
     * @return The attached upgrade
     */
    public abstract TimeMachineUpgrade getUpgrade();
}
