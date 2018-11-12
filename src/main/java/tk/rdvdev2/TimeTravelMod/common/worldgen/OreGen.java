package tk.rdvdev2.TimeTravelMod.common.worldgen;

import com.google.common.base.Predicate;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;
import tk.rdvdev2.TimeTravelMod.ModBlocks;

import java.util.Random;

public class OreGen implements IWorldGenerator {
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        switch (world.provider.getDimension()) {
            // Nether
            case -1:
                break;
            // Overworld
            case 0:
                runGenerator(ModBlocks.timeCrystalOre.getDefaultState(), 4, 1, 5, 20, BlockMatcher.forBlock(Blocks.STONE), world, random, chunkX, chunkZ);
                break;
            // End
            case 1:
                break;
            // Old West
            case 20:
                break;
            // Other mods dimensions
            default:
                break;
        }
    }
    private void runGenerator(IBlockState block, int amount, int spawnChance, int minHeight, int maxHeight, Predicate<IBlockState> replacedBlock, World world, Random random, int chunk_x, int chunk_z) {
        if (minHeight < 0 || maxHeight > 256 || minHeight > maxHeight)
            throw new IllegalArgumentException("Illegal Height Arguments for WorldGenerator");

        WorldGenMinable generator = new WorldGenMinable(block, amount, replacedBlock);
        int heightdiff = maxHeight - minHeight +1;
        for (int i = 0; i<spawnChance; i++){
            int x = chunk_x * 16 +random.nextInt(16);
            int y = minHeight +random.nextInt(heightdiff);
            int z = chunk_z +random.nextInt(16);

            generator.generate(world, random, new BlockPos(x, y, z));
        }
    }
}
