package tk.rdvdev2.TimeTravelMod.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import tk.rdvdev2.TimeTravelMod.TimeTravelMod;

public class BlockReinforcedHeavyBlock extends Block {

    private String name = "reinforcedheavyblock";

    public BlockReinforcedHeavyBlock() {
        super(Material.IRON);
        setSoundType(SoundType.METAL);
        setHardness(10f);
        setLightLevel (0 / 16f);
        setLightOpacity(15);
        setUnlocalizedName(name);
        setCreativeTab(TimeTravelMod.tabTTM);
        setRegistryName(name);
        setHarvestLevel("pickaxe", 3);
    }
}