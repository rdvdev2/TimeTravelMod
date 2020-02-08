package tk.rdvdev2.TimeTravelMod;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.provider.BiomeProviderType;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
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

    public static void addBiomeTypes() {
        BiomeDictionary.addTypes(OLDWEST, BiomeDictionary.getTypes(Biomes.DESERT).toArray(new BiomeDictionary.Type[]{}));
    }

    public static class ProviderTypes {
        public static DeferredRegister<BiomeProviderType<?, ?>> BIOME_PROVIDER_TYPES = new DeferredRegister<>(ForgeRegistries.BIOME_PROVIDER_TYPES, MODID);
        //@ObjectHolder("timetravelmod:oldwest_layered")
        //public static final BiomeProviderType<?,?> OLDWEST_LAYERED = null;

        public static final RegistryObject<BiomeProviderType<OldWestBiomeProviderSettings, OldWestBiomeProvider>> OLDWEST_LAYERED = BIOME_PROVIDER_TYPES.register("oldwest_layered", () -> new BiomeProviderType<>(OldWestBiomeProvider::new, OldWestBiomeProviderSettings::new));

        /*@SubscribeEvent
        public static void registerProviderTypes(RegistryEvent.Register<BiomeProviderType<?,?>> event) {
            event.getRegistry().registerAll(
                    new BiomeProviderType<>(OldWestBiomeProvider::new, OldWestBiomeProviderSettings::new).setRegistryName(MODID, "oldwest_layered")
            );
        }*/
    }
}
