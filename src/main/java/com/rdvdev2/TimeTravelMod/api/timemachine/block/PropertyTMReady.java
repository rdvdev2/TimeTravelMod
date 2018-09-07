package com.rdvdev2.TimeTravelMod.api.timemachine.block;

import net.minecraft.block.properties.PropertyBool;

// TODO: JavaDoc
public class PropertyTMReady {
    public static PropertyBool ready;

    public static void init() {
     ready = PropertyBool.create("ready");
    }
}
