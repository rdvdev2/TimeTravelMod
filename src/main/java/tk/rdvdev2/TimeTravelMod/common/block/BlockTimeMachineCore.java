package tk.rdvdev2.TimeTravelMod.common.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

public class BlockTimeMachineCore extends tk.rdvdev2.TimeTravelMod.api.timemachine.block.BlockTimeMachineCore {

    private String name = "timemachinecore";

    public BlockTimeMachineCore() {
        super(Properties.create(Material.IRON)
                .sound(SoundType.METAL)
                .hardnessAndResistance(4f)
                .lightValue(5 / 16)
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
        return 3;
    }
}
