package tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade;

import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.IForgeRegistryEntry;
import tk.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;

/**
 * Contains all the Time Machine hooks needed to apply an upgrade to a Time Machine. You can compose an upgrade using a builder-like scheme. Objects must be registered to work.
 */
public interface TimeMachineUpgrade extends IForgeRegistryEntry<TimeMachineUpgrade> {

    /**
     * Adds a hook to the upgrade. It will run in non exclusive mode.
     * @param hook The hook to add
     * @return The upgrade itself
     */
    TimeMachineUpgrade addHook(TimeMachineHook<?> hook);

    /**
     * Adds a hook to the upgrade
     * @param hook The hook to add
     * @param exclusiveMode If true, the hook will run in exclusive mode
     * @return The upgrade itself
     */
    TimeMachineUpgrade addHook(TimeMachineHook<?> hook, boolean exclusiveMode);

    /**
     * Adds one or more hooks to the upgrade. They will run in non exclusive mode
     * @param hooks The hooks to add
     * @return The upgrade itself
     */
    TimeMachineUpgrade addAllHooks(TimeMachineHook<?>... hooks);

    /**
     * Specifies which Time Machines are compatible with this upgrade
     * @param compatibleTMs The list of compatible Time Machines
     * @return The upgrade itself
     */
    TimeMachineUpgrade setCompatibleTMs(TimeMachine... compatibleTMs);

    /**
     * Returns which Time Machines are compatible with this upgrade
     * @return The list of compatible Time Machines
     */
    TimeMachine[] getCompatibleTMs();

    /**
     * Returns the name of the upgrade for use in GUIs
     * @return The name of the upgrade as a TranslationTextComponent
     */
    TranslationTextComponent getName();

    /**
     * Returns the description of the upgrade for use in GUIs
     * @return The description of the upgrade as a TranslationTextComponent
     */
    TranslationTextComponent getDescription();

    /**
     * Support method for registration
     * @see net.minecraftforge.registries.ForgeRegistryEntry#setRegistryName(String)
     */
    TimeMachineUpgrade setRegistryName(String name);

    /**
     * Support method for registration
     * @see net.minecraftforge.registries.ForgeRegistryEntry#setRegistryName(String, String)
     */
    TimeMachineUpgrade setRegistryName(String modID, String name);

    /**
     * Constructs a new empty upgrade
     * @return An empty upgrade
     */
    static TimeMachineUpgrade getNew() {
        return new tk.rdvdev2.TimeTravelMod.common.timemachine.upgrade.TimeMachineUpgrade();
    }
}
