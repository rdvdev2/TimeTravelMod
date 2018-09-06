package com.rdvdev2.TimeTravelMod.api.timemachine.upgrade;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface TimeMachineHook {
    boolean runOriginal();
    <T> T run(T original, Object... args);

    interface RunHook extends TimeMachineHook {
        @Override
        default boolean runOriginal(){
            return false;
        };

        @Override
        default <T> T run(T original, Object... args) {
            run((World) args[0], (EntityPlayer) args[1], (BlockPos) args[2], (EnumFacing) args[3]);
            return null;
        }

        void run(World world, EntityPlayer playerIn, BlockPos controllerPos, EnumFacing side);
    }
}
