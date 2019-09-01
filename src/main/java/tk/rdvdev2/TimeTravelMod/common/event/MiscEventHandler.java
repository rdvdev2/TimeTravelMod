package tk.rdvdev2.TimeTravelMod.common.event;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.VersionChecker;
import net.minecraftforge.fml.common.Mod;
import tk.rdvdev2.TimeTravelMod.ModConfig;
import tk.rdvdev2.TimeTravelMod.ModRegistries;
import tk.rdvdev2.TimeTravelMod.ModTimeLines;
import tk.rdvdev2.TimeTravelMod.TimeTravelMod;
import tk.rdvdev2.TimeTravelMod.api.dimension.TimeLine;
import tk.rdvdev2.TimeTravelMod.common.entity.origintl.IOriginTL;

@Mod.EventBusSubscriber(modid = "timetravelmod")
public class MiscEventHandler {

    @CapabilityInject(IOriginTL.class)
    private static Capability<IOriginTL> ORIGIN_CAPABILITY = null;

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

    @SubscribeEvent
    public static void entitySpawn(LivingSpawnEvent.CheckSpawn event) {
        IOriginTL originCap = event.getEntityLiving().getCapability(ORIGIN_CAPABILITY).orElseThrow(RuntimeException::new);
        for (TimeLine tl: ModRegistries.TIME_LINES) {
            if (tl.getDimension() == event.getWorld().getDimension().getType()) {
                originCap.setOriginTimeLine(tl);
                break;
            }
        }
        if (originCap.getOriginTimeLine() == null) originCap.setOriginTimeLine(ModTimeLines.PRESENT);
    }
}
