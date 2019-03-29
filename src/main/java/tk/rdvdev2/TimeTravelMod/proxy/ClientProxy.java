package tk.rdvdev2.TimeTravelMod.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.concurrent.ThreadTaskExecutor;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.network.NetworkEvent;
import tk.rdvdev2.TimeTravelMod.ModRegistries;
import tk.rdvdev2.TimeTravelMod.TimeTravelMod;
import tk.rdvdev2.TimeTravelMod.api.timemachine.TimeMachine;
import tk.rdvdev2.TimeTravelMod.client.gui.GuiEngineerBook;
import tk.rdvdev2.TimeTravelMod.client.gui.TimeMachineScreen;
import tk.rdvdev2.TimeTravelMod.common.networking.OpenTmGuiPKT;

public class ClientProxy extends CommonProxy {

    private GuiEngineerBook generatedEngineerBook;

    @Override
    public void displayTMGuiScreen(PlayerEntity player, TimeMachine tm, BlockPos pos, Direction side) {
        LogicalSidedProvider.WORKQUEUE.<ThreadTaskExecutor<?>>get(LogicalSide.CLIENT).deferTask(()->Minecraft.getInstance().displayGuiScreen(new TimeMachineScreen(player, tm, pos, side)));
    }

    @Override
    public void displayEngineerBookGuiScreen(EntityPlayer player) {
        if (generatedEngineerBook == null) generatedEngineerBook = new GuiEngineerBook(ModRegistries.timeMachinesRegistry.getValues());
        Minecraft.getInstance().addScheduledTask(()->Minecraft.getInstance().displayGuiScreen(generatedEngineerBook));
    }

    @Override
    public void handleOpenTMGUI(OpenTmGuiPKT message, NetworkEvent.Context ctx) {
        PlayerEntity player = Minecraft.getInstance().player;
        TimeTravelMod.proxy.displayTMGuiScreen(player, message.tm.hook(player.world, message.pos, message.side), message.pos, message.side);
    }
}
