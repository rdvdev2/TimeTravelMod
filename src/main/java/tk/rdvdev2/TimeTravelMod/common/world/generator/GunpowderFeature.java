package tk.rdvdev2.TimeTravelMod.common.world.generator;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import tk.rdvdev2.TimeTravelMod.ModBlocks;

import java.util.Random;

public class GunpowderFeature extends Feature<NoFeatureConfig> {

    public GunpowderFeature() {
        super(null);
    }

    @Override
    public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> chunkGenerator, Random random, BlockPos pos, NoFeatureConfig noFeatureConfig) {
        if (world.hasWater(pos)) return false;
        for (int i = random.nextInt(8) + 2; i > 0; i--) {
            BlockPos thisPos = pos.add(random.nextInt(5)-2, 0, random.nextInt(5)-2);
            thisPos = world.getHeight(Heightmap.Type.WORLD_SURFACE_WG, thisPos);
            if (random.nextFloat() > 0.2) {
                setBlockState(world, thisPos, ModBlocks.gunpowderWire.getDefaultState());
            } else {
                setBlockState(world, thisPos, Blocks.TNT.getDefaultState());
            }
        }
        return true;
    }
}
