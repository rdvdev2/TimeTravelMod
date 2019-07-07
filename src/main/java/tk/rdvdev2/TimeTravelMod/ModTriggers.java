package tk.rdvdev2.TimeTravelMod;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.util.ResourceLocation;
import tk.rdvdev2.TimeTravelMod.common.triggers.CustomTrigger;

import static tk.rdvdev2.TimeTravelMod.TimeTravelMod.MODID;

public class ModTriggers {
    public static final CustomTrigger ACCESS_TIME_MACHINE = new CustomTrigger(new ResourceLocation(MODID, "access_time_machine"));
    public static final CustomTrigger BETTER_THAN_MENDING = new CustomTrigger(new ResourceLocation(MODID, "better_than_mending"));
    public static final CustomTrigger TEMPORAL_EXPLOSION = new CustomTrigger(new ResourceLocation(MODID, "temporal_explosion"));

    public static final CustomTrigger[] TRIGGERS = new CustomTrigger[]{
            ACCESS_TIME_MACHINE,
            BETTER_THAN_MENDING,
            TEMPORAL_EXPLOSION
    };

    public static void register() {
        for (CustomTrigger trigger : TRIGGERS) CriteriaTriggers.register(trigger);
    }
}
