package tk.rdvdev2.TimeTravelMod.common.timemachine;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import tk.rdvdev2.TimeTravelMod.ModRegistries;
import tk.rdvdev2.TimeTravelMod.TimeTravelMod;
import tk.rdvdev2.TimeTravelMod.api.dimension.TimeLine;
import tk.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;

public class TimeMachineCreative extends TimeMachine {

    @Override
    public int getCooldownTime() {
        return 0;
    }

    @Override
    public int getTier() {
        return ModRegistries.timeLinesRegistry.getSlaveMap(ModRegistries.TIERTOTIMELINE, TimeLine[][].class).length -1;
    }

    @Override
    public int[][] coreBlocksPos() {
        return new int[0][0];
    }

    @Override
    public int[][] basicBlocksPos() {
        return new int[0][0];
    }

    @Override
    public int[][] airBlocksPos() {
        return new int[0][0];
    }

    @Override
    public void run(World world, EntityPlayer playerIn, BlockPos controllerPos, EnumFacing side) {
        if (isPlayerInside(world, controllerPos, side, playerIn) &&
                !isOverloaded(world, controllerPos, side)) {
            TimeTravelMod.proxy.displayTMGuiScreen(playerIn, this, controllerPos, side);
        }
    }

    @Override
    public boolean isBuilt(World world, BlockPos controllerPos, EnumFacing side) {
        return true;
    }

    @Override
    public AxisAlignedBB getAirSpace(BlockPos controllerPos, EnumFacing side) {
        return new AxisAlignedBB(
                controllerPos.getX() -1,
                controllerPos.getY() -1,
                controllerPos.getZ() -1,
                controllerPos.getX() +1,
                controllerPos.getY() +1,
                controllerPos.getZ() +1
        );
    }

    @Override
    public boolean isPlayerInside(World world, BlockPos controllerPos, EnumFacing side, EntityPlayer player) {
        return true;
    }

    @Override
    public void teleporterTasks(World worldIn, World worldOut, BlockPos controllerPos, EnumFacing side) {
        Chunk chunk = worldIn.getChunk(controllerPos);
        worldIn.getChunkProvider().provideChunk(chunk.x, chunk.z, true, false);
    }

    @Override
    public boolean isOverloaded(World world, BlockPos controllerPos, EnumFacing side) {
        return false;
    }
}
