package tk.rdvdev2.TimeTravelMod;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import tk.rdvdev2.TimeTravelMod.common.dimension.oldwest.BiomeOldWest;

@Mod.EventBusSubscriber(modid="timetravelmod")
public class ModBiomes {
    public static Biome OLDWEST = new BiomeOldWest();

    @SubscribeEvent
    public static void registerBiomes(RegistryEvent.Register<Biome> event) {
        event.getRegistry().registerAll(
                OLDWEST.setRegistryName("timetravelmod:oldwest")
        );
    }
}
