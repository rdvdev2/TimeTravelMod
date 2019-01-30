package tk.rdvdev2.TimeTravelMod.common.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import tk.rdvdev2.TimeTravelMod.TimeTravelMod;

public class BlockTimeMachineControlPanel extends tk.rdvdev2.TimeTravelMod.api.timemachine.block.BlockTimeMachineControlPanel {

    private String name = "timemachinecontrolpanel";

    public BlockTimeMachineControlPanel() {
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
