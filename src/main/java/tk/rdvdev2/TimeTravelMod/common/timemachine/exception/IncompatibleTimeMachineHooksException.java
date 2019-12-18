package tk.rdvdev2.TimeTravelMod.common.timemachine.exception;

import tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.TimeMachineUpgrade;

import java.util.HashSet;

public class IncompatibleTimeMachineHooksException extends Throwable {

    private HashSet<tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.TimeMachineUpgrade> incompatibilities;

    public IncompatibleTimeMachineHooksException(HashSet<tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.TimeMachineUpgrade> incompatibilities) {
        this.incompatibilities = incompatibilities;
    }

    public HashSet<TimeMachineUpgrade> getIncompatibilities() {
        return incompatibilities;
    }
}
