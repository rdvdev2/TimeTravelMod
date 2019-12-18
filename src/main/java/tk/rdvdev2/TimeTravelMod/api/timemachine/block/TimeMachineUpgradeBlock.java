package tk.rdvdev2.TimeTravelMod.api.timemachine.block;

import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import tk.rdvdev2.TimeTravelMod.ModRegistries;
import tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.TimeMachineUpgrade;
import tk.rdvdev2.TimeTravelMod.common.block.AbstractTimeMachineComponentBlock;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.HashMap;

public class TimeMachineUpgradeBlock extends AbstractTimeMachineComponentBlock {

    private TimeMachineUpgrade upgrade;

    public TimeMachineUpgradeBlock(Properties properties) {
        super(properties);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setTimeMachine);
    }

    public void setTimeMachine(FMLCommonSetupEvent event) {
        HashMap<TimeMachineUpgrade, TimeMachineUpgradeBlock[]> hm = ModRegistries.UPGRADES.getSlaveMap(ModRegistries.UPGRADETOBLOCK, HashMap.class);
        if (hm.containsKey(getUpgrade())) {
            TimeMachineUpgradeBlock[] blocks = hm.get(getUpgrade());
            int index = blocks.length;
            blocks = Arrays.copyOf(blocks, index+1);
            blocks[index] = this;
            hm.put(getUpgrade(), blocks);
        } else {
            hm.put(getUpgrade(), new TimeMachineUpgradeBlock[]{this});
        }
    }

    public TimeMachineUpgradeBlock setUpgrade(@Nonnull TimeMachineUpgrade upgrade) {
        this.upgrade = upgrade;
        return this;
    }

    /**
     * Returns the attached upgrade
     * @return The attached upgrade
     */
    public TimeMachineUpgrade getUpgrade() {
        if (this.upgrade == null) throw new NullPointerException("Tried to access to the TimeMachineUpgrade of an unconfigured block (" + getRegistryName() + ")");
        return this.upgrade;
    }
}
