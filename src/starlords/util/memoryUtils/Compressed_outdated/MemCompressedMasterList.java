package starlords.util.memoryUtils.Compressed_outdated;

import com.fs.starfarer.api.Global;
import lombok.Getter;
import starlords.util.memoryUtils.Compressed_outdated.dataTypes.*;
import starlords.util.memoryUtils.Compressed_outdated.types.MemCompressed_FleetComp;
import starlords.util.memoryUtils.Compressed_outdated.types.MemCompressed_Lord;
import starlords.util.memoryUtils.Compressed_outdated.types.MemCompressed_ShipComp;

import java.util.HashMap;

@Deprecated
public class MemCompressedMasterList {
    /*
    todo:
        so...
        this is fucking worthless. I am going to be forced to use stats anyways. its not usefull having a compressed memory -in this form-.
        I need to get the memory down. the best plan:
        1) have each starlord store a 'seed'. the 'seed' can and will be used to recreate all stats a starlord has.
           -how? here is how it works:
           Math.random can be givin a seed. give it one.
           from there, go through the given stat data -in order-. (as desided by some yet to be determined json).
           I can therefor recreate... recreate... um...
           wait.
           I would need to:
           a: need to recreate all data on a starlord each time I wish to view anything
           or
           b: save the data in some type of temp storge anyways?????
           ... is this useful? all it does is reduce the memory usage in the memory object. unuseful.
           ... although it could reduce load times.
           -!!!!) THIS WOULD WORK IF:
            there was a way to get a random value consistently for little overhead. for example: reading a seed, and being able to get different values from any seed...
            so, here is the idea:
            each 'stat' has an order it is loaded in.
            get true seed as order * seed. this would give each stat its own random data seed and avoid duplicit values (aka first random value always being false).
            so... that works. we did it boys.
            we can now get the full random data in a givin starlord.
            IGNORING SCRIP OVERRIDES (it will be fine, just keep the same values.)
            this fucking works.
            issue: compressed data is now completely worthless. I need a new system.

       2) instead of storing data as 'floating' (in easly acsessable random memory) I store the data in memory.
          this could reduce ram useage.
          but.. is it worth it?
          need to calculate cost of calling memory and storing memory. likely high.


    */
    /*
    YOU: i SEE YOU! look, this is a reminder:
    THIS IS FOR HOLDING CONSISTANCY OF DATA OVER TIME
    THIS IS ALSO FOR SMALL AMOUNTS OF DATA COMPRESSION.
    each starlord can have upwards of 1k values in them.
    because of the -disorganized- nature of starlords (things can change, people can add new stats to all lords, so on so forth) I effectivly need to use hashmaps for this.
    this therefor cuts down the stored data by 50% - 1.
    it does so by replacing all hashmaps with a 'greater' hashmap that stores links to id(int) of values.
    ...
    ...
    ...
    wait....
    does this... make sense?
    ...
    ok so...
    no...
    no no no it does not...
    um, OK so right now:
    I am holding 2 datas:
    1: hashmap<String, Integer> (single per item)
    2: ArrayList<Object> (many)

    storge space used = size3 + (size * lords)+lords.

    if I just had hashmaps, for everything it would take:

    2size * lords

    3a + (a*b) + b
        -
    2a * b


    if a = 100, and b = 100
    this madness = 300 + 10000 + 100 = 11100
    hashmaps     = 200 * 100 =         20000
    ... ok that is a lot of diffrence. nearly 50%.
    at 1000:
    this madness = 3000 + 1000000 + 1000 = 1004000
    hashmpas     = 2000 * 1000           = 2000000 = nearly 50%.
    at 1000a - 100b: (big data, low lords)
    this madness = 3000 + 100000 + 100 = 103100
    hashmapss    = 2000 * 100          = 200000
    at 100a - 1000b small data, big lords
    this masness = 300 + 100000 + 1000 = 101300
    hashmaps     = 200 * 1000          = 200000
    at 10000
    this madness = 30k + 100000k + 10k = 100040k
    hashmaps     = 20k * 10k =           200000k
    ...
    FUCK YOU
    THIS IS NOT FUCKING WORTH IT.
    RECALCULATION REQUIRED.
    WHY? BECAUSE DOUBLES ARE BIG IN MEMORY.
    AND STRINGS ARE ALSO BIG, BUT ITS A 50% DECREASE.
    AAAAA
    THIS IS FUCKING FRUSTRATING.
    HOW THE HELL DO I DEAL WITH THIS
    I BUILT THIS INTER FUCKING SYSTEM, BUT IT IS NOT WORTH IT ITS JUST ENDLESS PAIN
    I COULD POWER THOUGH, BUT IT MAKES NO FUCKING SENSE
    AT SOME POINT IT JUST BREAKS DOWN. ITS NOT WORTH THE ADDITIONAL CALCULATION COST. ITS JUST NOT.
    WHY? BECAUSE I FORGET TO CALCULATE THE OVERHEAD VALUE AS WELL.
    ok...
    ok...
    so first of all: fuck you all.
    secondly, I need a new way to get a givin lords stats.
    I also need to reclaculate this intier fucking thing.
    so:
    I am going to put this at the top...


    for each lord, I gain 1a worth of space. this is only in effect after the second lord.
    -assuming- the hashmaps are the costly part, then... I am saving space. But this is hell.
    ok. I have convinced myself. because it only needs this one last part to be compleat, I will compleat it.



    if you have 1k starlords, taht is 1m values.
    but this, this BULLSHIT, cuts that down just a little bit.
    it cuts the 1k values down to






    */
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
    //constants for disorganized dataTypes:
    public static final String MTYPE_KEY_DOUBLE = "DOUBLE_";
    public static final String MTYPE_KEY_STRING = "STRING_";
    public static final String MTYPE_KEY_BOOLEAN = "BOOLEAN_";
    public static final String MTYPE_KEY_MUTABLE_STAT = "MUTABLE_STAT_";
    //default key for random value:
    public static final String MTYPE_KEY_NO_CUSTOM = "NULLKEY_";

