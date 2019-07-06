package tk.rdvdev2.TimeTravelMod.common.networking;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import tk.rdvdev2.TimeTravelMod.ModItems;
import tk.rdvdev2.TimeTravelMod.TimeTravelMod;

import java.util.function.Supplier;

public class SyncBookData {
    public SyncBookData() {}

    private NBTTagCompound data;

    public SyncBookData(NBTTagCompound data) {
        this.data = data;
    }

    public static void encode(SyncBookData pkt, PacketBuffer buf) {
        buf.writeInt(pkt.data.getInt("page"));
        buf.writeInt(pkt.data.getInt("y"));
    }

    public static SyncBookData decode(PacketBuffer buf) {
        NBTTagCompound data = new NBTTagCompound();
        data.setInt("page", buf.readInt());
        data.setInt("y", buf.readInt());
        return new SyncBookData(data);
    }

    public static class Handler {
        public static void handle(SyncBookData message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(()->{
                ItemStack item = ctx.get().getSender().inventory.getCurrentItem();
                int slot = ctx.get().getSender().inventory.currentItem;
                if (item.isItemEqual(new ItemStack(ModItems.engineerBook))) {
                    NBTTagCompound tag = item.getTag();
                    tag.setTag("data", message.data);
                    item.setTag(tag);
                    ctx.get().getSender().inventory.setInventorySlotContents(slot, item);
                } else {
                    TimeTravelMod.logger.warn("Server was unable to sync Engineer Book data!");
                }
            });
        }
    }
}
