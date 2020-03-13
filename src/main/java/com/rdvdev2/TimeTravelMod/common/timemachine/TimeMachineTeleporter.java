package com.rdvdev2.TimeTravelMod.common.timemachine;

import com.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;
import net.minecraft.entity.Entity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.ITeleporter;

import java.util.function.Function;

public class TimeMachineTeleporter implements ITeleporter {

    private final com.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine tm;
    private final BlockPos pos;
    private final Direction side;
    private final boolean shouldBuild;

    public TimeMachineTeleporter(TimeMachine tm, BlockPos pos, Direction side, boolean shouldBuild) {
        this.tm = tm;
        this.pos = pos;
        this.side = side;
        this.shouldBuild = shouldBuild;
    }

    @Override
    public Entity placeEntity(Entity entity, ServerWorld currentWorld, ServerWorld destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
        tm.teleporterTasks(entity, destWorld, currentWorld, pos, side, shouldBuild);
        return repositionEntity.apply(false);
    }
}
