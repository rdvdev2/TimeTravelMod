package com.rdvdev2.TimeTravelMod;

import com.rdvdev2.TimeTravelMod.common.registry.RegistryTimeLines;
import com.rdvdev2.TimeTravelMod.common.registry.RegistryTimeMachines;

public class ModRegistries {

    public static RegistryTimeMachines timeMachines;
    public static RegistryTimeLines timeLines;

    public static void init() {
        timeMachines = new RegistryTimeMachines();
        timeLines = new RegistryTimeLines();
    }

    public static void start() {
        timeMachines.start();
        timeLines.start();
    }
}
