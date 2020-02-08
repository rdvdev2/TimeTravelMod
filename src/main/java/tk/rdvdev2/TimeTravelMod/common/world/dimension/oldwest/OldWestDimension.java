package tk.rdvdev2.TimeTravelMod.common.world.dimension.oldwest;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.JsonOps;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.NBTDynamicOps;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.provider.*;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.*;
import tk.rdvdev2.TimeTravelMod.ModBiomes;
import tk.rdvdev2.TimeTravelMod.common.world.dimension.oldwest.biome.OldWestBiomeProvider;
import tk.rdvdev2.TimeTravelMod.common.world.dimension.oldwest.biome.OldWestBiomeProviderSettings;

import javax.annotation.Nullable;

public class OldWestDimension extends net.minecraft.world.dimension.Dimension {
    private DimensionType type;
    private World world;

    public OldWestDimension(World world, DimensionType type) {
        super(world, type, 0.0F);
        this.world = world;
        this.type = type;
    }

    @Override
    public boolean hasSkyLight() {
        return true;
    }

    @Override
    public ChunkGenerator<?> createChunkGenerator() {
        WorldType worldtype = this.world.getWorldInfo().getGenerator();
        ChunkGeneratorType<FlatGenerationSettings, FlatChunkGenerator> chunkgeneratortype = ChunkGeneratorType.FLAT;
        ChunkGeneratorType<DebugGenerationSettings, DebugChunkGenerator> chunkgeneratortype1 = ChunkGeneratorType.DEBUG;
        ChunkGeneratorType<NetherGenSettings, NetherChunkGenerator> chunkgeneratortype2 = ChunkGeneratorType.CAVES;
        ChunkGeneratorType<EndGenerationSettings, EndChunkGenerator> chunkgeneratortype3 = ChunkGeneratorType.FLOATING_ISLANDS;
        ChunkGeneratorType<OverworldGenSettings, OverworldChunkGenerator> chunkgeneratortype4 = ChunkGeneratorType.SURFACE;
        BiomeProviderType<SingleBiomeProviderSettings, SingleBiomeProvider> biomeprovidertype = BiomeProviderType.FIXED;
        //BiomeProviderType<OverworldBiomeProviderSettings, OverworldBiomeProvider> biomeprovidertype1 = BiomeProviderType.VANILLA_LAYERED;
        BiomeProviderType<OldWestBiomeProviderSettings, OldWestBiomeProvider> biomeprovidertype1 = ModBiomes.ProviderTypes.OLDWEST_LAYERED.get();
        BiomeProviderType<CheckerboardBiomeProviderSettings, CheckerboardBiomeProvider> biomeprovidertype2 = BiomeProviderType.CHECKERBOARD;
        if (worldtype == WorldType.FLAT) {
            FlatGenerationSettings flatgenerationsettings = FlatGenerationSettings.getDefaultFlatGenerator();
            flatgenerationsettings.setBiome(ModBiomes.OLDWEST);
            SingleBiomeProviderSettings singlebiomeprovidersettings1 = biomeprovidertype.func_226840_a_(this.world.getWorldInfo()).setBiome(flatgenerationsettings.getBiome());
            return chunkgeneratortype.create(this.world, biomeprovidertype.create(singlebiomeprovidersettings1), flatgenerationsettings);
        } else if (worldtype == WorldType.DEBUG_ALL_BLOCK_STATES) {
            SingleBiomeProviderSettings singlebiomeprovidersettings = biomeprovidertype.func_226840_a_(this.world.getWorldInfo()).setBiome(ModBiomes.OLDWEST);
            return chunkgeneratortype1.create(this.world, biomeprovidertype.create(singlebiomeprovidersettings), chunkgeneratortype1.createSettings());
        } else if (worldtype != WorldType.BUFFET) {
            OverworldGenSettings overworldgensettings = chunkgeneratortype4.createSettings();
            OldWestBiomeProviderSettings oldWestBiomeProviderSettings = biomeprovidertype1.func_226840_a_(this.world.getWorldInfo()).setWorldInfo(this.world.getWorldInfo()).setGeneratorSettings(overworldgensettings);
            return chunkgeneratortype4.create(this.world, biomeprovidertype1.create(oldWestBiomeProviderSettings), overworldgensettings);
        } else {
            BiomeProvider biomeprovider = null;
            JsonElement jsonelement = Dynamic.convert(NBTDynamicOps.INSTANCE, JsonOps.INSTANCE, this.world.getWorldInfo().getGeneratorOptions());
            JsonObject jsonobject = jsonelement.getAsJsonObject();
            JsonObject jsonobject1 = jsonobject.getAsJsonObject("biome_source");
            if (jsonobject1 != null && jsonobject1.has("type") && jsonobject1.has("options")) {
                BiomeProviderType<?, ?> biomeprovidertype3 = Registry.BIOME_SOURCE_TYPE.getOrDefault(new ResourceLocation(jsonobject1.getAsJsonPrimitive("type").getAsString()));
                JsonObject jsonobject2 = jsonobject1.getAsJsonObject("options");
                Biome[] abiome = new Biome[]{Biomes.OCEAN};
                if (jsonobject2.has("biomes")) {
                    JsonArray jsonarray = jsonobject2.getAsJsonArray("biomes");
                    abiome = jsonarray.size() > 0 ? new Biome[jsonarray.size()] : new Biome[]{Biomes.OCEAN};

                    for(int i = 0; i < jsonarray.size(); ++i) {
                        abiome[i] = Registry.BIOME.getValue(new ResourceLocation(jsonarray.get(i).getAsString())).orElse(Biomes.OCEAN);
                    }
                }

                if (BiomeProviderType.FIXED == biomeprovidertype3) {
                    SingleBiomeProviderSettings singlebiomeprovidersettings2 = biomeprovidertype.func_226840_a_(this.world.getWorldInfo()).setBiome(abiome[0]);
                    biomeprovider = biomeprovidertype.create(singlebiomeprovidersettings2);
                }

                if (BiomeProviderType.CHECKERBOARD == biomeprovidertype3) {
                    int j = jsonobject2.has("size") ? jsonobject2.getAsJsonPrimitive("size").getAsInt() : 2;
                    CheckerboardBiomeProviderSettings checkerboardbiomeprovidersettings = biomeprovidertype2.func_226840_a_(this.world.getWorldInfo()).setBiomes(abiome).setSize(j);
                    biomeprovider = biomeprovidertype2.create(checkerboardbiomeprovidersettings);
                }

                if (BiomeProviderType.VANILLA_LAYERED == biomeprovidertype3) {
                    OldWestBiomeProviderSettings oldWestBiomeProviderSettings = biomeprovidertype1.func_226840_a_(this.world.getWorldInfo()).setGeneratorSettings(new OverworldGenSettings()).setWorldInfo(this.world.getWorldInfo());
                    biomeprovider = biomeprovidertype1.create(oldWestBiomeProviderSettings);
                }
            }

            if (biomeprovider == null) {
                biomeprovider = biomeprovidertype.create(biomeprovidertype.func_226840_a_(this.world.getWorldInfo()).setBiome(Biomes.OCEAN));
            }

            BlockState blockstate = Blocks.STONE.getDefaultState();
            BlockState blockstate1 = Blocks.WATER.getDefaultState();
            JsonObject jsonobject3 = jsonobject.getAsJsonObject("chunk_generator");
            if (jsonobject3 != null && jsonobject3.has("options")) {
                JsonObject jsonobject4 = jsonobject3.getAsJsonObject("options");
                if (jsonobject4.has("default_block")) {
                    String s = jsonobject4.getAsJsonPrimitive("default_block").getAsString();
                    blockstate = Registry.BLOCK.getOrDefault(new ResourceLocation(s)).getDefaultState();
                }

                if (jsonobject4.has("default_fluid")) {
                    String s1 = jsonobject4.getAsJsonPrimitive("default_fluid").getAsString();
                    blockstate1 = Registry.BLOCK.getOrDefault(new ResourceLocation(s1)).getDefaultState();
                }
            }

            if (jsonobject3 != null && jsonobject3.has("type")) {
                ChunkGeneratorType<?, ?> chunkgeneratortype5 = Registry.CHUNK_GENERATOR_TYPE.getOrDefault(new ResourceLocation(jsonobject3.getAsJsonPrimitive("type").getAsString()));
                if (ChunkGeneratorType.CAVES == chunkgeneratortype5) {
                    NetherGenSettings nethergensettings = chunkgeneratortype2.createSettings();
                    nethergensettings.setDefaultBlock(blockstate);
                    nethergensettings.setDefaultFluid(blockstate1);
                    return chunkgeneratortype2.create(this.world, biomeprovider, nethergensettings);
                }

                if (ChunkGeneratorType.FLOATING_ISLANDS == chunkgeneratortype5) {
                    EndGenerationSettings endgenerationsettings = chunkgeneratortype3.createSettings();
                    endgenerationsettings.setSpawnPos(new BlockPos(0, 64, 0));
                    endgenerationsettings.setDefaultBlock(blockstate);
                    endgenerationsettings.setDefaultFluid(blockstate1);
                    return chunkgeneratortype3.create(this.world, biomeprovider, endgenerationsettings);
                }
            }

            OverworldGenSettings overworldgensettings1 = chunkgeneratortype4.createSettings();
            overworldgensettings1.setDefaultBlock(blockstate);
            overworldgensettings1.setDefaultFluid(blockstate1);
            return chunkgeneratortype4.create(this.world, biomeprovider, overworldgensettings1);
        }
    }

