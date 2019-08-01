package tk.rdvdev2.TimeTravelMod.api.timemachine.block;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import tk.rdvdev2.TimeTravelMod.ModRegistries;
import tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.TimeMachineUpgrade;

import java.util.Arrays;
import java.util.HashMap;

public abstract class AbstractTimeMachineUpgradeBlock extends AbstractTimeMachineComponentBlock {

    public AbstractTimeMachineUpgradeBlock(Properties properties) {
        super(properties);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setTimeMachine);
    }

    public void setTimeMachine(FMLCommonSetupEvent event) {
        HashMap<TimeMachineUpgrade, AbstractTimeMachineComponentBlock[]> hm = ModRegistries.UPGRADES.getSlaveMap(ModRegistries.UPGRADETOBLOCK, HashMap.class);
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
