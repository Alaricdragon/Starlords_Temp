package starlords.util.memoryUtils.Stats;

import starlords.util.math.StarLord_MutableStat;
import java.util.HashMap;

public class StatsRandomOrganizer {
    public static final String TYPE_LORD = "LORD",TYPE_PMC = "PMC", TYPE_FACTION = "FACTION";
    private static HashMap<String, StatsRandomOrganizer> organizers;
    //this is randomDataToBeSet. in effect, this is the stat values that will be set to a givin lord / other.
    //it is stored as a string to allow for scripts to run. it will be stored in better values once started.
    private HashMap<String,String> randomDataToBeSet;
    public StatsRandomOrganizer(String id, String path){
        //load data here (from json path).
        organizers.put(id,this);
    }
    public static StatsRandomOrganizer getRandomOrganizer(String TYPE){
        return organizers.get(TYPE);
    }
    public void setData(StatsHolder holder, Object linkedObject,HashMap<String,String> scripts){
        for (String a : randomDataToBeSet.keySet()){
            String value;
            if (scripts.containsKey(a)) value = scripts.get(a);
            else value = randomDataToBeSet.get(a);
            setSingleItem(holder.data,linkedObject,value);
        }
    }
    private void setSingleItem(HashMap<String, StarLord_MutableStat> data, Object linkedObject, String value){
        String idOfItem="";
        boolean isScript; //this is for if the item is a scrip of a mutible stat. if this is the case, the stat can change when it is called. This is rarely usefull, but should be implmented anyways.
        if (!data.containsKey(idOfItem)){} //create new item here
        StarLord_MutableStat stat = data.get(idOfItem);
        //from here, all I need to do is get the relevant data. it should be simple hopefully.
    }
}
