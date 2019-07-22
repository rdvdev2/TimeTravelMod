package tk.rdvdev2.TimeTravelMod.proxy;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import tk.rdvdev2.TimeTravelMod.TimeTravelMod;
import tk.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;
import tk.rdvdev2.TimeTravelMod.common.networking.OpenTmGuiPKT;

import java.util.UUID;

public interface IProxy {
    void displayTMGuiScreen(PlayerEntity player, TimeMachine tm, BlockPos pos, Direction side, UUID... additionalEntities); // Show the TM GUI to the player
    void displayEngineerBookGuiScreen(PlayerEntity player); // Show the Engineer's Book GUI to the player
    void handleOpenTMGUI(OpenTmGuiPKT message, NetworkEvent.Context ctx); // Handle the OpenTmGuiPKT network packet (Client Only)
    void modConstructor(TimeTravelMod instance); // Tasks done in the constructor
}
