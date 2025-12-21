package starlords.util.memoryUtils.Stats;

import starlords.util.ScriptedValues.SV_Double_Code;
import starlords.util.math.StarLord_MutableStat;
import starlords.util.memoryUtils.GenericMemory;
import starlords.util.memoryUtils.genaricLists.SubStaticPreparationData;

import java.util.HashMap;
import java.util.HashSet;

public class StatsRandomOrganizer {
    public static final String TYPE_BASE = "MutableStat", TYPE_MULTI = "MutableStat_multi", TYPE_FLAT = "MutableStat_flat";
    //NOTE: type is stored in the GenericMemory.
    //public static final String TYPE_LORD = "LORD",TYPE_PMC = "PMC", TYPE_FACTION = "FACTION";
    private static HashMap<String, StatsRandomOrganizer> masterList;
    //this is randomDataToBeSet. in effect, this is the stat values that will be set to a givin lord / other.
    //it is stored as a string to allow for scripts to run. it will be stored in better values once started.
    //data is: statmodID, statID,
    private HashMap<String,HashMap<String, SV_Double_Code>> randomData_Base;
    private HashMap<String,HashMap<String, SV_Double_Code>> randomData_Flat;
    private HashMap<String,HashMap<String, SV_Double_Code>> randomData_Multi;

    /*todo:
        1) I already have a stored list of 'organizers'. so that fine.
        2) create a set of static functions. they will do the following:
            1: set a default data.
            2: fix integrity with a given stat mod.
            3: set the random data of a given stat holder.

        note: this intier function sucks. I need to refactor. By compleat deleation of all data. sorry.

    */
    public static void setStatData_Base(String TYPE, String mutibleStatID, String statID, SV_Double_Code value){
        masterList.get(TYPE).randomData_Base.get(mutibleStatID).put(statID,value);
    }
    public static void setStatData_Flat(String TYPE, String mutibleStatID, String statID, SV_Double_Code value){
        masterList.get(TYPE).randomData_Flat.get(mutibleStatID).put(statID,value);
    }
    public static void setStatData_Multi(String TYPE, String mutibleStatID, String statID, SV_Double_Code value){
        masterList.get(TYPE).randomData_Multi.get(mutibleStatID).put(statID,value);
    }
    public static StatsHolder prepStatsHolder(String TYPE, HashMap<String,String> scripts, Object linkedObject){
        StatsRandomOrganizer s = masterList.get(TYPE);
        StatsHolder out = new StatsHolder();

        HashSet<String> prepedStats = new HashSet<>();
        for (String a : s.randomData_Base.keySet()){
            if (!out.data.containsKey(a)) out.data.put(a,new StarLord_MutableStat(1));
            //todo: I was here. I am just trying to set the initalization data right now. it is very not set.
        }

        return out;
    }

    public static void insureIntergerty(){
        //example taken from SubStaticPreperationData.
        if (!masterList.containsKey(GenericMemory.TYPE_FACTION)) new StatsRandomOrganizer(GenericMemory.TYPE_FACTION);
        if (!masterList.containsKey(GenericMemory.TYPE_LORD)) new StatsRandomOrganizer(GenericMemory.TYPE_LORD);
        if (!masterList.containsKey(GenericMemory.TYPE_PMC)) new StatsRandomOrganizer(GenericMemory.TYPE_PMC);
    }
    public StatsRandomOrganizer(String id){
        masterList.put(id,this);
    }
}
