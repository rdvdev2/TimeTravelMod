package com.rdvdev2.TimeTravelMod.api.timemachine.exception;

import com.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;
import com.rdvdev2.TimeTravelMod.api.timemachine.upgrade.TimeMachineUpgrade;

import java.util.Set;

/**
 * This {@link Exception} is throwed whenever an incompatible {@link TimeMachineUpgrade} is used on a {@link TimeMachine}
 */
public class IncompatibleTimeMachineHooksException extends Throwable {

    private Set<TimeMachineUpgrade> incompatibilities;

    /**
     * Constructor of the {@link Exception}. Takes a {@link Set} with all the incompatibilities.
     * @param incompatibilities A {@link Set} containing all the incompatible {@link TimeMachineUpgrade}s
     */
    public IncompatibleTimeMachineHooksException(Set<TimeMachineUpgrade> incompatibilities) {
        this.incompatibilities = incompatibilities;
    }

    /**
     * Returns a {@link Set} with all the incompatibilities.
     * @return A {@link Set} containing all the incompatible {@link TimeMachineUpgrade}s
     */
    public Set<TimeMachineUpgrade> getIncompatibilities() {
        return incompatibilities;
    }
}
