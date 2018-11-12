package tk.rdvdev2.TimeTravelMod.common.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import tk.rdvdev2.TimeTravelMod.TimeTravelMod;
import tk.rdvdev2.TimeTravelMod.api.timemachine.block.BlockTimeMachineComponent;
import tk.rdvdev2.TimeTravelMod.api.timemachine.block.EnumTimeMachineComponentType;

public class BlockTimeMachineCore extends BlockTimeMachineComponent {

    private String name = "timemachinecore";

    public BlockTimeMachineCore() {
        super(Material.IRON, EnumTimeMachineComponentType.CORE);
        setSoundType(SoundType.METAL);
        setHarvestLevel("pickaxe", 3);
        setLightLevel(5 / 16f);
        setLightOpacity(15);
        setHardness(4f);
        setCreativeTab(TimeTravelMod.tabTTM);
        setUnlocalizedName(name);
        setRegistryName(name);
    }
}
