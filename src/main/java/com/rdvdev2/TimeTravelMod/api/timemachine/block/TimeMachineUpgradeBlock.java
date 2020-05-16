package com.rdvdev2.TimeTravelMod.api.timemachine.block;

import com.rdvdev2.TimeTravelMod.ModRegistries;
import com.rdvdev2.TimeTravelMod.api.timemachine.upgrade.TimeMachineUpgrade;
import net.minecraft.block.Block;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Blocks that pretend to act as a Time Machine Upgrade must extend from this class.
 * Subclasses will provide the specified Time Machine Upgrade without needing to overwrite nothing on the class.
 */
public class TimeMachineUpgradeBlock extends Block {

    private TimeMachineUpgrade upgrade;

    /**
     * This works exactly as {@link Block#Block(Properties)}, but you also have to provide the {@link TimeMachineUpgrade} associated to this block.
     * @param properties {@link Block#Block(Properties)}
     * @param upgrade The {@link TimeMachineUpgrade} associated to this block
     */
    public TimeMachineUpgradeBlock(Properties properties, TimeMachineUpgrade upgrade) {
        super(properties);
        this.upgrade = upgrade;
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setTimeMachine);
    }

    private void setTimeMachine(FMLCommonSetupEvent event) {
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

    /**
     * Returns the attached {@link TimeMachineUpgrade}
     * @return The attached {@link TimeMachineUpgrade}
     */
    public TimeMachineUpgrade getUpgrade() {
        if (this.upgrade == null) throw new NullPointerException("Tried to access to the TimeMachineUpgrade of an unconfigured block (" + getRegistryName() + ")");
        return this.upgrade;
    }
}
