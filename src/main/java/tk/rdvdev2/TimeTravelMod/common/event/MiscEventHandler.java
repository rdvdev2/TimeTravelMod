package tk.rdvdev2.TimeTravelMod.common.event;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.VersionChecker;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import tk.rdvdev2.TimeTravelMod.ModConfig;
import tk.rdvdev2.TimeTravelMod.TimeTravelMod;

@Mod.EventBusSubscriber(modid = "timetravelmod")
public class MiscEventHandler {
    @SubscribeEvent
    public static void worldStart(PlayerEvent.PlayerLoggedInEvent event) {
        if (ModConfig.COMMON.enableUpdatePromos.get()) {
            if (event.getPlayer() instanceof EntityPlayerMP) {
                EntityPlayerMP player = (EntityPlayerMP) event.getPlayer();
                VersionChecker.CheckResult result = VersionChecker.getResult(ModList.get().getModContainerById(TimeTravelMod.MODID).get().getModInfo());
                if (result.status == VersionChecker.Status.OUTDATED) {
                    player.sendMessage(new TextComponentTranslation("chat.ttm.outdated"));
                }
            }
        }
    }
}
