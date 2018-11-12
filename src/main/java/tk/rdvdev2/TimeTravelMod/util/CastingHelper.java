package tk.rdvdev2.TimeTravelMod.util;

import net.minecraft.util.EnumFacing;

public class CastingHelper {

    public static EnumFacing intToEnumFacing(int i) {
        switch (i) {
            case 0:
                return EnumFacing.DOWN;
            case 1:
                return EnumFacing.UP;
            case 2:
                return EnumFacing.NORTH;
            case 3:
                return EnumFacing.SOUTH;
            case 4:
                return EnumFacing.WEST;
            case 5:
                return EnumFacing.EAST;
        }
        return null;
    }
}
