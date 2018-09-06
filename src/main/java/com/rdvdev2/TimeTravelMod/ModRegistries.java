package com.rdvdev2.TimeTravelMod;

import com.rdvdev2.TimeTravelMod.api.dimension.TimeLine;
import com.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;
import com.rdvdev2.TimeTravelMod.api.timemachine.upgrade.TimeMachineUpgrade;
import com.rdvdev2.TimeTravelMod.common.timemachine.TimeMachineCreative;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryInternal;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryManager;
import org.apache.commons.lang3.ArrayUtils;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashMap;

@Mod.EventBusSubscriber(modid="timetravelmod")
public class ModRegistries {

    public static IForgeRegistry<TimeMachine> timeMachinesRegistry;
    public static IForgeRegistry<TimeLine> timeLinesRegistry;
    public static IForgeRegistry<TimeMachineUpgrade> upgradesRegistry;
    public static ResourceLocation TIERTOTIMELINE = new ResourceLocation("timetravelmod:tiertotimeline");
    public static ResourceLocation BLOCKTOTM = new ResourceLocation("timetravelmod:blocktotm");

    @SubscribeEvent
    public static void addRegistries(RegistryEvent.NewRegistry event) {
        timeLinesRegistry = new RegistryBuilder<TimeLine>()
                .setType(TimeLine.class)
                .setName(new ResourceLocation("timetravelmod:timelines"))
                .addCallback(new TimeLinesCallbacks())
                .create();

        timeMachinesRegistry = new RegistryBuilder<TimeMachine>()
                .setType(TimeMachine.class)
                .setName(new ResourceLocation("timetravelmod:timemachines"))
                .addCallback(new TimeMachinesCallbacks())
                .create();

        upgradesRegistry = new RegistryBuilder<TimeMachineUpgrade>()
                .setType(TimeMachineUpgrade.class)
                .setName(new ResourceLocation("timetravelmod:tmupgrades"))
                .create();
    }

    public static class TimeLinesCallbacks implements IForgeRegistry.AddCallback<TimeLine>, IForgeRegistry.CreateCallback<TimeLine> {

        private TimeLine[][] tierToTimeLineArray;

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

    public static class TimeMachinesCallbacks implements IForgeRegistry.CreateCallback<TimeMachine>, IForgeRegistry.AddCallback<TimeMachine> {

        private HashMap<IBlockState, ResourceLocation> blockStateResourceLocationHashMap;

        @Override
        public void onCreate(IForgeRegistryInternal<TimeMachine> owner, RegistryManager stage) {
            blockStateResourceLocationHashMap = new HashMap<>();
            owner.setSlaveMap(BLOCKTOTM, blockStateResourceLocationHashMap.clone());
        }

        @Override
        public void onAdd(IForgeRegistryInternal owner, RegistryManager stage, int id, TimeMachine obj, @Nullable TimeMachine oldObj) {
            blockStateResourceLocationHashMap = (HashMap<IBlockState, ResourceLocation>)owner.getSlaveMap(BLOCKTOTM, HashMap.class);
            if (obj instanceof TimeMachineCreative) return; // Special rule for the creative Time Machine
            if (!blockStateResourceLocationHashMap.containsValue(obj.getRegistryName())) {
                for(IBlockState block:obj.getBlocks()) {
                    if (!blockStateResourceLocationHashMap.containsKey(block)) {
                        blockStateResourceLocationHashMap.put(block, obj.getRegistryName());
                    } else {
                        throw new RuntimeException(obj.getRegistryName()+" tryed to register with block "+block.toString()+", but it is already registered to "+ GameRegistry.findRegistry(Block.class).getValue(blockStateResourceLocationHashMap.get(block)).toString());
                    }
                }
            }
        }
    }
}
