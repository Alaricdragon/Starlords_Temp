package starlords.util.memoryUtils.Stats;

import starlords.util.ScriptedValues.SV_Double_Code;
import starlords.util.ScriptedValues.ScriptedValueController;
import starlords.util.math.StarLord_MutableStat;
import starlords.util.memoryUtils.GenericMemory;

import java.util.HashMap;

public class StatsRandomOrganizer {
    public static final String TYPE_BASE = "MutableStat", TYPE_MULTI = "MutableStat_multi", TYPE_FLAT = "MutableStat_flat";
    //NOTE: type is stored in the GenericMemory.
    //public static final String TYPE_LORD = "LORD",TYPE_PMC = "PMC", TYPE_FACTION = "FACTION";
    private static HashMap<String, StatsRandomOrganizer> masterList;
    //this is randomDataToBeSet. in effect, this is the stat values that will be set to a givin lord / other.
    //it is stored as a string to allow for scripts to run. it will be stored in better values once started.
    //data is: statmodID, statID,
    private HashMap<String,HashMap<String, SV_Double_Code>> randomData_Base = new HashMap<>();
    private HashMap<String,HashMap<String, SV_Double_Code>> randomData_Flat = new HashMap<>();
    private HashMap<String,HashMap<String, SV_Double_Code>> randomData_Multi = new HashMap<>();
    public static void setStatData_Base(String TYPE, String mutibleStatID, String statID, SV_Double_Code value){
        masterList.get(TYPE).randomData_Base.get(mutibleStatID).put(statID,value);
    }
    public static void setStatData_Flat(String TYPE, String mutibleStatID, String statID, SV_Double_Code value){
        masterList.get(TYPE).randomData_Flat.get(mutibleStatID).put(statID,value);
    }
    public static void setStatData_Multi(String TYPE, String mutibleStatID, String statID, SV_Double_Code value){
        masterList.get(TYPE).randomData_Multi.get(mutibleStatID).put(statID,value);
    }
    public static StatsHolder prepStatsHolder(String TYPE, HashMap<String,HashMap<String,HashMap<String,String>>> scriptsTemp, Object linkedObject){
        //note: so the input data scripts: here is how its organized:
        //string one is 'type', or 'type of stat mod'.
        //string two is 'MutibleStatID', or the mutable stat ID.
        //string three is 'statID'. or the ID of a givin stat.
        //final string is the rar json string that is used for the stat mod. usefull for scripted values.
        StatsRandomOrganizer s = masterList.get(TYPE);
        StatsHolder out = new StatsHolder();
        for (String a : s.randomData_Base.keySet()){
            if (!out.data.containsKey(a)) out.data.put(a,new StarLord_MutableStat(1));
            StarLord_MutableStat stat = out.data.get(a);

            HashMap<String,HashMap<String,String>> scripts = scriptsTemp.get(TYPE_BASE);
            for (String b : s.randomData_Base.get(a).keySet()) {
                float data;
                if (scripts.containsKey(a) && scripts.get(a).containsKey(b)) continue;
                data = (float) s.randomData_Base.get(a).get(b).getValue(linkedObject);
                stat.addDynamicIncreaseMod(b,data);
            }
            scripts = scriptsTemp.get(TYPE_FLAT);
            for (String b : s.randomData_Flat.get(a).keySet()) {
                float data;
                if (scripts.containsKey(a) && scripts.get(a).containsKey(b)) continue;
                data = (float) s.randomData_Flat.get(a).get(b).getValue(linkedObject);
                stat.addFlatMod(b,data);
            }
            scripts = scriptsTemp.get(TYPE_MULTI);
            for (String b : s.randomData_Multi.get(a).keySet()) {
                float data;
                if (scripts.containsKey(a) && scripts.get(a).containsKey(b)) continue;
                data = (float) s.randomData_Multi.get(a).get(b).getValue(linkedObject);
                stat.addMultiMod(b,data);
            }
        }
        addScriptsToStats(out,scriptsTemp,linkedObject);
        return out;
    }
    private static void addScriptsToStats(StatsHolder out,HashMap<String,HashMap<String,HashMap<String,String>>> scripts,Object linkedObject){
        //note: so the input data scripts: here is how its organized:
        //string one is 'type', or 'type of stat mod'.
        //string two is 'MutibleStatID', or the mutable stat ID.
        //string three is 'statID'. or the ID of a givin stat.
        //final string is the rar json string that is used for the stat mod. usefull for scripted values.
        HashMap<String,HashMap<String,String>> set = scripts.get(TYPE_BASE);
        for (String a : set.keySet()) for (String b : set.get(a).keySet()){
            if (!out.data.containsKey(a)) out.data.put(a,new StarLord_MutableStat(1));
            out.data.get(a).addDynamicIncreaseMod(b, (float) new ScriptedValueController(set.get(a).get(b)).getNextDouble().getValue(linkedObject));
        }
        set = scripts.get(TYPE_FLAT);
        for (String a : set.keySet()) for (String b : set.get(a).keySet()){
            if (!out.data.containsKey(a)) out.data.put(a,new StarLord_MutableStat(1));
            out.data.get(a).addFlatMod(b, (float) new ScriptedValueController(set.get(a).get(b)).getNextDouble().getValue(linkedObject));
        }
        set = scripts.get(TYPE_MULTI);
        for (String a : set.keySet()) for (String b : set.get(a).keySet()){
            if (!out.data.containsKey(a)) out.data.put(a,new StarLord_MutableStat(1));
            out.data.get(a).addFlatMod(b, (float) new ScriptedValueController(set.get(a).get(b)).getNextDouble().getValue(linkedObject));
        }
    }

    public static void insureCoreIntergerty(){
        //example taken from SubStaticPreperationData.
        if (!masterList.containsKey(GenericMemory.TYPE_FACTION)) new StatsRandomOrganizer(GenericMemory.TYPE_FACTION);
        if (!masterList.containsKey(GenericMemory.TYPE_LORD)) new StatsRandomOrganizer(GenericMemory.TYPE_LORD);
        if (!masterList.containsKey(GenericMemory.TYPE_PMC)) new StatsRandomOrganizer(GenericMemory.TYPE_PMC);
        if (!masterList.containsKey(GenericMemory.TYPE_FLEET)) new StatsRandomOrganizer(GenericMemory.TYPE_FLEET);
        if (!masterList.containsKey(GenericMemory.TYPE_SHIP)) new StatsRandomOrganizer(GenericMemory.TYPE_SHIP);
    }
    public static void repair(String TYPE, StatsHolder out , Object linkedObject){
        StatsRandomOrganizer s = masterList.get(TYPE);
        for (String a : s.randomData_Base.keySet()){
            if (!out.data.containsKey(a)) out.data.put(a,new StarLord_MutableStat(1));
            else continue;
            StarLord_MutableStat stat = out.data.get(a);

            for (String b : s.randomData_Base.get(a).keySet()) {
                float data;
                data = (float) s.randomData_Base.get(a).get(b).getValue(linkedObject);
                stat.addDynamicIncreaseMod(b,data);
            }
            for (String b : s.randomData_Flat.get(a).keySet()) {
                float data;
                data = (float) s.randomData_Flat.get(a).get(b).getValue(linkedObject);
                stat.addFlatMod(b,data);
            }
            for (String b : s.randomData_Multi.get(a).keySet()) {
                float data;
                data = (float) s.randomData_Multi.get(a).get(b).getValue(linkedObject);
                stat.addMultiMod(b,data);
            }
        }
    }
    public StatsRandomOrganizer(String id){

        masterList.put(id,this);
    }
}
