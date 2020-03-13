package com.rdvdev2.TimeTravelMod;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModSounds {

    private static final DeferredRegister<SoundEvent> SOUND_EVENTS = new DeferredRegister<>(ForgeRegistries.SOUND_EVENTS, TimeTravelMod.MODID);

    public static final RegistryObject<SoundEvent> OLDWEST_MUSIC = SOUND_EVENTS.register("oldwest_music", () -> new SoundEvent(new ResourceLocation(TimeTravelMod.MODID, "oldwest_music")));

    public static void init() {
        SOUND_EVENTS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
