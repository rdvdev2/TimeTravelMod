package tk.rdvdev2.TimeTravelMod.api.dimension;

import net.minecraft.network.PacketBuffer;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.apache.commons.lang3.Validate;
import tk.rdvdev2.TimeTravelMod.ModRegistries;

import java.util.Iterator;
import java.util.function.BiFunction;

/**
 * This class defines a time line. It's the dimension's world provider. It must be registered in the time line registry instead of the dimension manager.
 */
public abstract class TimeLine extends ForgeRegistryEntry<TimeLine> {

    private final ModDimension modDimension;
    private final int minTier;
    private DimensionType dimension;

    /**
     * Gets the minimum Time Machine tier required to travel to this time line
     * @return The minimum tier
     */
    public int getMinTier() {
        return minTier;
    }

    public DimensionType getDimension() {
        if (this.dimension != null) {
            return this.dimension;
        } else {
            return DimensionType.byName(this.modDimension.getRegistryName());
        }
    }

    public ModDimension getModDimension() {
        return modDimension;
    }

    /**
     * Constructor of the Time Line
     * @param minTier The desired minimum tier
     */
    public TimeLine(int minTier, ModDimension modDimension) {
        this.minTier = minTier;
        if (modDimension == null) { // Special case for Present, because Overworld doesn't have a ModDimension
            this.modDimension = null;
            this.dimension = DimensionType.OVERWORLD;
            return;
        }
        Validate.notNull(modDimension.getRegistryName(), "Mod Dimension must have a Registry Name assigned!");
        this.modDimension = new ModDimension() {
            @Override
            public BiFunction<World, DimensionType, ? extends Dimension> getFactory() {
                return hook(modDimension.getFactory());
            }

            @Override
            public void write(PacketBuffer buffer, boolean network) {
                modDimension.write(buffer, network);
            }

            @Override
            public void read(PacketBuffer buffer, boolean network) {
                modDimension.read(buffer, network);
            }
        };
        this.modDimension.setRegistryName(modDimension.getRegistryName());
    }

    private final BiFunction<World, DimensionType, ? extends Dimension> hook(BiFunction<World, DimensionType, ? extends Dimension> originial) {
        return (world, dimensionType) -> {
            this.dimension = dimensionType;
            return originial.apply(world, dimensionType);
        };
    }

    public static boolean isValidTimeLine(IWorld world) {
        Iterator<TimeLine> iterator = ModRegistries.TIME_LINES.iterator();
        while (iterator.hasNext()) {
            TimeLine tl = iterator.next();
            if (tl.getDimension() == world.getDimension().getType()) return true;
        }
        return false;
    }
}
