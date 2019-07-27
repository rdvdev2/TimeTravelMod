package tk.rdvdev2.TimeTravelMod;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
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
import tk.rdvdev2.TimeTravelMod.api.timemachine.block.AbstractTimeMachineComponentBlock;
import tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.TimeMachineUpgrade;
import tk.rdvdev2.TimeTravelMod.common.timemachine.CreativeTimeMachine;

import javax.annotation.Nullable;
import java.util.HashMap;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class ModRegistries {

    public static IForgeRegistry<TimeMachine> timeMachinesRegistry;
    public static IForgeRegistry<TimeLine> timeLinesRegistry;
    public static IForgeRegistry<TimeMachineUpgrade> upgradesRegistry;
    public static ResourceLocation CONTROLLERTOTM = new ResourceLocation("timetravelmod:blocktotm");
    public static ResourceLocation UPGRADETOBLOCK = new ResourceLocation("timetravelmod:upgradetoblock");

    @SubscribeEvent
    public static void addRegistries(RegistryEvent.NewRegistry event) {
        timeLinesRegistry = new RegistryBuilder<TimeLine>()
                .setType(TimeLine.class)
                .setName(new ResourceLocation("timetravelmod:timelines"))
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

    public static class TimeMachinesCallbacks implements IForgeRegistry.CreateCallback<TimeMachine>, IForgeRegistry.AddCallback<TimeMachine>, IForgeRegistry.BakeCallback {

        private HashMap<BlockState, ResourceLocation> blockStateResourceLocationHashMap;

        @Override
        public void onCreate(IForgeRegistryInternal<TimeMachine> owner, RegistryManager stage) {
            blockStateResourceLocationHashMap = new HashMap<>();
            owner.setSlaveMap(CONTROLLERTOTM, blockStateResourceLocationHashMap.clone());
        }

        @SuppressWarnings("unchecked")
        @Override
        public void onAdd(IForgeRegistryInternal owner, RegistryManager stage, int id, TimeMachine obj, @Nullable TimeMachine oldObj) {
            blockStateResourceLocationHashMap = (HashMap<BlockState, ResourceLocation>)owner.getSlaveMap(CONTROLLERTOTM, HashMap.class);
            if (obj instanceof CreativeTimeMachine) return; // Special rule for the creative Time Machine
            if (!blockStateResourceLocationHashMap.containsValue(obj.getRegistryName())) {
                for(BlockState block:obj.getControllerBlocks()) {
                    if (!blockStateResourceLocationHashMap.containsKey(block)) {
                        blockStateResourceLocationHashMap.put(block, obj.getRegistryName());
                    } else {
                        throw new RuntimeException(obj.getRegistryName()+" tryed to register with controller block "+block.toString()+", but it is already registered to "+ GameRegistry.findRegistry(Block.class).getValue(blockStateResourceLocationHashMap.get(block)).toString());
                    }
                }
            }
        }

        @Override
        public void onBake(IForgeRegistryInternal owner, RegistryManager stage) {

        }
    }

    public static class TimeMachineUpgradesCallbacks implements IForgeRegistry.CreateCallback<TimeMachineUpgrade> {

        private HashMap<TimeMachineUpgrade, AbstractTimeMachineComponentBlock[]> upgradetoblockhm;

        @Override
        public void onCreate(IForgeRegistryInternal<TimeMachineUpgrade> owner, RegistryManager stage) {
            upgradetoblockhm = new HashMap<>();
            owner.setSlaveMap(UPGRADETOBLOCK, upgradetoblockhm);
        }
    }
}
