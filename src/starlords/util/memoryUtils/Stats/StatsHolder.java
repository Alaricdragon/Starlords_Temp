package starlords.util.memoryUtils.Stats;

import lombok.Getter;
import starlords.util.Utils;
import starlords.util.math.StarLord_MutableStat;
import starlords.util.memoryUtils.Stats.types.*;
import starlords.util.memoryUtils.DataHolder;

import java.util.HashMap;

public class StatsHolder {
    protected HashMap<String, StarLord_MutableStat> data;
    public StatsHolder(String dataType, Object linkedObject,HashMap<String,String> overWritingData){
        StatsRandomOrganizer.getOrganizers().get(dataType);
        //seed = Utils.rand.nextInt(StatsRandomOrganizer.getMaxSeed());
        StatsRandomOrganizer.getOrganizers().get(dataType).setData(this,linkedObject,overWritingData);
    }
}
