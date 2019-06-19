package tk.rdvdev2.TimeTravelMod.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

public class TimeCrystalOreBlock extends Block {

    String name = "timecrystalore";

    public TimeCrystalOreBlock() {
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
    public ToolType getHarvestTool(BlockState p_getHarvestTool_1_) {
        return ToolType.PICKAXE;
    }

    @Override
    public int getHarvestLevel(BlockState p_getHarvestLevel_1_) {
        return 3;
    }
}
