package tk.rdvdev2.TimeTravelMod.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import tk.rdvdev2.TimeTravelMod.ModRegistries;
import tk.rdvdev2.TimeTravelMod.TimeTravelMod;
import tk.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;
import tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.IncompatibleTimeMachineHooksException;
import tk.rdvdev2.TimeTravelMod.client.gui.EngineerBookScreen;
import tk.rdvdev2.TimeTravelMod.api.timemachine.upgrade.IncompatibleTimeMachineHooksException;
import tk.rdvdev2.TimeTravelMod.client.gui.TimeMachineScreen;
import tk.rdvdev2.TimeTravelMod.common.networking.OpenTmGuiPKT;

public class ClientProxy extends CommonProxy {

    private EngineerBookScreen generatedEngineerBook;

    @Override
    public void displayTMGuiScreen(PlayerEntity player, TimeMachine tm, BlockPos pos, Direction side) {
        Minecraft.getInstance().deferTask(()->Minecraft.getInstance().displayGuiScreen(new TimeMachineScreen(player, tm, pos, side)));
    }

    @Override
    public void displayEngineerBookGuiScreen(PlayerEntity player) {
        if (generatedEngineerBook == null) generatedEngineerBook = new EngineerBookScreen(ModRegistries.timeMachinesRegistry.getValues());
        Minecraft.getInstance().deferTask(()->Minecraft.getInstance().displayGuiScreen(generatedEngineerBook));
    }

    @Override
    public void handleOpenTMGUI(OpenTmGuiPKT message, NetworkEvent.Context ctx) {
        PlayerEntity player = Minecraft.getInstance().player;
        try {
            TimeTravelMod.proxy.displayTMGuiScreen(player, message.tm.hook(player.world, message.pos, message.side), message.pos, message.side);
        } catch (IncompatibleTimeMachineHooksException e) {
            throw new RuntimeException("Time Machine GUI opened with invalid upgrade configuration");
        }
    }
}
