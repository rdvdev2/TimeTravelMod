package tk.rdvdev2.TimeTravelMod.common.world.generator;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.IChunkGenSettings;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import tk.rdvdev2.TimeTravelMod.ModBlocks;

import java.util.Random;

public class FeatureGunpowder extends Feature<NoFeatureConfig> {

    public FeatureGunpowder() {
        super(true);
    }

    @Override
    public boolean func_212245_a(IWorld world, IChunkGenerator<? extends IChunkGenSettings> generator, Random random, BlockPos pos, NoFeatureConfig config) {
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
