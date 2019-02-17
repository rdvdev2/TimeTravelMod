package tk.rdvdev2.TimeTravelMod.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.MinableConfig;
import net.minecraft.world.gen.placement.CountRangeConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;
import tk.rdvdev2.TimeTravelMod.*;
import tk.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;
import tk.rdvdev2.TimeTravelMod.common.event.EventConfigureTimeMachineBlocks;
import tk.rdvdev2.TimeTravelMod.common.networking.OpenTmGuiPKT;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

public class CommonProxy implements IProxy {

    @Override
    public void commonSetup(FMLCommonSetupEvent event) {
        TimeTravelMod.logger.info("Time Travel Mod is in common setup state.");
        ForgeRegistries.BIOMES.forEach((biome ->
                biome.addFeature(GenerationStage.Decoration.UNDERGROUND_ORES, Biome.createCompositeFeature(
                        Feature.MINABLE,
                        new MinableConfig(MinableConfig.IS_ROCK, ModBlocks.timeCrystalOre.getDefaultState(), 4),
                        Biome.COUNT_RANGE,
                        new CountRangeConfig(1, 0, 0, 16)
                ))));
        ModStructures.init();
        ModRecipes.init();
        EVENT_BUS.post(new EventConfigureTimeMachineBlocks());
    }

    @Override
    public void displayTMGuiScreen(EntityPlayer player, TimeMachine tm, BlockPos pos, EnumFacing side) {
        ModPacketHandler.CHANNEL.sendTo(new OpenTmGuiPKT(tm, pos, side), ((EntityPlayerMP)player).connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
    }

    @Override
    public void handleOpenTMGUI(OpenTmGuiPKT message, NetworkEvent.Context ctx) {
        // Server is not going to handle this
        TimeTravelMod.logger.warn("Server is trying to handle the OpenTmGuiPKT packet. That's weird!");
    }
}
