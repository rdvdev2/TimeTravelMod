package com.rdvdev2.TimeTravelMod.util;

import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;

public class ByteBufHelper {

    public static void writeBlockPos(ByteBuf buf, BlockPos pos) {
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
    }
    public static BlockPos readBlockPos(ByteBuf buf) {
        return new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    }

    public static void writeString(ByteBuf buf, String s) {
        buf.writeInt(s.length());
        buf.writeCharSequence(s.toString(), Charsets.UTF_8);
    }

    public static String readString(ByteBuf buf) {
        int size = buf.readInt();
        return buf.readCharSequence(size, Charsets.UTF_8).toString();
    }
}
