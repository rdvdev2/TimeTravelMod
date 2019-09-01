package tk.rdvdev2.TimeTravelMod.common.entity.origintl;

import tk.rdvdev2.TimeTravelMod.api.dimension.TimeLine;

public interface IOriginTL {

    TimeLine getOriginTimeLine();

    @Deprecated
    void setOriginTimeLine(TimeLine origin); // Only for data restoring from NBT
}
