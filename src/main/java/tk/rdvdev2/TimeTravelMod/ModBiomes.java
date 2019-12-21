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
    public static final Biome OLDWEST = new OldWestBiome().setRegistryName(MODID, "oldwest");

    @SubscribeEvent
    public static void registerBiomes(RegistryEvent.Register<Biome> event) {
        event.getRegistry().registerAll(
                OLDWEST
        );
    }

    public static class ProviderTypes {
        public static final BiomeProviderType<OldWestBiomeProviderSettings, OldWestBiomeProvider> OLDWEST_LAYERED = (BiomeProviderType<OldWestBiomeProviderSettings, OldWestBiomeProvider>) new BiomeProviderType<OldWestBiomeProviderSettings, OldWestBiomeProvider>(OldWestBiomeProvider::new, OldWestBiomeProviderSettings::new).setRegistryName(MODID, "oldwest_layered");

        @SubscribeEvent
        public static void registerProviderTypes(RegistryEvent.Register<BiomeProviderType<?,?>> event) {
            event.getRegistry().registerAll(
                    OLDWEST_LAYERED
            );
        }
    }
}
