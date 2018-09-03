package com.rdvdev2.TimeTravelMod.common.registry;

import com.rdvdev2.TimeTravelMod.api.dimension.TimeLine;
import com.rdvdev2.TimeTravelMod.api.event.EventRegisterTimeLine;
import net.minecraftforge.common.DimensionManager;

import java.util.Arrays;

import static net.minecraftforge.common.MinecraftForge.EVENT_BUS;

public class RegistryTimeLines {

    private TimeLine[] timeLines = new TimeLine[]{};

    public RegistryTimeLines(){}

    public void start() {
        EVENT_BUS.post(new EventRegisterTimeLine(this));
    }

    public int register(TimeLine tl) {
        DimensionManager.registerDimension(tl.getDimId(), tl.getDimensionType());
        int rid = timeLines.length;
        timeLines = Arrays.copyOf(timeLines, rid+1);
        tl.setModRegistryId(rid);
        timeLines[rid] = tl;
        return rid;
    }

    public TimeLine[] getAvailableTimeLines(int tier) {
        TimeLine[] availableTimeLines = new TimeLine[]{};
        for (TimeLine tl:timeLines) {
            if (tl.getMinTier() <= tier) {
                int i = availableTimeLines.length;
                availableTimeLines = Arrays.copyOf(availableTimeLines, i+1);
                availableTimeLines[i] = tl;
            }
        }
        return availableTimeLines;
    }

    public TimeLine getFromRegistryId(int rid) {
        return timeLines[rid];
    }

    public TimeLine getFromDimId(int dimId) {
        for (TimeLine tl:timeLines) {
            if (tl.getDimId() == dimId) {
                return tl;
            }
        }
        return null;
    }

    public TimeLine[] getTimeLines() {
        return timeLines;
    }
}
