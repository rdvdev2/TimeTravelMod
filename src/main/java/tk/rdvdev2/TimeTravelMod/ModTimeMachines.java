package tk.rdvdev2.TimeTravelMod;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tk.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;
import tk.rdvdev2.TimeTravelMod.common.timemachine.TimeMachineCreative;
import tk.rdvdev2.TimeTravelMod.common.timemachine.TimeMachineTier1;

@Mod.EventBusSubscriber(modid="timetravelmod")
public class ModTimeMachines {

    public static TimeMachine timeMachineTier1;
    public static ResourceLocation timeMachineTier1Location;
    public static TimeMachine timeMachineCreative;
    public static ResourceLocation timeMachineCreativeLocation;

    public static void init() {
        timeMachineTier1 = new TimeMachineTier1();
        timeMachineTier1Location = new ResourceLocation("timetravelmod:tmtier1");
        timeMachineCreative = new TimeMachineCreative();
        timeMachineCreativeLocation = new ResourceLocation("timetravelmod:tmcreative");
    }
    @SubscribeEvent
    public static void registerTimeMachines(RegistryEvent.Register<TimeMachine> event) {
        event.getRegistry().registerAll(
                timeMachineTier1.setRegistryName(timeMachineTier1Location),
                timeMachineCreative.setRegistryName(timeMachineCreativeLocation)
        );
    }
}
