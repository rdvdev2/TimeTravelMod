package tk.rdvdev2.TimeTravelMod.common.event;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.ForgeVersion.Status;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

@Mod.EventBusSubscriber(modid = "timetravelmod")
public class MiscEventHandler {
    @SubscribeEvent
    public static void worldStart(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.player;
            ForgeVersion.CheckResult result = ForgeVersion.getResult(Loader.instance().activeModContainer());
            if (result.status == Status.OUTDATED) {
                player.sendMessage(new TextComponentTranslation("chat.ttm.outdated"));
            }
        }
    }
}
