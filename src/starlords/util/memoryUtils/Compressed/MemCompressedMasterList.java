package starlords.util.memoryUtils.Compressed;

import com.fs.starfarer.api.Global;
import lombok.Getter;
import starlords.util.memoryUtils.Compressed.types.*;

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
    public static final String KEY_LORD = "LORD_";
    public static final String KEY_FACTION = "FACTION_";
    public static final String KEY_PMC = "PMC_";
    public static final String KEY_FLEET = "FLEET_";
    public static final String KEY_SHIP = "SHIP_";
    //constants for disorganized types:
    public static final String MTYPE_KEY_DOUBLE = "DOUBLE_";
    public static final String MTYPE_KEY_STRING = "STRING_";
    public static final String MTYPE_KEY_BOOLEAN = "BOOLEAN_";
    //default key for random value:
    public static final String MTYPE_KEY_NO_CUSTOM = "NULLKEY_";

    public static final String[] standerdDataAsArray = {
            MTYPE_KEY_DOUBLE,
            MTYPE_KEY_STRING,
            MTYPE_KEY_BOOLEAN,
            MTYPE_KEY_NO_CUSTOM
    };
    //constants for upgrade keys:
    public static final String BUILDER_UPGRADE_KEY = "UPGRADE_";
    public static final String UPGRADE_WEIGHT_KEY = "_WEIGHT_";
    public static final String UPGRADE_COST_KEY = "_COST_";
    public static final String UPGRADE_AIWEIGHT_KEY = "_AIWEIGHT_";

    //constants for fleet keys:
    public static final String BUILDER_FLEETCOMP_KEY = "FLEETCOMP_";
    public static final String FLEET_COMBAT = "COMBAT_";
    public static final String FLEET_FUEL = "FUEL_";
    public static final String FLEET_CARGO = "CARGO_";
    public static final String FLEET_PERSONAL = "PERSONAL_";
    public static final String FLEET_TUG = "TUG_";


    @Getter
    private static HashMap<String,MemCompressedOrganizer<?,?>> memory = new HashMap<>();


    private static final String memKey = "$STARLORDS_MEMORY_COMPRESSED_MEMORY_MASTER_LIST";

    public static MemCompressedOrganizer<MemCompressedHolder<?>,MemCompressedOrganizer<?,?>> getBaseOrganizer(String KEY_TYPE){
        return (MemCompressedOrganizer<MemCompressedHolder<?>, MemCompressedOrganizer<?, ?>>) memory.get(KEY_TYPE);
    }
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
        insureStructureLordPresent();
        insureStructurePMCPresent();


    }
    private static void insureStructurePresent(String key){
        MemCompressedOrganizer<?,MemCompressedOrganizer<?,?>> memory = (MemCompressedOrganizer<?, MemCompressedOrganizer<?, ?>>) MemCompressedMasterList.getMemory().get(key);
        if (!memory.hasItem(MTYPE_KEY_DOUBLE)){
            memory.setItem(MTYPE_KEY_DOUBLE,new MemCompressed_DoubleScript());
        }
        if (!memory.hasItem(MTYPE_KEY_STRING)){
            memory.setItem(MTYPE_KEY_STRING,new MemCompressed_StringScript());
        }
        if (!memory.hasItem(MTYPE_KEY_BOOLEAN)){
            memory.setItem(MTYPE_KEY_BOOLEAN,new MemCompressed_BooleanScript());
        }
        if (!memory.hasItem(MTYPE_KEY_NO_CUSTOM)){
            memory.setItem(MTYPE_KEY_NO_CUSTOM,new MemCompressed_OtherScript());
        }
    }
    private static void insureStructureLordPresent(){
        if (!MemCompressedMasterList.memory.containsKey(KEY_LORD)){
            MemCompressedMasterList.getMemory().put(KEY_LORD,new MemCompressed_Lord());
        }
        insureStructurePresent(KEY_LORD);
    }
    private static void insureStructureFleetComp(){
        //todo: create a MemCompressed_ for fleet comp
        //      note: it is NOT POSSABLE to make this compleatly generatic. the reasons is because of the 'repair' data. that data needs to be stored somewere and
        //            ... ok so it is possible to make generic. just fuck me. do I really want to it just moves the issue somewhere else for fuck sakes! Yes, I could do it. but it is easyer this way, I think.
        if (true) return;
        if (!MemCompressedMasterList.memory.containsKey(KEY_FLEET)){
            MemCompressedMasterList.getMemory().put(KEY_FLEET,new MemCompressed_Lord());
        }
        insureStructurePresent(KEY_FLEET);
    }
    private static void insureStructureShipComp(){
        //todo: create a MemCompressed_ for ship comp
        if (true) return;
        if (!MemCompressedMasterList.memory.containsKey(KEY_SHIP)){
            MemCompressedMasterList.getMemory().put(KEY_SHIP,new MemCompressed_Lord());
        }
        insureStructurePresent(KEY_SHIP);
    }
    private static void insureStructurePMCPresent(){
        //todo: this cannot use 'MemCompressed_Lord'
        if (true) return;
        if (!MemCompressedMasterList.memory.containsKey(KEY_PMC)){
            MemCompressedMasterList.getMemory().put(KEY_PMC,new MemCompressed_Lord());
        }
        insureStructurePresent(KEY_PMC);
    }
}
