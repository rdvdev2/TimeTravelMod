package com.rdvdev2.TimeTravelMod.common.world.dimension;

import com.rdvdev2.TimeTravelMod.ModRegistries;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.common.ModDimension;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.apache.commons.lang3.Validate;

import java.util.function.BiFunction;

public class TimeLine extends ForgeRegistryEntry<com.rdvdev2.TimeTravelMod.api.dimension.TimeLine> implements com.rdvdev2.TimeTravelMod.api.dimension.TimeLine {

    private final ModDimension modDimension;
    private final int minTier;
    private DimensionType dimension;

    @Override
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

    @Override
    public ModDimension getModDimension() {
        return modDimension;
    }

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

    private BiFunction<World, DimensionType, ? extends Dimension> hook(BiFunction<World, DimensionType, ? extends Dimension> originial) {
        return (world, dimensionType) -> {
            this.dimension = dimensionType;
            return originial.apply(world, dimensionType);
        };
    }

    public static boolean isValidTimeLine(World world) {
        for (com.rdvdev2.TimeTravelMod.api.dimension.TimeLine timeLine : ModRegistries.TIME_LINES) {
            TimeLine tl = (TimeLine) timeLine;
            if (tl.getDimension() == world.getDimension().getType()) return true;
        }
        return false;
    }
}
