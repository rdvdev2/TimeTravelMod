package com.rdvdev2.TimeTravelMod.proxy;

import com.rdvdev2.TimeTravelMod.TimeTravelMod;
import com.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;
import com.rdvdev2.TimeTravelMod.common.networking.OpenTmGuiPKT;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;

public interface IProxy {
    default void displayTMGuiScreen(PlayerEntity player, TimeMachine tm, BlockPos pos, Direction side, UUID... additionalEntities) { } // Show the TM GUI to the player
    default void displayEngineerBookGuiScreen(PlayerEntity player) { } // Show the Engineer's Book GUI to the player
    default void handleOpenTMGUI(OpenTmGuiPKT message, NetworkEvent.Context ctx) { } // Handle the OpenTmGuiPKT network packet (Client Only)
    default void modConstructor(TimeTravelMod instance) { } // Tasks done in the constructor
    default void clientSetup(FMLClientSetupEvent event) { } // Client-side setup
}
