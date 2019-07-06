package tk.rdvdev2.TimeTravelMod.common.networking;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import tk.rdvdev2.TimeTravelMod.ModItems;
import tk.rdvdev2.TimeTravelMod.TimeTravelMod;

import java.util.function.Supplier;

public class SyncBookData {
    public SyncBookData() {}

    private CompoundNBT data;

    public SyncBookData(CompoundNBT data) {
        this.data = data;
    }

    public static void encode(SyncBookData pkt, PacketBuffer buf) {
        buf.writeInt(pkt.data.getInt("page"));
        buf.writeInt(pkt.data.getInt("y"));
    }

    public static SyncBookData decode(PacketBuffer buf) {
        CompoundNBT data = new CompoundNBT();
        data.putInt("page", buf.readInt());
        data.putInt("y", buf.readInt());
        return new SyncBookData(data);
    }

    public static class Handler {
        public static void handle(SyncBookData message, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(()->{
                ItemStack item = ctx.get().getSender().inventory.getCurrentItem();
                int slot = ctx.get().getSender().inventory.currentItem;
                if (item.isItemEqual(new ItemStack(ModItems.engineerBook))) {
                    CompoundNBT tag = item.getTag();
                    tag.put("data", message.data);
                    item.setTag(tag);
                    ctx.get().getSender().inventory.setInventorySlotContents(slot, item);
                } else {
                    TimeTravelMod.logger.warn("Server was unable to sync Engineer Book data!");
                }
            });
        }
    }
}
