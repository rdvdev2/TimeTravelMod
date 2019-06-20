package tk.rdvdev2.TimeTravelMod;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = "timetravelmod")
public class ModSounds {

    public static ResourceLocation oldWestMusicLocation = new ResourceLocation("timetravelmod", "oldwest_music");
    public static SoundEvent oldWestMusicEvent = new SoundEvent(oldWestMusicLocation);

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().registerAll(
                oldWestMusicEvent.setRegistryName(oldWestMusicLocation)
        );
    }

    @SubscribeEvent
    public static void onPlaySound(PlaySoundEvent event) {
        if (Minecraft.getMinecraft().player != null &&
                Minecraft.getMinecraft().player.dimension == ModTimeLines.oldWest.getDimId() &&
                event.getSound().getCategory() == SoundCategory.MUSIC) {
            event.setResultSound(PositionedSoundRecord.getMusicRecord(oldWestMusicEvent));
        }
    }
}
