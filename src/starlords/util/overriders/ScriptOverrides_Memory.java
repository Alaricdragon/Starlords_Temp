package starlords.util.overriders;

import lombok.Getter;
import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.util.memoryUtils.Stats.StatsRandomOrganizer;

import java.util.HashMap;
import java.util.Iterator;

public class ScriptOverrides_Memory {
    /*so what is this?
        this is my solution to a simple issue: some objects (such as lords) sometimes need certan bits of data to be overridden.
        but, the data should only exist until it's set in memory. This is my solution.
    */
    //this is for replacement scripts of inter functions. Will likely change this latter,
    //private HashMap<String,String> Scripts = new HashMap<>();

    //note: do to its nature, sub static items -cannot have the same id acrose different data types-. this means that this only requires one string.
    @Getter
    private HashMap<String,String> subStaticItems = new HashMap<>();
    @Getter
    private HashMap<String,HashMap<String,HashMap<String,String>>> statModOverrides = new HashMap<>();
    @SneakyThrows
    public ScriptOverrides_Memory(JSONObject json){
        getStatModOverrides(json);
        getSubStaticItems(json);
    }
    @SneakyThrows
    private void getStatModOverrides(JSONObject json){
        //note: so the input data scripts: here is how its organized:
        //string one is 'type', or 'type of stat mod'.
        //string two is 'MutibleStatID', or the mutable stat ID.
        //string three is 'statID'. or the ID of a givin stat.
        //final string is the rar json string that is used for the stat mod. usefull for scripted values.
        statModOverrides.put(StatsRandomOrganizer.TYPE_BASE,new HashMap<>());
        statModOverrides.put(StatsRandomOrganizer.TYPE_FLAT,new HashMap<>());
        statModOverrides.put(StatsRandomOrganizer.TYPE_MULTI,new HashMap<>());
        if (json == null || !json.has("mutableStats")) return;
        json = json.getJSONObject("mutableStats");
        for (Iterator it = json.keys(); it.hasNext(); ) {
            String mutibleStatID = it.next().toString();
            for (Iterator iter = json.getJSONObject(mutibleStatID).keys(); iter.hasNext(); ) {
                String b = iter.next().toString();
                String[] data = b.split(":");
                String type = data[0];
                String id = data[1];
                String value = json.getJSONObject(mutibleStatID).getString(b);
                HashMap<String,HashMap<String,String>> thing = new HashMap<>();
                thing = switch (type) {
                    case "base" -> statModOverrides.get(StatsRandomOrganizer.TYPE_BASE);
                    case "flat" -> statModOverrides.get(StatsRandomOrganizer.TYPE_FLAT);
                    case "multi" -> statModOverrides.get(StatsRandomOrganizer.TYPE_MULTI);
                    default -> thing;
                };
                if (!thing.containsKey(mutibleStatID)) thing.put(mutibleStatID,new HashMap<>());
                thing.get(mutibleStatID).put(id,value);
            }
        }
    }
    @SneakyThrows
    private void getSubStaticItems(JSONObject json){
        if (json == null || !json.has("randoms")) return;
        json = json.getJSONObject("randoms");
        for (Iterator it = json.keys(); it.hasNext(); ) {
            String a = it.next().toString();
            subStaticItems.put(a,json.getString(a));
        }
    }
    public void resetUnwantedDataAfterMemonyHasEaten(){
        subStaticItems = null;
        statModOverrides = null;
    }
}
