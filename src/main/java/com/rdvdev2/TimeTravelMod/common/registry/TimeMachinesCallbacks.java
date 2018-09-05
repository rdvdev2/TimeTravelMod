package com.rdvdev2.TimeTravelMod.common.registry;

import com.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;
import com.rdvdev2.TimeTravelMod.common.timemachine.TimeMachineCreative;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryInternal;
import net.minecraftforge.registries.RegistryManager;

import javax.annotation.Nullable;
import java.util.HashMap;

public class TimeMachinesCallbacks implements IForgeRegistry.CreateCallback<TimeMachine>, IForgeRegistry.AddCallback<TimeMachine> {

    private HashMap<IBlockState, ResourceLocation> blockStateResourceLocationHashMap;
    public static ResourceLocation BLOCKTOTM = new ResourceLocation("timetravelmod:blocktotm");

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
