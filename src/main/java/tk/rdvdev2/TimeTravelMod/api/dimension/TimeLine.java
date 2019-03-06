package tk.rdvdev2.TimeTravelMod.api.dimension;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.registries.IForgeRegistryEntry;
import tk.rdvdev2.TimeTravelMod.ModRegistries;

import javax.annotation.Nullable;
import java.util.Iterator;

/**
 * This class defines a time line. It's the dimension's world provider. It must be registered in the time line registry instead of the dimension manager.
 */
public abstract class TimeLine implements IForgeRegistryEntry<TimeLine> {

    private int minTier;
    private ModDimension dimension;
    private TimeLine registeredInstance = null;

    /**
     * Gets the minimum Time Machine tier required to travel to this time line
     * @return The minimum tier
     */
    public int getMinTier() {
        return minTier;
    }

    public ModDimension getDimension() {
        return dimension;
    }

    /**
     * Constructor of the Time Line
     * @param minTier The desired minimum tier
     */
    public TimeLine(ModDimension dimension, int minTier) {
        super();
        this.dimension = dimension;
        this.minTier = minTier;
    }

    // FORGE REGISTRY

    private ResourceLocation REGISTRY_NAME = null;

    /**
     * Sets a unique name for this Item. This should be used for uniquely identify the instance of the Item.
     * This is the valid replacement for the atrocious 'getUnlocalizedName().substring(6)' stuff that everyone does.
     * Unlocalized names have NOTHING to do with unique identifiers. As demonstrated by vanilla blocks and items.
     * <p>
     * The supplied name will be prefixed with the currently active mod's modId.
     * If the supplied name already has a prefix that is different, it will be used and a warning will be logged.
     * <p>
     * If a name already exists, or this Item is already registered in a registry, then an IllegalStateException is thrown.
     * <p>
     * Returns 'this' to allow for chaining.
     *
     * @param name Unique registry name
     * @return This instance
     */
    @Override
    public TimeLine setRegistryName(ResourceLocation name) {
        if (REGISTRY_NAME == null)
            REGISTRY_NAME = name;
        else
            throw new RuntimeException("Tryed to change a TimeLine registry name");
        return this;
    }

    /**
     * A unique identifier for this entry, if this entry is registered already it will return it's official registry name.
     * Otherwise it will return the name set in setRegistryName().
     * If neither are valid null is returned.
     *
     * @return Unique identifier or null.
     */
    @Nullable
    @Override
    public ResourceLocation getRegistryName() {
        return REGISTRY_NAME;
    }

    /**
     * Determines the type for this entry, used to look up the correct registry in the global registries list as there can only be one
     * registry per concrete class.
     *
     * @return Root registry type.
     */
    @Override
    public Class<TimeLine> getRegistryType() {
        return (Class<TimeLine>)getClass();
    }

    public static boolean isValidTimeLine(World world) {
        Iterator<TimeLine> iterator = ModRegistries.timeLinesRegistry.iterator();
        while (iterator.hasNext()) {
            TimeLine tl = iterator.next();
            if (tl.getDimension() == world.getDimension().getType().getModType() || world.getDimension().getType() == DimensionType.OVERWORLD) return true;
        }
        return false;
    }
}
