package tk.rdvdev2.TimeTravelMod.common.timemachine;

import net.minecraft.util.math.BlockPos;
import tk.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Tier1TimeMachine extends TimeMachine {

    public Tier1TimeMachine() {};

    @Override
    public int getCooldownTime() {
        return 20*20; // 20 seconds (20t = 1s)
    }

    public int getTier() {return 1;}

    public List<BlockPos> coreBlocksPos() {
        return Collections.singletonList(new BlockPos(0, -2, -1));
    }

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

    public List<BlockPos> airBlocksPos() {
        return Arrays.stream(new Integer[][]{
                {0, -1, -1}, {0, -1, -2}, {0, 0, -1}, {0, 0, -2}
        }).map((it) -> new BlockPos(it[0], it[1], it[2])).collect(Collectors.toList());
    }
}
