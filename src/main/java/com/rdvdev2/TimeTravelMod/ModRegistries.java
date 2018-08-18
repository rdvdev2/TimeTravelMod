package com.rdvdev2.TimeTravelMod;

import com.rdvdev2.TimeTravelMod.common.registry.RegistryTimeMachines;

public class ModRegistries {

    public static RegistryTimeMachines timeMachines;

    public static void init() {
        timeMachines = new RegistryTimeMachines();
    }

    public static void start() {
        timeMachines.start();
    }
}
