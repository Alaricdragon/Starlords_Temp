package starlords.util;

import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;
import starlords.util.ScriptedValues.ScriptedValueController;

import java.util.HashMap;

public class CsvFilerReader {
    //this might seem a little round about. but the objective of this class is to eliminate any items with the same ID between all CSV files of the same location, as well as insure load priority.
    private static final String loadString = "loadPriority";
    private static final String idString = "id";
    @SneakyThrows
    public static HashMap<String, JSONObject> computeFile(JSONArray json){
        HashMap<String,JSONObject> out = new HashMap<>();
        HashMap<String,Double> priority = new HashMap<>();
        for (int a = 0; a < json.length(); a++){
            JSONObject b = json.getJSONObject(a);
            addItemToArray(b,out,priority);
        }
        return out;
    }
    @SneakyThrows
    public static HashMap<String,HashMap<String, JSONObject>> computeFile(JSONArray json,String categoryKey){
        HashMap<String,HashMap<String, JSONObject>> out = new HashMap<>();
        HashMap<String,HashMap<String, Double>> priority = new HashMap<>();
        for (int a = 0; a < json.length(); a++){
            JSONObject b = json.getJSONObject(a);
            String cat = b.getString(categoryKey);
            HashMap<String, JSONObject> outT = out.getOrDefault(cat,new HashMap<>());
            HashMap<String, Double> priT = priority.getOrDefault(cat,new HashMap<>());
            addItemToArray(b,outT,priT);
        }

        return out;
    }
    @SneakyThrows
    private static void addItemToArray(JSONObject b, HashMap<String,JSONObject> out, HashMap<String,Double> priority){
        String id = b.getString(idString);
        String p = b.getString(loadString);
        if (id.isEmpty()) return;//prevent loading of 'false' strings.
        if (id.startsWith("#")) return;
        double finalP;
        if (p.isEmpty()){
            finalP = 0;
        }else{
            finalP = ((new ScriptedValueController(p)).getNextDouble().getValue(null));
        }
        if (out.containsKey(id)){
            if (finalP < priority.get(id)) return;
        }
        out.put(id,b);
        priority.put(id,finalP);
    }
}
