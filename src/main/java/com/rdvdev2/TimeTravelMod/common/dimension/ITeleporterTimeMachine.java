package com.rdvdev2.TimeTravelMod.common.dimension;

import com.rdvdev2.TimeTravelMod.ModBlocks;
import com.rdvdev2.TimeTravelMod.TimeTravelMod;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.ITeleporter;
import org.apache.logging.log4j.Logger;

public class ITeleporterTimeMachine implements ITeleporter {

    protected final WorldServer worldIn;
    protected final WorldServer worldOut;
    private BlockPos corePos = null;
    private BlockPos controlPos;
    private int[][] basicTMCoord = null;
    private int[][] airCoord = null;
    private Logger log = TimeTravelMod.logger;

    public ITeleporterTimeMachine(WorldServer worldIn, WorldServer worldOut, BlockPos controlPos, EnumFacing facing) {
        this.worldIn = worldIn;
        this.worldOut = worldOut;
        this.controlPos = controlPos;
        switch (facing.getName()) {
            case "north":
                this.corePos = new BlockPos(0, -2, -1);
                this.basicTMCoord = new int[][]{
                        {-1, -2, 0}, {-1, -2, -1}, {-1, -2, -2}, {0, -2, 0}, {0, -2, -2},
                        {1, -2, 0}, {1, -2, -1}, {1, -2, -2}, {-1, -1, 0}, {-1, -1, -1},
                        {-1, -1, -2}, {0, -1, 0}, {1, -1, 0}, {1, -1, -1}, {1, -1, -2},
                        {-1, 0, 0}, {-1, 0, -1}, {-1, 0, -2}, {1, 0, 0}, {1, 0, -1},
                        {1, 0, -2}, {-1, 1, 0}, {-1, 1, -1}, {-1, 1, -2}, {0, 1, 0},
                        {0, 1, -1}, {0, 1, -2}, {1, 1, 0}, {1, 1, -1}, {1, 1, -2}
                };
                this.airCoord = new int[][]{{0, -1, -1}, {0, -1, -2}, {0, 0, -1}, {0, 0, -2}};
                break;
            case "south":
                this.corePos = new BlockPos(0,-2,1);
                this.basicTMCoord = new int[][]{
                        {-1, -2, 0}, {-1, -2, 1}, {-1, -2, 2}, {0, -2, 0}, {0, -2, 2},
                        {1, -2, 0}, {1, -2, 1}, {1, -2, 2}, {-1, -1, 0}, {-1, -1, 1},
                        {-1, -1, 2}, {0, -1, 0}, {1, -1, 0}, {1, -1, 1}, {1, -1, 2},
                        {-1, 0, 0}, {-1, 0, 1}, {-1, 0, 2}, {1, 0, 0}, {1, 0, 1},
                        {1, 0, 2}, {-1, 1, 0}, {-1, 1, 1}, {-1, 1, 2}, {0, 1, 0},
                        {0, 1, 1}, {0, 1, 2}, {1, 1, 0}, {1, 1, 1}, {1, 1, 2}
                };
                this.airCoord = new int[][]{{0, -1, 1}, {0, -1, 2}, {0, 0, 1}, {0, 0, 2}};
                break;
            case "east":
                this.corePos = new BlockPos(1, -2, 0);
                this.basicTMCoord = new int[][]{
                        {0, -2, -1}, {1, -2, -1}, {2, -2, -1}, {0, -2, 0}, {2, -2, 0},
                        {0, -2, 1}, {1, -2, 1}, {2, -2, 1}, {0, -1, -1}, {1, -1, -1},
                        {2, -1, -1}, {0, -1, 0}, {0, -1, 1}, {1, -1, 1}, {2, -1, 1},
                        {0, 0, -1}, {1, 0, -1}, {2, 0, -1}, {0, 0, 1}, {1, 0, 1},
                        {2, 0, 1}, {0, 1, -1}, {1, 1, -1}, {2, 1, -1}, {0, 1, 0},
                        {1, 1, 0}, {2, 1, 0}, {0, 1, 1}, {1, 1, 1}, {2, 1, 1}
                };
                this.airCoord = new int[][]{{1, -1, 0}, {2, -1, 0}, {1, 0, 0}, {2, 0, 0}};
                break;
            case "west":
                this.corePos = new BlockPos(-1, -2,0);
                this.basicTMCoord = new int[][]{
                        {0, -2, -1}, {-1, -2, -1}, {-2, -2, -1}, {0, -2, 0}, {-2, -2, 0},
                        {0, -2, 1}, {-1, -2, 1}, {-2, -2, 1}, {0, -1, -1}, {-1, -1, -1},
                        {-2, -1, -1}, {0, -1, 0}, {0, -1, 1}, {-1, -1, 1}, {-2, -1, 1},
                        {0, 0, -1}, {-1, 0, -1}, {-2, 0, -1}, {0, 0, 1}, {-1, 0, 1},
                        {-2, 0, 1}, {0, 1, -1}, {-1, 1, -1}, {-2, 1, -1}, {0, 1, 0},
                        {-1, 1, 0}, {-2, 1, 0}, {0, 1, 1}, {-1, 1, 1}, {-2, 1, 1}
                };
                this.airCoord = new int[][]{{-1, -1, 0}, {-2, -1, 0}, {-1, 0, 0}, {-2, 0, 0}};
                break;
        }
    }

    @Override
    public void placeEntity(World world, Entity entity, float yaw)
    {
        log.info("Destroying old TM");
        destroyTM();
        log.info("Building new TM");
        buildTM();
    }

    private void buildTM() {
        log.info("Building TM Control Panel");
        worldIn.setBlockState(controlPos, ModBlocks.timeMachineControlPanel.getDefaultState());
        log.info("Building TM Core");
        worldIn.setBlockState(controlPos.add(corePos), ModBlocks.timeMachineCore.getDefaultState());
        log.info("Building TM Basic Blocks");
        for (int i = 0; i < basicTMCoord.length; i++) {
            worldIn.setBlockState(controlPos.add(basicTMCoord[i][0], basicTMCoord[i][1], basicTMCoord[i][2]), ModBlocks.timeMachineBasicBlock.getDefaultState());
        }
        log.info("Building TM Internal Air");
        for (int i = 0; i < airCoord.length; i++) {
            worldIn.setBlockToAir(controlPos.add(airCoord[i][0], airCoord[i][1], airCoord[i][2]));
        }
    }

    private void destroyTM() {
        log.info("Removing TM Control Panel");
        worldOut.setBlockToAir(controlPos);
        log.info("Removing TM Core");
        worldOut.setBlockToAir(controlPos.add(corePos));
        log.info("Removing TM Basic Blocks");
        for (int i = 0; i < basicTMCoord.length; i++) {
            worldOut.setBlockToAir(controlPos.add(basicTMCoord[i][0], basicTMCoord[i][1], basicTMCoord[i][2]));
        }
        log.info("Removing TM Internal Air (Yeah, it's a little stupid this)");
        for (int i = 0; i < airCoord.length; i++) {
            worldOut.setBlockToAir(controlPos.add(airCoord[i][0], airCoord[i][1], airCoord[i][2]));
        }
    }
}
