package tk.rdvdev2.TimeTravelMod.api.timemachine.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tk.rdvdev2.TimeTravelMod.ModRegistries;
import tk.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;
import tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.TimeMachineUpgrade;
import tk.rdvdev2.TimeTravelMod.common.event.EventSetTimeMachine;

import java.util.HashMap;

/**
 * A generic Block instance that works with the TimeMachine mechanics
 */
public abstract class BlockTimeMachineComponent extends Block {

    private TimeMachine timeMachine = null;

    BlockTimeMachineComponent(Material material) {
        super(material);
        MinecraftForge.EVENT_BUS.register(this);
    }

    /**
     * Links the block with it's corresponding Time Machine
     * @param event The linking event
     */
    @SuppressWarnings("unchecked")
    @SubscribeEvent
    public void setTimeMachine(EventSetTimeMachine event) {
        this.timeMachine = ModRegistries.timeMachinesRegistry.getValue(((HashMap<IBlockState, ResourceLocation>) ModRegistries.timeMachinesRegistry.getSlaveMap(ModRegistries.BLOCKTOTM, HashMap.class)).get(getDefaultState()));
        if (this.timeMachine == null)
            throw new IllegalArgumentException("This block (" + getDefaultState().toString() + ") is not registered in any Time Machine");
    }

    /**
     * Returns the Time Machine that belongs to this block
     * @return The compatible Time Machine
     */
    public final TimeMachine getTimeMachine() {
        return timeMachine;
    }

    /**
     * Returns the attached upgrade if the block is a Time Machine Upgrade
     * @return The attached upgrade
     */
    public TimeMachineUpgrade getUpgrade() {
        return null;
    }
}
