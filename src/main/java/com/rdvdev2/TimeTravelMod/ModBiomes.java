package com.rdvdev2.TimeTravelMod;

import com.rdvdev2.TimeTravelMod.common.world.dimension.oldwest.biome.OldWestBiome;
import com.rdvdev2.TimeTravelMod.common.world.dimension.oldwest.biome.OldWestBiomeProvider;
import com.rdvdev2.TimeTravelMod.common.world.dimension.oldwest.biome.OldWestBiomeProviderSettings;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.provider.BiomeProviderType;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModBiomes {

    private static DeferredRegister<Biome> BIOMES = new DeferredRegister<>(ForgeRegistries.BIOMES, TimeTravelMod.MODID);

    public static final RegistryObject<Biome> OLDWEST = BIOMES.register("oldwest", OldWestBiome::new);

    public static void addBiomeTypes() {
        BiomeDictionary.addTypes(OLDWEST.get(), BiomeDictionary.getTypes(Biomes.DESERT).toArray(new BiomeDictionary.Type[]{}));
    }

    public static void init() {
        BIOMES.register(FMLJavaModLoadingContext.get().getModEventBus());
        ProviderTypes.BIOME_PROVIDER_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static class ProviderTypes {
        private static DeferredRegister<BiomeProviderType<?, ?>> BIOME_PROVIDER_TYPES = new DeferredRegister<>(ForgeRegistries.BIOME_PROVIDER_TYPES, TimeTravelMod.MODID);

        public static final RegistryObject<BiomeProviderType<OldWestBiomeProviderSettings, OldWestBiomeProvider>> OLDWEST_LAYERED = BIOME_PROVIDER_TYPES.register("oldwest_layered", () -> new BiomeProviderType<>(OldWestBiomeProvider::new, OldWestBiomeProviderSettings::new));
    }
}
