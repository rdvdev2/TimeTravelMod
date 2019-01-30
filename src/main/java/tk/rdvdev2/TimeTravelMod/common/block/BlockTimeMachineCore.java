package tk.rdvdev2.TimeTravelMod.common.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import tk.rdvdev2.TimeTravelMod.TimeTravelMod;

public class BlockTimeMachineCore extends tk.rdvdev2.TimeTravelMod.api.timemachine.block.BlockTimeMachineCore {

    private String name = "timemachinecore";

    public BlockTimeMachineCore() {
        super(Material.IRON);
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
