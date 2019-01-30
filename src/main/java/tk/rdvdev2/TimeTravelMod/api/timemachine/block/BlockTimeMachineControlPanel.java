package tk.rdvdev2.TimeTravelMod.api.timemachine.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.TimeMachineHookRunner;

import javax.annotation.OverridingMethodsMustInvokeSuper;

public abstract class BlockTimeMachineControlPanel extends BlockTimeMachineComponent {

    public BlockTimeMachineControlPanel(Material material) {
        super(material);
    }

    @OverridingMethodsMustInvokeSuper
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
        if (!worldIn.isRemote && !(side == EnumFacing.UP || side == EnumFacing.DOWN)) {
            TimeMachineHookRunner hookRunner = super.getTimeMachine().hook(worldIn, pos, side);
            hookRunner.run(worldIn, playerIn, pos, side);
            return true;
        } else return false;
    }
}
