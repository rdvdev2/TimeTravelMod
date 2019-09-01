package tk.rdvdev2.TimeTravelMod.common.entity.origintl;

import tk.rdvdev2.TimeTravelMod.api.dimension.TimeLine;

public class OriginHandler implements IOriginTL {

    private TimeLine origin;

    @Override
    public TimeLine getOriginTimeLine() {
        return origin;
    }

    @Override
    public void setOriginTimeLine(TimeLine origin) {
        this.origin = origin;
    }
}
