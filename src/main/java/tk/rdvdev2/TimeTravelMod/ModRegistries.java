package tk.rdvdev2.TimeTravelMod;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryInternal;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryManager;
import tk.rdvdev2.TimeTravelMod.api.dimension.TimeLine;
import tk.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;
import tk.rdvdev2.TimeTravelMod.api.timemachine.block.BlockTimeMachineComponent;
import tk.rdvdev2.TimeTravelMod.api.timemachine.block.BlockTimeMachineUpgrade;
import tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.TimeMachineUpgrade;
import tk.rdvdev2.TimeTravelMod.common.event.EventConfigureTimeMachineBlocks;
import tk.rdvdev2.TimeTravelMod.common.timemachine.TimeMachineCreative;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashMap;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class ModRegistries {

    public static IForgeRegistry<TimeMachine> timeMachinesRegistry;
    public static IForgeRegistry<TimeLine> timeLinesRegistry;
    public static IForgeRegistry<TimeMachineUpgrade> upgradesRegistry;
    public static ResourceLocation TIERTOTIMELINE = new ResourceLocation("timetravelmod:tiertotimeline");
    public static ResourceLocation BLOCKTOTM = new ResourceLocation("timetravelmod:blocktotm");
    public static ResourceLocation TMTOUPGRADE = new ResourceLocation("timetravelmod:tmtoupgrade");
    public static ResourceLocation UPGRADETOBLOCK = new ResourceLocation("timetravelmod:upgradetoblock");

    @SubscribeEvent
    public static void addRegistries(RegistryEvent.NewRegistry event) {
        timeLinesRegistry = new RegistryBuilder<TimeLine>()
                .setType(TimeLine.class)
                .setName(new ResourceLocation("timetravelmod:timelines"))
                .addCallback(new TimeLinesCallbacks())
                .create();

        timeMachinesRegistry = new RegistryBuilder<TimeMachine>()
                .setType(TimeMachine.class)
                .setName(new ResourceLocation("timetravelmod:ztimemachines"))
                .addCallback(new TimeMachinesCallbacks())
                .create();

        upgradesRegistry = new RegistryBuilder<TimeMachineUpgrade>()
                .setType(TimeMachineUpgrade.class)
                .setName(new ResourceLocation("timetravelmod:tmupgrades"))
                .addCallback(new TimeMachineUpgradesCallbacks())
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

            // Get the tiers array
            tierToTimeLineArray = owner.getSlaveMap(TIERTOTIMELINE, TimeLine[][].class);

            // Expand the array if there is a new max tier
            if (tierToTimeLineArray.length < obj.getMinTier()+1) {
                int oldMax = tierToTimeLineArray.length - 1;
                TimeLine[] oldMaxTLS = null;
                if (oldMax != -1)
                    oldMaxTLS = tierToTimeLineArray[oldMax];
                tierToTimeLineArray = Arrays.copyOf(tierToTimeLineArray, obj.getMinTier() + 1);
                if (oldMax != -1) {
                    // Copy all the registered TimeLines to the new tiers
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

    public static class TimeMachinesCallbacks implements IForgeRegistry.CreateCallback<TimeMachine>, IForgeRegistry.AddCallback<TimeMachine>, IForgeRegistry.BakeCallback {

        private HashMap<IBlockState, ResourceLocation> blockStateResourceLocationHashMap;

        @Override
        public void onCreate(IForgeRegistryInternal<TimeMachine> owner, RegistryManager stage) {
            blockStateResourceLocationHashMap = new HashMap<>();
            owner.setSlaveMap(BLOCKTOTM, blockStateResourceLocationHashMap.clone());
        }

        @SuppressWarnings("unchecked")
        @Override
        public void onAdd(IForgeRegistryInternal owner, RegistryManager stage, int id, TimeMachine obj, @Nullable TimeMachine oldObj) {
            blockStateResourceLocationHashMap = (HashMap<IBlockState, ResourceLocation>)owner.getSlaveMap(BLOCKTOTM, HashMap.class);
            if (obj instanceof TimeMachineCreative) return; // Special rule for the creative Time Machine
            if (!blockStateResourceLocationHashMap.containsValue(obj.getRegistryName())) {
                for(IBlockState block:obj.getBlocks()) {
                    if (block.getBlock() instanceof BlockTimeMachineUpgrade) continue; // Time Machine Upgrade blocks must be ignored
                    if (!blockStateResourceLocationHashMap.containsKey(block)) {
                        blockStateResourceLocationHashMap.put(block, obj.getRegistryName());
                    } else {
                        throw new RuntimeException(obj.getRegistryName()+" tryed to register with block "+block.toString()+", but it is already registered to "+ GameRegistry.findRegistry(Block.class).getValue(blockStateResourceLocationHashMap.get(block)).toString());
                    }
                }
            }
        }

        @Override
        public void onBake(IForgeRegistryInternal owner, RegistryManager stage) {
            EVENT_BUS.post(new EventConfigureTimeMachineBlocks());
        }
    }

    public static class TimeMachineUpgradesCallbacks implements IForgeRegistry.CreateCallback<TimeMachineUpgrade>, IForgeRegistry.AddCallback<TimeMachineUpgrade> {

        private HashMap<TimeMachine, TimeMachineUpgrade[]> tmtoupgradehm;
        private HashMap<TimeMachineUpgrade, BlockTimeMachineComponent[]> upgradetoblockhm;

        @Override
        public void onCreate(IForgeRegistryInternal<TimeMachineUpgrade> owner, RegistryManager stage) {
            tmtoupgradehm = new HashMap<>();
            owner.setSlaveMap(TMTOUPGRADE, tmtoupgradehm);
            upgradetoblockhm = new HashMap<>();
            owner.setSlaveMap(UPGRADETOBLOCK, upgradetoblockhm);
        }

        @SuppressWarnings("unchecked")
        @Override
        public void onAdd(IForgeRegistryInternal<TimeMachineUpgrade> owner, RegistryManager stage, int id, TimeMachineUpgrade obj, @Nullable TimeMachineUpgrade oldObj) {
            tmtoupgradehm = (HashMap<TimeMachine, TimeMachineUpgrade[]>) owner.getSlaveMap(TMTOUPGRADE, HashMap.class);
            for (TimeMachine tm:obj.getCompatibleTMs()) {
                if (tmtoupgradehm.containsKey(tm)) {
                    TimeMachineUpgrade[] upgrades = tmtoupgradehm.get(tm);
                    int index = upgrades.length;
                    upgrades = Arrays.copyOf(upgrades, index+1);
                    upgrades[index] = obj;
                    tmtoupgradehm.put(tm, upgrades);
                } else {
                    tmtoupgradehm.put(tm, new TimeMachineUpgrade[]{obj});
                }
            }
        }
    }
}
