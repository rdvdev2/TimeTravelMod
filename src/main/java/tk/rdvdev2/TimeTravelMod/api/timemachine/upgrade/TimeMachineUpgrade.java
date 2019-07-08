package tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade;

import net.minecraftforge.registries.IForgeRegistryEntry;
import tk.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;

import java.util.ArrayList;

public abstract class TimeMachineUpgrade implements IForgeRegistryEntry<TimeMachineUpgrade> {

    private ArrayList<TimeMachineHook> hooks;
    private TimeMachine[] compatibleTMs;

    public TimeMachineUpgrade() {
        this.hooks = new ArrayList<TimeMachineHook>(0);
    }

    public void addHook(TimeMachineHook hook) {
        this.hooks.add(hook);
    }

    public void addAllHooks(TimeMachineHook... hooks) {
        for (TimeMachineHook hook:hooks) {
            this.addHook(hook);
        }
    }

    public <T> T runHook(T original, Class<? extends TimeMachineHook> clazz, TimeMachineHookRunner tm, Object... args) {
        T result = original;
        for (TimeMachineHook hook:this.hooks) {
            if (clazz.isInstance(hook)) {
                result = hook.run(result, tm, args);
            }
        }
        return result;
    }

    public boolean runVoidHook(Class<? extends TimeMachineHook> clazz, TimeMachineHookRunner tm, Object... args) {
        for (TimeMachineHook hook:this.hooks) {
            if (clazz.isInstance(hook)) {
                hook.run(null, tm, args);
                return true;
            }
        }
        return false;
    }

    public TimeMachine[] getCompatibleTMs() {
        return compatibleTMs;
    }

    public void setCompatibleTMs(TimeMachine[] compatibleTMs) {
        this.compatibleTMs = compatibleTMs;
    }
}
