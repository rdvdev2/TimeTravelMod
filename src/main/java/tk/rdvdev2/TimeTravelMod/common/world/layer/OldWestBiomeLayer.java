package tk.rdvdev2.TimeTravelMod.common.world.layer;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.OverworldGenSettings;
import net.minecraft.world.gen.layer.traits.IC0Transformer;
import tk.rdvdev2.TimeTravelMod.ModBiomes;

public class OldWestBiomeLayer implements IC0Transformer {

    @SuppressWarnings("unchecked")
    private java.util.List<net.minecraftforge.common.BiomeManager.BiomeEntry>[] biomes = new java.util.ArrayList[net.minecraftforge.common.BiomeManager.BiomeType.values().length];
    private final OverworldGenSettings settings;

    public OldWestBiomeLayer(OverworldGenSettings p_i48641_2_) {
        for (net.minecraftforge.common.BiomeManager.BiomeType type : net.minecraftforge.common.BiomeManager.BiomeType.values()) {
            com.google.common.collect.ImmutableList<net.minecraftforge.common.BiomeManager.BiomeEntry> biomesToAdd = net.minecraftforge.common.BiomeManager.getBiomes(type);
            int idx = type.ordinal();

            if (biomes[idx] == null) biomes[idx] = new java.util.ArrayList<net.minecraftforge.common.BiomeManager.BiomeEntry>();
            if (biomesToAdd != null) biomes[idx].addAll(biomesToAdd);
        }

        int desertIdx = net.minecraftforge.common.BiomeManager.BiomeType.DESERT.ordinal();

        biomes[desertIdx].add(new net.minecraftforge.common.BiomeManager.BiomeEntry(ModBiomes.OLDWEST, 80));
        biomes[desertIdx].add(new net.minecraftforge.common.BiomeManager.BiomeEntry(Biomes.BADLANDS_PLATEAU, 5));
        biomes[desertIdx].add(new net.minecraftforge.common.BiomeManager.BiomeEntry(Biomes.WOODED_BADLANDS_PLATEAU, 15));

        this.settings = p_i48641_2_;
    }

    public int apply(INoiseRandom context, int value) {
        int i = (value & 3840) >> 8;
        value = value & -3841;
        if (!LayerUtil.isOcean(value)) {
            return Registry.BIOME.getId(getWeightedBiomeEntry(net.minecraftforge.common.BiomeManager.BiomeType.DESERT, context).biome); }
        else {
            return value;
        }
    }

    protected net.minecraftforge.common.BiomeManager.BiomeEntry getWeightedBiomeEntry(net.minecraftforge.common.BiomeManager.BiomeType type, INoiseRandom context) {
        java.util.List<net.minecraftforge.common.BiomeManager.BiomeEntry> biomeList = biomes[type.ordinal()];
        int totalWeight = net.minecraft.util.WeightedRandom.getTotalWeight(biomeList);
        int weight = net.minecraftforge.common.BiomeManager.isTypeListModded(type)?context.random(totalWeight):context.random(totalWeight / 10) * 10;
        return (net.minecraftforge.common.BiomeManager.BiomeEntry)net.minecraft.util.WeightedRandom.getRandomItem(biomeList, weight);
    }
}