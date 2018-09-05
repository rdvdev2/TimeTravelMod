package com.rdvdev2.TimeTravelMod.common.registry;

import com.rdvdev2.TimeTravelMod.ModRegistries;
import com.rdvdev2.TimeTravelMod.api.dimension.TimeLine;

import java.util.Arrays;
import java.util.Iterator;

public class RegistryTimeLines {

    public RegistryTimeLines(){}

    public TimeLine[] getAvailableTimeLines(int tier) {
        TimeLine[] availableTimeLines = new TimeLine[]{};
        Iterator<TimeLine> tls = ModRegistries.timeLinesRegistry.iterator();
        while (tls.hasNext()) {
            TimeLine tl = tls.next();
            if (tl.getMinTier() <= tier) {
                int i = availableTimeLines.length;
                availableTimeLines = Arrays.copyOf(availableTimeLines, i+1);
                availableTimeLines[i] = tl;
            }
        }
        return availableTimeLines;
    }

    public Iterator<TimeLine> getTimeLines() {
        return ModRegistries.timeLinesRegistry.iterator();
    }
}