    @Nullable
    @Override
    public BlockPos findSpawn(ChunkPos p_206920_1_, boolean checkValid) {
        for(int i = p_206920_1_.getXStart(); i <= p_206920_1_.getXEnd(); ++i) {
            for(int j = p_206920_1_.getZStart(); j <= p_206920_1_.getZEnd(); ++j) {
                BlockPos blockpos = this.findSpawn(i, j, checkValid);
                if (blockpos != null) {
                    return blockpos;
                }
            }
        }

        return null;
    }

    @Nullable
    @Override
    public BlockPos findSpawn(int p_206921_1_, int p_206921_2_, boolean checkValid) {
        BlockPos.Mutable blockpos$mutableblockpos = new BlockPos.Mutable(p_206921_1_, 0, p_206921_2_);
        Biome biome = this.world.getBiome(blockpos$mutableblockpos);
        BlockState iblockstate = biome.getSurfaceBuilderConfig().getTop();
        if (checkValid && !iblockstate.getBlock().isIn(BlockTags.VALID_SPAWN)) {
            return null;
        } else {
            Chunk chunk = this.world.getChunk(p_206921_1_ >> 4, p_206921_2_ >> 4);
            int i = chunk.getTopBlockY(Heightmap.Type.MOTION_BLOCKING, p_206921_1_ & 15, p_206921_2_ & 15);
            if (i < 0) {
                return null;
            } else if (chunk.getTopBlockY(Heightmap.Type.WORLD_SURFACE, p_206921_1_ & 15, p_206921_2_ & 15) > chunk.getTopBlockY(Heightmap.Type.OCEAN_FLOOR, p_206921_1_ & 15, p_206921_2_ & 15)) {
                return null;
            } else {
                for(int j = i + 1; j >= 0; --j) {
                    blockpos$mutableblockpos.setPos(p_206921_1_, j, p_206921_2_);
                    BlockState iblockstate1 = this.world.getBlockState(blockpos$mutableblockpos);
                    if (!iblockstate1.getFluidState().isEmpty()) {
                        break;
                    }

                    if (iblockstate1.equals(iblockstate)) {
                        return blockpos$mutableblockpos.up().toImmutable();
                    }
                }

                return null;
            }
        }
    }

