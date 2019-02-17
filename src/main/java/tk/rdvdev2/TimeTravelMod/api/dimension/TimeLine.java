package tk.rdvdev2.TimeTravelMod.api.dimension;

import com.google.common.reflect.TypeToken;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;

/**
 * This class defines a time line. It's the dimension's world provider. It must be registered in the time line registry instead of the dimension manager.
 */
public abstract class TimeLine extends WorldProvider implements IForgeRegistryEntry<TimeLine> {

    private TypeToken<TimeLine> token = new TypeToken<TimeLine>(getClass()){};
    private ResourceLocation registryName = null;

    /**
     * Sets the name of the time line in the registry
     * @param name The name in the registry (String)
     * @return The time line
     */
    public final TimeLine setRegistryName(String name)
    {
        if (getRegistryName() != null)
            throw new IllegalStateException("Attempted to set registry name with existing registry name! New: " + name + " Old: " + getRegistryName());

        this.registryName = GameData.checkPrefix(name);
        return (TimeLine) this;
    }

    /**
     * Sets the name of the time line in the registry
     * @param name The name in the registry (ResourceLocation)
     * @return The time line
     */
    @Override
    public final TimeLine setRegistryName(ResourceLocation name){ return setRegistryName(name.toString()); }

    /**
     * Sets the name of the time line in the registry
     * @param modID The modid (String)
     * @param name The name in the registry (String)
     * @return
     */
    public final TimeLine setRegistryName(String modID, String name){ return setRegistryName(modID + ":" + name); }

    /**
     * Gets the time line registry name
     * @return The registry name
     */
    @Override
    @Nullable
    public final ResourceLocation getRegistryName()
    {
        return registryName != null ? registryName : null;
    }

    /**
     * Gets the registry type
     * @return The registry type class
     */
    @Override
    public final Class<TimeLine> getRegistryType() { //noinspection unchecked
        return (Class<TimeLine>) token.getRawType(); };

    private DimensionType DIMENSION_TYPE;

    private int dimId;
    private int minTier;

    /**
     * Gets the dimension id in the game
     * @return The dimension id
     */
    public int getDimId() {
        return dimId;
    }

    /**
     * Gets the minimum Time Machine tier required to travel to this time line
     * @return The minimum tier
     */
    public int getMinTier() {
        return minTier;
    }

    /**
     * Constructor of the Time Line
     * @param dimId The desired dimension id
     * @param dimType The desired dimension type
     * @param minTier The desired minimum tier
     */
    public TimeLine(int dimId, DimensionType dimType, int minTier) {
        super();
        this.dimId = dimId;
        this.DIMENSION_TYPE = dimType;
        this.minTier = minTier;
    }

    @OverridingMethodsMustInvokeSuper
    public void init() {
        this.setDimension(dimId);
    }

    @Override
    public final boolean canRespawnHere() {
        return true;
    }

    @Override
    public final int getRespawnDimension(EntityPlayerMP player) {
        return dimId;
    }

    @Override
    public boolean isSurfaceWorld() {
        return true;
    }

    public final DimensionType getDimensionType() {
        return DIMENSION_TYPE;
    }
}
