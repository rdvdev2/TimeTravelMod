package com.rdvdev2.TimeTravelMod.common.timemachine;

import com.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;

public class TimeMachineTier1 extends TimeMachine {

    public TimeMachineTier1() {};

    public int getTier() {return 1;}

    public int[][] coreBlocksPos() {
        return new int[][]{{0, -2, -1}};
    }

    public int[][] basicBlocksPos() {
        return new int[][]{
                {-1, -2, 0}, {-1, -2, -1}, {-1, -2, -2}, {0, -2, 0}, {0, -2, -2},
                {1, -2, 0}, {1, -2, -1}, {1, -2, -2}, {-1, -1, 0}, {-1, -1, -1},
                {-1, -1, -2}, {0, -1, 0}, {1, -1, 0}, {1, -1, -1}, {1, -1, -2},
                {-1, 0, 0}, {-1, 0, -1}, {-1, 0, -2}, {1, 0, 0}, {1, 0, -1},
                {1, 0, -2}, {-1, 1, 0}, {-1, 1, -1}, {-1, 1, -2}, {0, 1, 0},
                {0, 1, -1}, {0, 1, -2}, {1, 1, 0}, {1, 1, -1}, {1, 1, -2}
        };
    }

    public int[][] airBlocksPos() {
        return new int[][]{{0, -1, -1}, {0, -1, -2}, {0, 0, -1}, {0, 0, -2}};
    }
}
