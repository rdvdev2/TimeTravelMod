package tk.rdvdev2.TimeTravelMod.proxy;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import tk.rdvdev2.TimeTravelMod.ModPacketHandler;
import tk.rdvdev2.TimeTravelMod.ModTimeLines;
import tk.rdvdev2.TimeTravelMod.TimeTravelMod;
import tk.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;
import tk.rdvdev2.TimeTravelMod.common.networking.OpenTmGuiPKT;

import java.util.UUID;

import static tk.rdvdev2.TimeTravelMod.ModConfig.clientSpec;
import static tk.rdvdev2.TimeTravelMod.ModConfig.commonSpec;

public class CommonProxy implements IProxy {

    @Override
    public void displayTMGuiScreen(PlayerEntity player, TimeMachine tm, BlockPos pos, Direction side, UUID... additionalEntities) {
        ModPacketHandler.CHANNEL.sendTo(new OpenTmGuiPKT(tm, pos, side, additionalEntities), ((ServerPlayerEntity)player).connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
    }

    @Override
    public void displayEngineerBookGuiScreen(PlayerEntity player) {
        TimeTravelMod.LOGGER.warn("Server is trying to display the Engineer's Book GUI");
    }

    @Override
    public void handleOpenTMGUI(OpenTmGuiPKT message, NetworkEvent.Context ctx) {
        // Server is not going to handle this
        TimeTravelMod.LOGGER.warn("Server is trying to handle the OpenTmGuiPKT packet. That's weird!");
    }

    @Override
    public void modConstructor(TimeTravelMod instance) {
        // Register FMLCommonSetupEvent
        FMLJavaModLoadingContext.get().getModEventBus().addListener(instance::commonSetup);
        // Register RegisterDimensionsEvent
        MinecraftForge.EVENT_BUS.addListener(ModTimeLines::registerDimension);
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(instance);

        // Register the client config
        ModLoadingContext.get().getActiveContainer().addConfig(new net.minecraftforge.fml.config.ModConfig(net.minecraftforge.fml.config.ModConfig.Type.CLIENT, clientSpec, ModLoadingContext.get().getActiveContainer()));
        // Register the common config
        ModLoadingContext.get().getActiveContainer().addConfig(new net.minecraftforge.fml.config.ModConfig(net.minecraftforge.fml.config.ModConfig.Type.COMMON, commonSpec, ModLoadingContext.get().getActiveContainer()));
    }
}
