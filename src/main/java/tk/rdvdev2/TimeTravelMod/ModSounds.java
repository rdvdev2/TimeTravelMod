package tk.rdvdev2.TimeTravelMod;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus= Mod.EventBusSubscriber.Bus.MOD)
public class ModSounds {

    public static ResourceLocation oldWestMusicLocation = new ResourceLocation(TimeTravelMod.MODID, "oldwest_music");
    public static SoundEvent oldWestMusicEvent = new SoundEvent(oldWestMusicLocation);

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().registerAll(
                oldWestMusicEvent.setRegistryName(oldWestMusicLocation)
        );
    }
}
