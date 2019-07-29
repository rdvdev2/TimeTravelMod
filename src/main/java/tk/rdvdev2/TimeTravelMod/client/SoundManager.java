package tk.rdvdev2.TimeTravelMod.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import tk.rdvdev2.TimeTravelMod.ModConfig;
import tk.rdvdev2.TimeTravelMod.ModSounds;
import tk.rdvdev2.TimeTravelMod.common.world.dimension.oldwest.OldWestDimension;

public class SoundManager {
    public static void onPlaySound(PlaySoundEvent event) {
        if (ModConfig.CLIENT.enableTimeLineMusic.get()) {
            if (Minecraft.getInstance().player != null &&
                    Minecraft.getInstance().player.world.getDimension() instanceof OldWestDimension &&
                    event.getSound().getCategory() == SoundCategory.MUSIC) {
                event.setResultSound(SimpleSound.music(ModSounds.oldWestMusicEvent));
            }
        }
    }
}
