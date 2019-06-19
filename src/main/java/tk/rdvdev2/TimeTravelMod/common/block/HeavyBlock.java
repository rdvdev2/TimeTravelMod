package tk.rdvdev2.TimeTravelMod.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

public class HeavyBlock extends Block {

    private String name = "heavyblock";

    public HeavyBlock() {
        super(Properties
                .create(Material.IRON)
                .sound(SoundType.METAL)
                .hardnessAndResistance(7f)
                .lightValue(0/16)
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
