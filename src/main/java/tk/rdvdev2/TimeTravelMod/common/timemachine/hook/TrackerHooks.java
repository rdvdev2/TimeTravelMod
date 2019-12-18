package tk.rdvdev2.TimeTravelMod.common.timemachine.hook;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tk.rdvdev2.TimeTravelMod.ModBlocks;
import tk.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;
import tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.TimeMachineHook;
import tk.rdvdev2.TimeTravelMod.common.block.tileentity.TimeMachineRecallerTileEntity;

import java.util.Set;

import static tk.rdvdev2.TimeTravelMod.ModTimeMachines.Upgrades.TRACKER;
import static tk.rdvdev2.TimeTravelMod.common.block.TimeMachineRecallerBlock.CONFIGURED;

public class TrackerHooks {

    public static final TimeMachineHook[] HOOKS = {new TeleporterHook()};

    public static class TeleporterHook implements TimeMachineHook.TeleporterTasks {

        @Override
        public void run(TimeMachine tm, Entity entity, World worldIn, World worldOut, BlockPos controllerPos, Direction side, boolean shouldBuild) {
            tm.removeHooks().teleporterTasks(entity, worldIn, worldOut, controllerPos, side, shouldBuild);
            if (shouldBuild) {
                Set<BlockPos> upgrades = tm.getUpgradePos(TRACKER);
                for (BlockPos pos: upgrades) {
                    for (Direction direction: Direction.values()) {
                        BlockPos recaller = pos.offset(direction);
                        if (worldOut.getBlockState(recaller).getBlock() == ModBlocks.TIME_MACHINE_RECALLER) {
                            worldOut.setBlockState(recaller, worldOut.getBlockState(recaller).with(CONFIGURED, true));
                            TileEntity tile = worldOut.getTileEntity(recaller);
                            if (tile instanceof TimeMachineRecallerTileEntity) {
                                ((TimeMachineRecallerTileEntity) tile).setControllerPos(controllerPos);
                                ((TimeMachineRecallerTileEntity) tile).setSide(side);
                                ((TimeMachineRecallerTileEntity) tile).setDest(worldIn.getDimension().getType());
                            }
                        }
                    }
                }
            }
        }
    }
}
