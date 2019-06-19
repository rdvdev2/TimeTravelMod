package tk.rdvdev2.TimeTravelMod.api.timemachine.block;

import net.minecraft.state.BooleanProperty;

/**
 * This class contains a BlockState property that defines if a Time Machine Core is cooled down
 */
public class TMReadyProperty {

    /**
     * BlockState property that defines if a Time Machine Core is cooled down
     */
    public static BooleanProperty ready = BooleanProperty.create("ready");
}