    public static final String[] standerdDataAsArray = {
            MTYPE_KEY_DOUBLE,
            MTYPE_KEY_STRING,
            MTYPE_KEY_BOOLEAN,
            MTYPE_KEY_NO_CUSTOM
    };
    //constants for upgrade keys:
    public static final String TYPE_UPGRADE_WEIGHT_KEY = "UPGRADE_WEIGHT";
    public static final String TYPE_UPGRADE_COST_KEY = "UPGRADE_COST";
    public static final String TYPE_UPGRADE_AIWEIGHT_KEY = "UPGRADE_AIWEIGHT";
    @Deprecated
    public static final String BUILDER_UPGRADE_KEY = "UPGRADE_";
    @Deprecated
    public static final String UPGRADE_WEIGHT_KEY = "_WEIGHT_";
    @Deprecated
    public static final String UPGRADE_COST_KEY = "_COST_";
    @Deprecated
    public static final String UPGRADE_AIWEIGHT_KEY = "_AIWEIGHT_";

    //constants for fleet keys:
    public static final String FLEETCOMP_COMBAT = "FLEETCOMP_COMBAT";
    public static final String FLEETCOMP_FUEL = "FLEETCOMP_FUEL";
    public static final String FLEETCOMP_CARGO = "FLEETCOMP_CARGO";
    public static final String FLEETCOMP_PERSONAL = "FLEETCOMP_PERSONAL";
    public static final String FLEETCOMP_TUG = "FLEETCOMP_TUG";

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
        insureStructureFleetComp();
        insureStructureShipComp();

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
        if (!memory.hasItem(MTYPE_KEY_MUTABLE_STAT)){
            memory.setItem(MTYPE_KEY_MUTABLE_STAT,new MemCompressed_MutableStatScript());
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
        if (!MemCompressedMasterList.memory.containsKey(KEY_FLEET)){
            MemCompressedMasterList.getMemory().put(KEY_FLEET,new MemCompressed_FleetComp());
        }
        insureStructurePresent(KEY_FLEET);
    }
    private static void insureStructureShipComp(){
        if (!MemCompressedMasterList.memory.containsKey(KEY_SHIP)){
            MemCompressedMasterList.getMemory().put(KEY_SHIP,new MemCompressed_ShipComp());
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
