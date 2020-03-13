package com.rdvdev2.TimeTravelMod.common.timemachine.exception;

import com.rdvdev2.TimeTravelMod.api.timemachine.upgrade.TimeMachineUpgrade;

import java.util.HashSet;

public class IncompatibleTimeMachineHooksException extends Throwable {

    private HashSet<TimeMachineUpgrade> incompatibilities;

    public IncompatibleTimeMachineHooksException(HashSet<TimeMachineUpgrade> incompatibilities) {
        this.incompatibilities = incompatibilities;
    }

    public HashSet<TimeMachineUpgrade> getIncompatibilities() {
        return incompatibilities;
    }
}
