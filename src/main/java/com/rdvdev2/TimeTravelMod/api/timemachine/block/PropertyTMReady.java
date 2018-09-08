package com.rdvdev2.TimeTravelMod.api.timemachine.block;

import net.minecraft.block.properties.PropertyBool;

/**
 * This class contains a BlockState property that defines if a Time Machine Core is cooled down
 */
public class PropertyTMReady {

    /**
     * BlockState property that defines if a Time Machine Core is cooled down
     */
    public static PropertyBool ready;

    /**
     * Method called from the mod to create the property
     */
    public static void init() {
     ready = PropertyBool.create("ready");
    }
}
