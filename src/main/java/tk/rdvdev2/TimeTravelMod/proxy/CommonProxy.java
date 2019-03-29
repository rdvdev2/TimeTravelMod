package tk.rdvdev2.TimeTravelMod.proxy;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import tk.rdvdev2.TimeTravelMod.ModPacketHandler;
import tk.rdvdev2.TimeTravelMod.TimeTravelMod;
import tk.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;
import tk.rdvdev2.TimeTravelMod.common.networking.OpenTmGuiPKT;

public class CommonProxy implements IProxy {

    @Override
    public void displayTMGuiScreen(PlayerEntity player, TimeMachine tm, BlockPos pos, Direction side) {
        ModPacketHandler.CHANNEL.sendTo(new OpenTmGuiPKT(tm, pos, side), ((ServerPlayerEntity)player).connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
    }

    @Override
    public void displayEngineerBookGuiScreen(EntityPlayer player) {
        TimeTravelMod.logger.warn("Server is trying to display the Engineer's Book GUI");
    }

    @Override
    public void handleOpenTMGUI(OpenTmGuiPKT message, NetworkEvent.Context ctx) {
        // Server is not going to handle this
        TimeTravelMod.logger.warn("Server is trying to handle the OpenTmGuiPKT packet. That's weird!");
    }
}
