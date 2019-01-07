package tk.rdvdev2.TimeTravelMod;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import tk.rdvdev2.TimeTravelMod.common.dimension.oldwest.BiomeOldWest;

@Mod.EventBusSubscriber(modid="timetravelmod")
public class ModBiomes {
    public static Biome OLDWEST;

    public static void init() {
        OLDWEST = new BiomeOldWest(new Biome.BiomeProperties("Old West"));
    }

    @SubscribeEvent
    public static void registerBiomes(RegistryEvent.Register<Biome> event) {
        event.getRegistry().registerAll(
                OLDWEST.setRegistryName("timetravelmod:oldwest")
        );
    }
}
