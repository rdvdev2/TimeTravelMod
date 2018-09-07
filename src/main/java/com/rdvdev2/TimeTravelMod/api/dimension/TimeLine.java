package com.rdvdev2.TimeTravelMod.api.dimension;

import com.google.common.reflect.TypeToken;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;

// TODO: JavaDoc
public abstract class TimeLine extends WorldProvider implements IForgeRegistryEntry<TimeLine> {

    private TypeToken<TimeLine> token = new TypeToken<TimeLine>(getClass()){};
    private ResourceLocation registryName = null;

    public final TimeLine setRegistryName(String name)
    {
        if (getRegistryName() != null)
            throw new IllegalStateException("Attempted to set registry name with existing registry name! New: " + name + " Old: " + getRegistryName());

        this.registryName = GameData.checkPrefix(name);
        return (TimeLine) this;
    }

    //Helper functions
    @Override
    public final TimeLine setRegistryName(ResourceLocation name){ return setRegistryName(name.toString()); }
    public final TimeLine setRegistryName(String modID, String name){ return setRegistryName(modID + ":" + name); }
    @Override
    @Nullable
    public final ResourceLocation getRegistryName()
    {
        return registryName != null ? registryName : null;
    }

    @Override
    public final Class<TimeLine> getRegistryType() { return (Class<TimeLine>) token.getRawType(); };

    private DimensionType DIMENSION_TYPE;

    private int dimId;
    private int minTier;

    public int getDimId() {
        return dimId;
    }

    public int getMinTier() {
        return minTier;
    }

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
