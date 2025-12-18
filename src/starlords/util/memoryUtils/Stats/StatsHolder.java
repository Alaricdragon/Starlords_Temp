package starlords.util.memoryUtils.Stats;

import starlords.util.math.StarLord_MutableStat;

import java.util.ArrayList;
import java.util.HashMap;

public class StatsHolder {
    protected HashMap<String, StarLord_MutableStat> data;
    protected HashMap<String,StatsHolder> linkedStats;
    public StatsHolder(String TYPE, Object linkedObject,HashMap<String,String> overWritingData){
        //this is only the default types.
        StatsRandomOrganizer.getRandomOrganizer(TYPE).setData(this,linkedObject,overWritingData);
    }
    public StarLord_MutableStat getStat(String id){
        return data.get(id);
    }
    public double getValue(String id){
        if (!data.containsKey(id)) return 0;
        if (shouldChangeStats(id,data.get(id).getLastChanged())){
            data.get(id).recalculateValue(getAllMutibleStats().toArray(new StarLord_MutableStat[]{}));
        }
        return data.get(id).getCalculatedValue();
    }
    protected ArrayList<StarLord_MutableStat> getAllMutibleStats(){
        ArrayList<StarLord_MutableStat> stats = new ArrayList<>();
        for (StatsHolder a : linkedStats.values()){
            stats.addAll(a.getAllMutibleStats());
        }
        return stats;
    }
    protected boolean shouldChangeStats(String id, double time){
        StarLord_MutableStat a = data.get(id);
        if (a.getLastChanged() > time) return true;
        for (StatsHolder b : linkedStats.values()){
            if (b.shouldChangeStats(id,time)) return true;
        }
        return false;
    }
}
