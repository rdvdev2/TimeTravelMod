package tk.rdvdev2.TimeTravelMod.common.world.dimension.oldwest.biome;

import com.google.common.collect.ImmutableSet;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.layer.Layer;
import net.minecraft.world.storage.WorldInfo;
import tk.rdvdev2.TimeTravelMod.ModBiomes;
import tk.rdvdev2.TimeTravelMod.common.world.layer.LayerUtil;

import java.util.Set;

public class OldWestBiomeProvider extends BiomeProvider {
    private final Layer genBiomes;
    //private final Layer biomeFactoryLayer;
    private static final Set<Biome> biomes = ImmutableSet.of(ModBiomes.OLDWEST, Biomes.BADLANDS, Biomes.BADLANDS_PLATEAU, Biomes.WOODED_BADLANDS_PLATEAU, Biomes.ERODED_BADLANDS, Biomes.MODIFIED_BADLANDS_PLATEAU, Biomes.MODIFIED_WOODED_BADLANDS_PLATEAU, Biomes.OCEAN, Biomes.LUKEWARM_OCEAN, Biomes.WARM_OCEAN, Biomes.DEEP_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN, Biomes.DEEP_WARM_OCEAN, Biomes.RIVER, Biomes.BEACH);

    public OldWestBiomeProvider(OldWestBiomeProviderSettings settingsProvider) {
        super(biomes);
        WorldInfo worldInfo = settingsProvider.getWorldInfo();
        this.genBiomes = LayerUtil.buildOldWestProcedure(worldInfo.getSeed(), worldInfo.getGenerator(), settingsProvider.getGeneratorSettings());
    }

    @Override
    public Biome func_225526_b_(int i, int i1, int i2) {
        return this.genBiomes.func_215738_a(i, i2);
    }
}
