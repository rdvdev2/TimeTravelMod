package com.rdvdev2.TimeTravelMod.common.registry;

import com.rdvdev2.TimeTravelMod.api.dimension.TimeLine;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryInternal;
import net.minecraftforge.registries.RegistryManager;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nullable;
import java.util.Arrays;

public class TimeLinesCallbacks implements IForgeRegistry.AddCallback<TimeLine>, IForgeRegistry.CreateCallback<TimeLine> {

    private TimeLine[][] tierToTimeLineArray;
    public static ResourceLocation TIERTOTIMELINE = new ResourceLocation("timetravelmod:tiertotimeline");

    @Override
    public void onCreate(IForgeRegistryInternal<TimeLine> owner, RegistryManager stage) {
        tierToTimeLineArray = new TimeLine[0][];
        owner.setSlaveMap(TIERTOTIMELINE, tierToTimeLineArray);
    }

    @Override
    public void onAdd(IForgeRegistryInternal<TimeLine> owner, RegistryManager stage, int id, TimeLine obj, @Nullable TimeLine oldObj) {

        if (!ArrayUtils.contains(DimensionManager.getDimensions(obj.getDimensionType()), obj.getDimId()))
            DimensionManager.registerDimension(obj.getDimId(), obj.getDimensionType());

        // Get the tiers array
        tierToTimeLineArray = owner.getSlaveMap(TIERTOTIMELINE, TimeLine[][].class);

        // Expand the array if there is a new max tier
        if (tierToTimeLineArray.length < obj.getMinTier()+1) {
            System.out.println("Expanding array");
            int oldMax = tierToTimeLineArray.length - 1;
            TimeLine[] oldMaxTLS = null;
            if (oldMax != -1)
                oldMaxTLS = tierToTimeLineArray[oldMax];
            tierToTimeLineArray = Arrays.copyOf(tierToTimeLineArray, obj.getMinTier() + 1);
            if (oldMax != -1) {
                // Copy all the registered TimeLines to the new tiers
                System.out.println("Copying old max tier TL's");
                for (; oldMax < tierToTimeLineArray.length; oldMax++)
                    tierToTimeLineArray[oldMax] = oldMaxTLS;
            } /*else {
                // Put empty arrays in the new indexes
                System.out.println("Creating empty arrays");
                for (TimeLine[] tl:tierToTimeLineArray)
                    tl = new TimeLine[]{null};
            }*/
        }

        // Add the new TimeLine to it's valid tiers
        for (int i = obj.getMinTier(); i < tierToTimeLineArray.length; i++) {
            try {
                int j = tierToTimeLineArray[i].length;
                tierToTimeLineArray[i] = Arrays.copyOf(tierToTimeLineArray[i], j + 1);
                tierToTimeLineArray[i][j] = obj;
            } catch (NullPointerException e) {
                tierToTimeLineArray[i] = new TimeLine[]{obj};
            }
        }

        // Save the new data
        owner.setSlaveMap(TIERTOTIMELINE, tierToTimeLineArray);
    }
}
