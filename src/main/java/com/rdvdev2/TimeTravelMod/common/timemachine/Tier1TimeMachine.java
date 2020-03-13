package com.rdvdev2.TimeTravelMod.common.timemachine;

import com.rdvdev2.TimeTravelMod.ModBlocks;
import com.rdvdev2.TimeTravelMod.api.timemachine.TimeMachineTemplate;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Tier1TimeMachine implements TimeMachineTemplate {

    public Tier1TimeMachine() {}

    @Override
    public List<BlockPos> coreBlocksPos() {
        return Collections.singletonList(new BlockPos(0, -2, -1));
    }

    @Override
    public List<BlockPos> basicBlocksPos() {
        return Arrays.stream(new Integer[][]{
                {-1, -2, 0}, {-1, -2, -1}, {-1, -2, -2}, {0, -2, 0}, {0, -2, -2},
                {1, -2, 0}, {1, -2, -1}, {1, -2, -2}, {-1, -1, 0}, {-1, -1, -1},
                {-1, -1, -2}, {0, -1, 0}, {1, -1, 0}, {1, -1, -1}, {1, -1, -2},
                {-1, 0, 0}, {-1, 0, -1}, {-1, 0, -2}, {1, 0, 0}, {1, 0, -1},
                {1, 0, -2}, {-1, 1, 0}, {-1, 1, -1}, {-1, 1, -2}, {0, 1, 0},
                {0, 1, -1}, {0, 1, -2}, {1, 1, 0}, {1, 1, -1}, {1, 1, -2}
        }).map((it) -> new BlockPos(it[0], it[1], it[2])).collect(Collectors.toList());
    }

    @Override
    public List<BlockPos> airBlocksPos() {
        return Arrays.stream(new Integer[][]{
                {0, -1, -1}, {0, -1, -2}, {0, 0, -1}, {0, 0, -2}
        }).map((it) -> new BlockPos(it[0], it[1], it[2])).collect(Collectors.toList());
    }

    @Override
    public BlockState[] getControllerBlocks() {
        return new BlockState[]{ModBlocks.TIME_MACHINE_CONTROL_PANEL.get().getDefaultState()};
    }

    @Override
    public BlockState[] getCoreBlocks() {
        return new BlockState[]{ModBlocks.TIME_MACHINE_CORE.get().getDefaultState()};
    }

    @Override
    public BlockState[] getBasicBlocks() {
        return new BlockState[]{ModBlocks.TIME_MACHINE_BASIC_BLOCK.get().getDefaultState()};
    }
}
