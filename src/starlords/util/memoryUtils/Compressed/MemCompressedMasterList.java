package starlords.util.memoryUtils.Compressed;

import com.fs.starfarer.api.Global;
import lombok.Getter;
import starlords.util.memoryUtils.Compressed.types.MemCompressed_Lord_DoubleScript;
import starlords.util.memoryUtils.Compressed.types.MemCompressed_Lord;

import java.util.HashMap;

public class MemCompressedMasterList {
    /*so, in regards to lords compressed memory: its organization is:
    * holder<holder<?>>
    * were the first holder refrences the 'lists of data'
    *   example: upgradeCost will be one list. upgradeWeight will be another
    *       -note: both 'upgradeCost' and 'upgradeWeight' hold lists corresponding to different upgrades, and each of said lists holds all the different double values for each possible weight.
    *
    * so I require 2 linked holders:
    * organized like so is: organizer<organize<double>>
    * (1: upgradeID, 2: upgradeVaruble: 3 value)
    *
    * note: this goes inside of the first linked holder. so it really looks like:
    * <organizer<organizer<?>>  (were in this case, the ? is organizer<double>)
    *
    *
    * notes on getting this system installed.
    * 1: I gotta remember that the system runs on a central hashmap. and inside of the central hashmap, I will hold my lords compressed memory organizer.
    *
    * */


    //constants for compressed memory keys (in primary hashmap)
    public static final String LORD_KEY = "LORD_";
    public static final String FACTION_KEY = "FACTION_";
    public static final String PMC_KEY = "PMC_";
    //constants for disorganized types:
    public static final String DOUBLE_KEY = "DOUBLE_";
    //default key for random value:
    public static final String NO_CUSTOM_KEY = "NULLKEY_";
    //constants for upgrade keys:
    public static final String TYPE_UPGRADE_KEY = "UPGRADE_";
    public static final String UPGRADE_WEIGHT_KEY = "_WEIGHT_";
    public static final String UPGRADE_COST_KEY = "_COST_";
    public static final String UPGRADE_AIWEIGHT_KEY = "_AIWEIGHT_";

    @Getter
    private static HashMap<String,MemCompressedOrganizer<?,?>> memory = new HashMap<>();


    private static final String memKey = "$STARLORDS_MEMORY_COMPRESSED_MEMORY_MASTER_LIST";
    public static void save(){
        for (String a : memory.keySet()){
            memory.get(a).save();
        }

        String key = memKey;
        HashMap<String,MemCompressedOrganizer<?,?>> data = memory;
        Global.getSector().getMemory().set(key,data);
    }
    public static void load(){
        String key = memKey;
        HashMap<String,MemCompressedOrganizer<?,?>> temp;
        if (Global.getSector().getMemory().contains(key)){
            temp = (HashMap<String,MemCompressedOrganizer<?,?>>) Global.getSector().getMemory().get(key);
        }else{
            temp = new HashMap<>();
        }
        memory = temp;

        insureCoreStructurePresent();
    }
    public static void loadFinal(){
        for (String a : memory.keySet()){
            memory.get(a).load();
        }
        checkAllMemoryOrganizers();

    }

    private static void checkAllMemoryOrganizers(){
    }

    private static void insureCoreStructurePresent(){
        //this is were all the basic classes are intialized. basicly, it is the preperation of structure, provided any bit of it is not present.
        if (!MemCompressedMasterList.memory.containsKey(LORD_KEY)){
            MemCompressedMasterList.getMemory().put(LORD_KEY,new MemCompressed_Lord());
        }
        MemCompressed_Lord lordmemory = (MemCompressed_Lord) MemCompressedMasterList.getMemory().get(LORD_KEY);
        if (!lordmemory.hasItem(DOUBLE_KEY)){
            lordmemory.setItem(DOUBLE_KEY,new MemCompressed_Lord_DoubleScript());
        }

    }
}
