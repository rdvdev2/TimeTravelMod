package com.rdvdev2.TimeTravelMod.api.timemachine.upgrade;

import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.Arrays;

public abstract class TimeMachineUpgrade extends IForgeRegistryEntry.Impl<TimeMachineUpgrade> {

    private int maxTier;
    private TimeMachineHook[] hooks;

    public TimeMachineUpgrade (int maxTier) {
        this.maxTier = maxTier;
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

    public int getMaxTier() {
        return maxTier;
    }

    public <T> T runHook(T original, Class<? extends TimeMachineHook> clazz, Object... args) {
        T result = original;
        for (TimeMachineHook hook:this.hooks) {
            if (clazz.isInstance(hook)) {
                result = hook.run(result, args);
            }
        }
        return result;
    }

    public boolean runVoidHook(Class<? extends TimeMachineHook> clazz, Object... args) {
        for (TimeMachineHook hook:this.hooks) {
            if (clazz.isInstance(hook)) {
                hook.run(null, args);
                return true;
            }
        }
        return false;
    }
}
