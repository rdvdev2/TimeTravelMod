package com.rdvdev2.TimeTravelMod.common.dimension;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;

import javax.annotation.OverridingMethodsMustInvokeSuper;

public abstract class TimeLine extends WorldProvider {
    public DimensionType DIMENSION_TYPE;

    public int dimId;
    private int modRegistryId = -1;
    private int minTier;

    public int getDimId() {
        return dimId;
    }

    public int getMinTier() {
        return minTier;
    }

    public TimeLine() {
        super();
    }

    public final void setModRegistryId(int id) {
        modRegistryId = modRegistryId == -1 ? id : throw_id();
    }

    public final int getModRegistryId() {
        return modRegistryId;
    }

    public int throw_id() {
        throw new RuntimeException("The registry id of a TimeLine can't be changed once is set");
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
}
