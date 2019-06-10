package tk.rdvdev2.TimeTravelMod.common.event;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
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
            if (event.getPlayer() instanceof ServerPlayerEntity) {
                ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
                VersionChecker.CheckResult result = VersionChecker.getResult(ModList.get().getModContainerById(TimeTravelMod.MODID).get().getModInfo());
                if (result.status == VersionChecker.Status.OUTDATED) {
                    player.sendMessage(new TranslationTextComponent("chat.ttm.outdated"));
                }
            }
        }
    }
}
