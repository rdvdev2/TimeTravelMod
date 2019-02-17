package tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade;

import net.minecraftforge.registries.IForgeRegistryEntry;
import tk.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;

import java.util.Arrays;

public abstract class TimeMachineUpgrade implements IForgeRegistryEntry<TimeMachineUpgrade> {

    private TimeMachineHook[] hooks;
    private TimeMachine[] compatibleTMs;

    public TimeMachineUpgrade() {
    }

    public void addHook(TimeMachineHook hook) {
        try {
            int id = this.hooks.length;
            this.hooks = Arrays.copyOf(this.hooks, id + 1);
            this.hooks[id] = hook;
        } catch (NullPointerException e) {
            this.hooks = new TimeMachineHook[]{hook};
        }
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
