package tk.rdvdev2.TimeTravelMod;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import tk.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;
import tk.rdvdev2.TimeTravelMod.common.timemachine.CreativeTimeMachine;
import tk.rdvdev2.TimeTravelMod.common.timemachine.Tier1TimeMachine;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class ModTimeMachines {

    public static TimeMachine timeMachineTier1 = new Tier1TimeMachine();
    public static ResourceLocation timeMachineTier1Location = new ResourceLocation("timetravelmod:tmtier1");
    public static TimeMachine timeMachineCreative = new CreativeTimeMachine();
    public static ResourceLocation timeMachineCreativeLocation = new ResourceLocation("timetravelmod:tmcreative");

    @SubscribeEvent
    public static void registerTimeMachines(RegistryEvent.Register<TimeMachine> event) {
        event.getRegistry().registerAll(
                timeMachineTier1.setRegistryName(timeMachineTier1Location),
                timeMachineCreative.setRegistryName(timeMachineCreativeLocation)
        );
    }
}
