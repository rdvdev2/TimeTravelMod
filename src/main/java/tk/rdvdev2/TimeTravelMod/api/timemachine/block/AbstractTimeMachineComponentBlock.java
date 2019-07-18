package tk.rdvdev2.TimeTravelMod.api.timemachine.block;

import net.minecraft.block.Block;

/**
 * A generic Block instance that works with the TimeMachine mechanics
 */
public abstract class AbstractTimeMachineComponentBlock extends Block {
    AbstractTimeMachineComponentBlock(Properties properties) {
        super(properties);
    }
}
