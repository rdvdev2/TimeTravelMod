package tk.rdvdev2.TimeTravelMod.common.world.dimension.oldwest.biome;

import com.google.common.collect.Sets;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.OverworldGenSettings;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.layer.Layer;
import net.minecraft.world.storage.WorldInfo;
import tk.rdvdev2.TimeTravelMod.ModBiomes;
import tk.rdvdev2.TimeTravelMod.common.world.layer.LayerUtil;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class OldWestBiomeProvider extends BiomeProvider {
    private final Layer genBiomes;
    private final Layer biomeFactoryLayer;
    private final Biome[] biomes = new Biome[]{ModBiomes.OLDWEST, Biomes.BADLANDS, Biomes.BADLANDS_PLATEAU, Biomes.WOODED_BADLANDS_PLATEAU, Biomes.ERODED_BADLANDS, Biomes.MODIFIED_BADLANDS_PLATEAU, Biomes.MODIFIED_WOODED_BADLANDS_PLATEAU, Biomes.OCEAN, Biomes.LUKEWARM_OCEAN, Biomes.WARM_OCEAN, Biomes.DEEP_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN, Biomes.DEEP_WARM_OCEAN, Biomes.RIVER, Biomes.BEACH};

    public OldWestBiomeProvider(OldWestBiomeProviderSettings settingsProvider) {
        WorldInfo worldInfo = settingsProvider.getWorldInfo();
        OverworldGenSettings overworldGenSettings = settingsProvider.getGeneratorSettings();
        Layer[] layers = LayerUtil.buildOldWestProcedure(worldInfo.getSeed(), worldInfo.getGenerator(), overworldGenSettings);
        this.genBiomes = layers[0];
        this.biomeFactoryLayer = layers[1];
    }

    @Override
    public Biome getBiome(int x, int y) {
        return this.biomeFactoryLayer.func_215738_a(x, y);
    }

    public Biome func_222366_b(int p_222366_1_, int p_222366_2_) {
        return this.genBiomes.func_215738_a(p_222366_1_, p_222366_2_);
    }

    @Override
    public Biome[] getBiomes(int x, int z, int width, int length, boolean cacheFlag) {
        return this.biomeFactoryLayer.generateBiomes(x, z, width, length);
    }

    @Override
    public Set<Biome> getBiomesInSquare(int centerX, int centerZ, int sideLength) {
        int x = centerX - sideLength >> 2;
        int z = centerZ - sideLength >> 2;
        int k = centerX + sideLength >> 2;
        int l = centerZ + sideLength >> 2;
        int width = k - x + 1;
        int length = l - z + 1;
        Set<Biome> set = Sets.newHashSet();
        Collections.addAll(set, this.genBiomes.generateBiomes(x, z, width, length));
        return set;
    }

    @Nullable
    @Override
    public BlockPos findBiomePosition(int x, int z, int range, List<Biome> biomes, Random random) {
        int i = x - range >> 2;
        int j = z - range >> 2;
        int k = x + range >> 2;
        int l = z + range >> 2;
        int i1 = k - i + 1;
        int j1 = l - j + 1;
        Biome[] abiome = this.genBiomes.generateBiomes(i, j, i1, j1);
        BlockPos blockpos = null;
        int k1 = 0;
        for(int l1 = 0; l1 < i1 * j1; ++l1) {
            int i2 = i + l1 % i1 << 2;
            int j2 = j + l1 / i1 << 2;
            if (biomes.contains(abiome[l1])) {
                if (blockpos == null || random.nextInt(k1 + 1) == 0) {
                    blockpos = new BlockPos(i2, 0, j2);
                }
                ++k1;
            }
        }
        return blockpos;
    }

    @Override
    public boolean hasStructure(Structure<?> structureIn) {
        return this.hasStructureCache.computeIfAbsent(structureIn, structure -> {
            for(Biome biome: this.biomes) {
                if (biome.hasStructure(structure)) {
                    return true;
                }
            }
            return false;
        });
    }

    @Override
    public Set<BlockState> getSurfaceBlocks() {
        if (this.topBlocksCache.isEmpty()) {
            for (Biome biome: this.biomes) {
                this.topBlocksCache.add(biome.getSurfaceBuilderConfig().getTop());
            }
        }
        return this.topBlocksCache;
    }
}
