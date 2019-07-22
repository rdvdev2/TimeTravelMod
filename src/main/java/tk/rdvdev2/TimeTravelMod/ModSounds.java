package tk.rdvdev2.TimeTravelMod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import tk.rdvdev2.TimeTravelMod.common.world.dimension.oldwest.OldWestDimension;

public class ModSounds {

    public static ResourceLocation oldWestMusicLocation = new ResourceLocation(TimeTravelMod.MODID, "oldwest_music");
    public static SoundEvent oldWestMusicEvent = new SoundEvent(oldWestMusicLocation);

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().registerAll(
                oldWestMusicEvent.setRegistryName(oldWestMusicLocation)
        );
    }

    public static void onPlaySound(PlaySoundEvent event) {
        if (ModConfig.CLIENT.enableTimeLineMusic.get()) {
            if (Minecraft.getInstance().player != null &&
                    Minecraft.getInstance().player.world.getDimension() instanceof OldWestDimension &&
                    event.getSound().getCategory() == SoundCategory.MUSIC) {
                event.setResultSound(SimpleSound.music(oldWestMusicEvent));
            }
        }
    }
}
