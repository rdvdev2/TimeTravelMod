package tk.rdvdev2.TimeTravelMod.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

public class BlockTimeCrystalOre extends Block {

    String name = "timecrystalore";

    public BlockTimeCrystalOre() {
        super(Properties.create(Material.ROCK)
                .sound(SoundType.STONE)
                .hardnessAndResistance(5f)
                .lightValue(5/16)
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
