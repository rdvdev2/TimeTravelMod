package tk.rdvdev2.TimeTravelMod.common.world.dimension;

import net.minecraft.entity.Entity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorld;
import net.minecraft.world.Teleporter;
import tk.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;

public class ITeleporterTimeMachine extends Teleporter {

    protected final ServerWorld worldIn;
    protected final ServerWorld worldOut;
    private TimeMachine tm;
    private BlockPos controlPos;
    private Direction facing;

    public ITeleporterTimeMachine(ServerWorld worldIn, ServerWorld worldOut, TimeMachine tm, BlockPos controlPos, Direction facing) {
        super(worldOut);
        this.worldIn = worldIn;
        this.worldOut = worldOut;
        this.tm = tm.hook(worldIn, controlPos, facing);
        this.controlPos = controlPos;
        this.facing = facing;
    }

    @Override
    public boolean func_222268_a(Entity entity, float p_222268_2_) {
        tm.teleporterTasks(entity, worldIn, worldOut, controlPos, facing);
        return true;
    }
}
