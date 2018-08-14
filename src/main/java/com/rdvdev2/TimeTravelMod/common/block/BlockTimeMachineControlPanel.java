package com.rdvdev2.TimeTravelMod.common.block;

import com.rdvdev2.TimeTravelMod.ModBlocks;
import com.rdvdev2.TimeTravelMod.ModDimensions;
import com.rdvdev2.TimeTravelMod.TimeTravelMod;
import com.rdvdev2.TimeTravelMod.client.gui.GuiTimeMachine;
import com.rdvdev2.TimeTravelMod.common.dimension.ITeleporterTimeMachine;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.OpenGuiHandler;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockTimeMachineControlPanel extends BlockTimeMachineBasicBlock{
    public BlockTimeMachineControlPanel() {
        super();
    }

    public Block setNames() {
        String name = "timemachinecontrolpanel";
        setUnlocalizedName(name);
        setRegistryName(name);
        return this;
    }

    @Override
    public boolean onBlockActivated(World worldIn,
                                    BlockPos pos,
                                    IBlockState state,
                                    EntityPlayer playerIn,
                                    EnumHand hand,
                                    EnumFacing side,
                                    float hitX,
                                    float hitY,
                                    float hitZ) {

        TimeTravelMod.logger.info(playerIn.getName() + " clicked a Time Machine Control Panel facing " + side.getName());


        BlockPos corePos = null;
        int basicTMCoord[][] = null;
        int airCoord[][] = null;
        BlockPos doorPos;
        boolean error = false;

        switch (side.getName()) {
            case "north":
                corePos = new BlockPos(0, -2, -1);
                basicTMCoord = new int[][]{
                        {-1, -2, 0}, {-1, -2, -1}, {-1, -2, -2}, {0, -2, 0}, {0, -2, -2},
                        {1, -2, 0}, {1, -2, -1}, {1, -2, -2}, {-1, -1, 0}, {-1, -1, -1},
                        {-1, -1, -2}, {0, -1, 0}, {1, -1, 0}, {1, -1, -1}, {1, -1, -2},
                        {-1, 0, 0}, {-1, 0, -1}, {-1, 0, -2}, {1, 0, 0}, {1, 0, -1},
                        {1, 0, -2}, {-1, 1, 0}, {-1, 1, -1}, {-1, 1, -2}, {0, 1, 0},
                        {0, 1, -1}, {0, 1, -2}, {1, 1, 0}, {1, 1, -1}, {1, 1, -2}
                };
                airCoord = new int[][]{{0, -1, -1}, {0, -1, -2}, {0, 0, -1}, {0, 0, -2}};
                break;
            case "south":
                corePos = new BlockPos(0,-2,1);
                basicTMCoord = new int[][]{
                        {-1, -2, 0}, {-1, -2, 1}, {-1, -2, 2}, {0, -2, 0}, {0, -2, 2},
                        {1, -2, 0}, {1, -2, 1}, {1, -2, 2}, {-1, -1, 0}, {-1, -1, 1},
                        {-1, -1, 2}, {0, -1, 0}, {1, -1, 0}, {1, -1, 1}, {1, -1, 2},
                        {-1, 0, 0}, {-1, 0, 1}, {-1, 0, 2}, {1, 0, 0}, {1, 0, 1},
                        {1, 0, 2}, {-1, 1, 0}, {-1, 1, 1}, {-1, 1, 2}, {0, 1, 0},
                        {0, 1, 1}, {0, 1, 2}, {1, 1, 0}, {1, 1, 1}, {1, 1, 2}
                };
                airCoord = new int[][]{{0, -1, 1}, {0, -1, 2}, {0, 0, 1}, {0, 0, 2}};
                break;
            case "east":
                corePos = new BlockPos(1, -2, 0);
                basicTMCoord = new int[][]{
                        {0, -2, -1}, {1, -2, -1}, {2, -2, -1}, {0, -2, 0}, {2, -2, 0},
                        {0, -2, 1}, {1, -2, 1}, {2, -2, 1}, {0, -1, -1}, {1, -1, -1},
                        {2, -1, -1}, {0, -1, 0}, {0, -1, 1}, {1, -1, 1}, {2, -1, 1},
                        {0, 0, -1}, {1, 0, -1}, {2, 0, -1}, {0, 0, 1}, {1, 0, 1},
                        {2, 0, 1}, {0, 1, -1}, {1, 1, -1}, {2, 1, -1}, {0, 1, 0},
                        {1, 1, 0}, {2, 1, 0}, {0, 1, 1}, {1, 1, 1}, {2, 1, 1}
                };
                airCoord = new int[][]{{1, -1, 0}, {2, -1, 0}, {1, 0, 0}, {2, 0, 0}};
                break;
            case "west":
                corePos = new BlockPos(-1, -2,0);
                basicTMCoord = new int[][]{
                        {0, -2, -1}, {-1, -2, -1}, {-2, -2, -1}, {0, -2, 0}, {-2, -2, 0},
                        {0, -2, 1}, {-1, -2, 1}, {-2, -2, 1}, {0, -1, -1}, {-1, -1, -1},
                        {-2, -1, -1}, {0, -1, 0}, {0, -1, 1}, {-1, -1, 1}, {-2, -1, 1},
                        {0, 0, -1}, {-1, 0, -1}, {-2, 0, -1}, {0, 0, 1}, {-1, 0, 1},
                        {-2, 0, 1}, {0, 1, -1}, {-1, 1, -1}, {-2, 1, -1}, {0, 1, 0},
                        {-1, 1, 0}, {-2, 1, 0}, {0, 1, 1}, {-1, 1, 1}, {-2, 1, 1}
                };
                airCoord = new int[][]{{-1, -1, 0}, {-2, -1, 0}, {-1, 0, 0}, {-2, 0, 0}};
                break;
            default:
                error = true;
        }

        if (!error) {
            if (worldIn.getBlockState(pos.add(corePos)) == ModBlocks.timeMachineCore.getDefaultState()) {
                TimeTravelMod.logger.info("There is a TM Core in the right place.");
                for (int i = 0; i < basicTMCoord.length; i++) {
                    if (worldIn.getBlockState(pos.add(basicTMCoord[i][0], basicTMCoord[i][1], basicTMCoord[i][2])) != ModBlocks.timeMachineBasicBlock.getDefaultState()) {
                        error = true;
                        break;
                    }
                }
                if (!error) {
                    TimeTravelMod.logger.info("The TM case is OK");
                    /*int travelTo;

                    switch (playerIn.dimension) {
                        case 0:
                            travelTo = ModDimensions.OldWestId;
                            break;
                        case 20:
                            travelTo = 0;
                            break;
                        default:
                            travelTo = -1;
                    }
                    if (playerIn instanceof EntityPlayerMP && travelTo != -1) {
                        EntityPlayerMP mp = (EntityPlayerMP)playerIn;
                        mp.getServer().getPlayerList().transferPlayerToDimension(mp, travelTo, new ITeleporterTimeMachine(mp.getServer().getWorld(travelTo), mp.getServer().getWorld(mp.dimension), pos, side));
                    }*/
                    // TODO: Implement a dimension selector GUI
                    Minecraft.getMinecraft().displayGuiScreen(new GuiTimeMachine(playerIn, pos, side));
                }
            }

        }

        return true;
    }
}
