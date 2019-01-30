package tk.rdvdev2.TimeTravelMod.common.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import tk.rdvdev2.TimeTravelMod.TimeTravelMod;
import tk.rdvdev2.TimeTravelMod.api.timemachine.block.BlockTimeMachineBasic;

public class BlockTimeMachineBasicBlock extends BlockTimeMachineBasic {

    String name = "timemachinebasicblock";

    public BlockTimeMachineBasicBlock() {
        super(Material.IRON);
        setSoundType(SoundType.METAL);
        setHardness(3f);
        setLightLevel (0 / 16f);
        setLightOpacity(15);
        setCreativeTab(TimeTravelMod.tabTTM);
        setHarvestLevel("pickaxe", 2);
        setUnlocalizedName(name);
        setRegistryName(name);
    }
}