    /**
     * Calculates the angle of sun and moon in the sky relative to a specified time (usually worldTime)
     *
     * @param worldTime
     * @param partialTicks
     */
    @Override
    public float calculateCelestialAngle(long worldTime, float partialTicks) {
        int i = (int)(worldTime % 24000L);
        float f = ((float)i + partialTicks) / 24000.0F - 0.25F;
        if (f < 0.0F) {
            ++f;
        }

        if (f > 1.0F) {
            --f;
        }

        float f1 = 1.0F - (float)((Math.cos((double)f * Math.PI) + 1.0D) / 2.0D);
        f = f + (f1 - f) / 3.0F;
        return f;
    }

    /**
     * Returns 'true' if in the "main surface world", but 'false' if in the Nether or End dimensions.
     */
    @Override
    public boolean isSurfaceWorld() {
        return true;
    }

    /**
     * Return Vec3D with biome specific fog color
     *
     * @param p_76562_1_
     * @param p_76562_2_
     */
    @Override
    public Vec3d getFogColor(float p_76562_1_, float p_76562_2_) {
        float f = MathHelper.cos(p_76562_1_ * ((float)Math.PI * 2F)) * 2.0F + 0.5F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        float f1 = 0.7529412F;
        float f2 = 0.84705883F;
        float f3 = 1.0F;
        f1 = f1 * (f * 0.94F + 0.06F);
        f2 = f2 * (f * 0.94F + 0.06F);
        f3 = f3 * (f * 0.91F + 0.09F);
        return new Vec3d((double)f1, (double)f2, (double)f3);
    }

    /**
     * True if the player can respawn in this dimension (true = overworld, false = nether).
     */
    @Override
    public boolean canRespawnHere() {
        return true;
    }

    /**
     * Returns true if the given X,Z coordinate should show environmental fog.
     *
     * @param x
     * @param z
     */
    @Override
    public boolean doesXZShowFog(int x, int z) {
        return false;
    }

    @Override
    public DimensionType getType() {
        return this.type;
    }
}
