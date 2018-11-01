package com.rdvdev2.TimeTravelMod.api.timemachine.upgrade;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface TimeMachineHook {
    <T> T run(T original, TimeMachineHookRunner tm, Object... args);

    interface RunHook extends TimeMachineHook {

        @Override
        default <T> T run(T original, TimeMachineHookRunner tm, Object... args) {
            run(tm, (World) args[0], (EntityPlayer) args[1], (BlockPos) args[2], (EnumFacing) args[3]);
            return null;
        }

        void run(TimeMachineHookRunner tm, World world, EntityPlayer playerIn, BlockPos controllerPos, EnumFacing side);
    }

    @SuppressWarnings("unchecked")
    interface TierHook extends TimeMachineHook {

        @Override
        default <T> T run(T original, TimeMachineHookRunner tm, Object... args) {
            if (original instanceof Integer) return (T)(Integer)run((Integer) original, tm);
            else throw new IllegalArgumentException("TierHook takes an int value");
        }

        int run(int original, TimeMachineHookRunner tm);
    }

    @SuppressWarnings("unchecked")
    interface EntityMaxLoadHook extends TimeMachineHook {

        @Override
        default <T> T run(T original, TimeMachineHookRunner tm, Object... args) {
            if (original instanceof Integer) return (T) (Integer)run((Integer) original, tm);
            else throw new IllegalArgumentException("EntityMaxLoadHook takes an int value");
        }

        int run(int original, TimeMachineHookRunner tm);
    }
}
