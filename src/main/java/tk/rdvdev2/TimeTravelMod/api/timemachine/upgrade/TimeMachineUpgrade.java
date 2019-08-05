package tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade;

import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistryEntry;
import tk.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;
import tk.rdvdev2.TimeTravelMod.common.timemachine.TimeMachineHookRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;

public abstract class TimeMachineUpgrade extends ForgeRegistryEntry<TimeMachineUpgrade> {

    private ArrayList<TimeMachineHook> hooks;
    private TimeMachine[] compatibleTMs;
    private HashSet<Class<? extends TimeMachineHook>> exclusiveHooks;

    public TimeMachineUpgrade() {
        this.hooks = new ArrayList<TimeMachineHook>(0);
    }

    public void addHook(TimeMachineHook hook) {
        addHook(hook, false);
    }

    public void addHook(TimeMachineHook hook, boolean exclusiveMode) {
        this.hooks.add(hook);
        if (exclusiveMode) {
            exclusiveHooks.add(hook.getClass());
        }
    }

    public void addAllHooks(TimeMachineHook... hooks) {
        for (TimeMachineHook hook:hooks) {
            this.addHook(hook);
        }
    }

    public <T> T runHook(Optional<T> original, Class<? extends TimeMachineHook> clazz, TimeMachineHookRunner tm, Object... args) {
        Optional<T> result = original;
        for (TimeMachineHook hook:this.hooks) {
            if (clazz.isInstance(hook)) {
                result = Optional.of((T)(hook.run(result, tm, args)));
            }
        }
        return result.orElse(original.orElseThrow(RuntimeException::new));
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

    public boolean isExclusiveHook(Class<? extends TimeMachineHook> hook) {
        if (exclusiveHooks.isEmpty()) return false;
        Iterator<Class<? extends TimeMachineHook>> it = exclusiveHooks.iterator();
        while (it.hasNext()) if (hook.isAssignableFrom(it.next())) return true;
        return false;
    }

    public final TranslationTextComponent getName() { // tmupgrade.modid.registryname.name
        return new TranslationTextComponent("tmupgrade."+getRegistryName().getNamespace()+"."+getRegistryName().getPath()+".name");
    }

    public final TranslationTextComponent getDescription() { // tmupgrade.modid.registryname.description
        return new TranslationTextComponent("tmupgrade."+getRegistryName().getNamespace()+"."+getRegistryName().getPath()+".description");
    }
}
