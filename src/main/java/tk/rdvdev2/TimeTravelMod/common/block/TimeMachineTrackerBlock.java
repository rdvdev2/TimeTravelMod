package tk.rdvdev2.TimeTravelMod.common.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;
import tk.rdvdev2.TimeTravelMod.ModTimeMachines;
import tk.rdvdev2.TimeTravelMod.api.timemachine.block.AbstractTimeMachineUpgradeBlock;
import tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.TimeMachineUpgrade;

import static tk.rdvdev2.TimeTravelMod.TimeTravelMod.MODID;

public class TimeMachineTrackerBlock extends AbstractTimeMachineUpgradeBlock {

    public TimeMachineTrackerBlock() {
        super(Properties.create(Material.IRON)
                .sound(SoundType.METAL)
                .hardnessAndResistance(3f)
                .lightValue(0 / 16)
                .variableOpacity()
                .harvestTool(ToolType.PICKAXE)
                .harvestLevel(2)
        );
        setRegistryName(MODID, "timemachinetracker");
    }

    /**
     * Returns the attached upgrade
     *
     * @return The attached upgrade
     */
    @Override
    public TimeMachineUpgrade getUpgrade() {
        return ModTimeMachines.Upgrades.TRACKER;
    }
}
