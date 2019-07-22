package tk.rdvdev2.TimeTravelMod.common.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.common.ToolType;
import tk.rdvdev2.TimeTravelMod.api.timemachine.block.BlockTimeMachineBasic;

import javax.annotation.Nullable;

public class BlockTimeMachineBasicBlock extends BlockTimeMachineBasic {

    String name = "timemachinebasicblock";

    public BlockTimeMachineBasicBlock() {
        super(Properties.create(Material.IRON)
                .sound(SoundType.METAL)
                .hardnessAndResistance(3f)
                .lightValue(0 / 16)
                .variableOpacity()
        );
        setRegistryName(name);
    }

    @Nullable
    @Override
    public ToolType getHarvestTool(IBlockState p_getHarvestTool_1_) {
        return ToolType.PICKAXE;
    }

    @Override
    public int getHarvestLevel(IBlockState p_getHarvestLevel_1_) {
        return 2;
    }
}