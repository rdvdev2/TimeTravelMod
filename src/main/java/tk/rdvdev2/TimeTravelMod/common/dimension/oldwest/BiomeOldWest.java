package tk.rdvdev2.TimeTravelMod.common.dimension.oldwest;

import net.minecraft.world.biome.BiomeDesert;

public class BiomeOldWest extends BiomeDesert {
    public BiomeOldWest(BiomeProperties properties) {
        super(properties);
        this.spawnableCreatureList.clear();
        this.spawnableMonsterList.clear();
    }
}
