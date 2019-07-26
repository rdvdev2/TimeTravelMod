package tk.rdvdev2.TimeTravelMod;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProviderType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import tk.rdvdev2.TimeTravelMod.common.world.dimension.oldwest.biome.OldWestBiome;
import tk.rdvdev2.TimeTravelMod.common.world.dimension.oldwest.biome.OldWestBiomeProvider;
import tk.rdvdev2.TimeTravelMod.common.world.dimension.oldwest.biome.OldWestBiomeProviderSettings;

import static tk.rdvdev2.TimeTravelMod.TimeTravelMod.MODID;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class ModBiomes {
    public static Biome OLDWEST = new OldWestBiome();

    @SubscribeEvent
    public static void registerBiomes(RegistryEvent.Register<Biome> event) {
        event.getRegistry().registerAll(
                OLDWEST.setRegistryName("timetravelmod:oldwest")
        );
    }

    public static class ProviderTypes {
        public static BiomeProviderType<OldWestBiomeProviderSettings, OldWestBiomeProvider> OLDWEST_LAYERED = new BiomeProviderType<>(OldWestBiomeProvider::new, OldWestBiomeProviderSettings::new);

        @SubscribeEvent
        public static void registerProviderTypes(RegistryEvent.Register<BiomeProviderType<?,?>> event) {
            event.getRegistry().registerAll(
                    OLDWEST_LAYERED.setRegistryName(MODID, "oldwest_layered")
            );
        }
    }
}
