package starlords.util.math;

import lombok.Getter;
import lombok.Setter;
import starlords.util.ScriptedValues.ScriptedValueController;
import starlords.util.memoryUtils.Compressed_outdated.MemCompressedHolder;
import starlords.util.memoryUtils.Compressed_outdated.MemCompressedMasterList;

import java.util.HashMap;

public class StarLord_MutableStat{ //extends MutableStat {
    //so, I have ran into an issue with MutableStat. what if the base might change mid calculation?
    //any increases therefore cannot be multiplicative. so I forced a solution:
    /*todo:
        there is a possibility of using compressed memory for some stats here. It would be really frustrating, but I could do it... probably.
        this is the 'core' of the memory cost as well... it would likely be a considerable decrease in size. this is kinda important, giving how thick starlords can be (in terms of data)
        -
        what would I need to do:
        1) randoms.csv would need to get me values for this. thats simple
        2) I would need my own -list- of compressedOrganizers. one for each single fucking stat.
            -for the sake of data, I would need to store the ID of stat here. to beable to get its organizer.
        3) for repair, I would need a special function in each bit of compressed data organizer. in theory, this is simple enouth. just get the currect organizer and run its repair on each stat mod.
            -I could get the ID of the right organizer for each mod by doing something like KEY_LORD + TYPE_STATMOD + 'idOfStatMod'.
        -
        -) for now, i could simple... have very short strings? like 2 digit strings? but...
        -) that defeats the intier reason for this... mess I have coding myself into.
        -) ... no i will still need it in all likely hood.
        -) ... is this even worth it for how often I am going to be getting data? what is the cost on this compaired to memory compression? proboly not worth it. for long term storge its fine, but I might end up useing a data holdder instead, in the end.
        -) ... for now, I am iggnoring this.

    */
    @Getter
    private MemCompressedHolder<Double> dynamicFlatCompressedMods;
    @Getter
    private MemCompressedHolder<Double> multiCompressedMods;
    @Getter
    private MemCompressedHolder<Double> flatCompressedMods;

    private HashMap<String,Float> dynamicFlatMultiMods = new HashMap<>();
    private HashMap<String,Float> multiMods = new HashMap<>();
    private HashMap<String,Float> flatMods = new HashMap<>();
    private String type;

    @Getter
    @Setter
    private float baseValue;
    public static String getCompressedID(String valueID,String whatThisMemoryIsAppliedTo,String dataType){
        //data type is stored in: ScriptedValueController as ScriptedValueController.TYPE_MUTABLE_STAT_???;
        return whatThisMemoryIsAppliedTo+MemCompressedMasterList.MTYPE_KEY_MUTABLE_STAT+dataType+"_"+valueID;
    }
    public String getCompressedID(String id,String dataType){
        return getCompressedID(id,dataType);
    }
    public StarLord_MutableStat(String id,String type,float base,Object linkedObject) {
        baseValue = base;
        this.type = type;
        dynamicFlatCompressedMods = new MemCompressedHolder<>(MemCompressedMasterList.getMemory().get(getCompressedID(id, ScriptedValueController.TYPE_MUTABLE_STAT_BASE)),linkedObject);
        flatCompressedMods = new MemCompressedHolder<>(MemCompressedMasterList.getMemory().get(getCompressedID(id,ScriptedValueController.TYPE_MUTABLE_STAT_FLAT)),linkedObject);
        multiCompressedMods = new MemCompressedHolder<>(MemCompressedMasterList.getMemory().get(getCompressedID(id,ScriptedValueController.TYPE_MUTABLE_STAT_MULTI)),linkedObject);
    }
    public void addDynamicIncreaseMod(String id, float value){
        dynamicFlatMultiMods.put(id,value);
    }
    public float getDynamicIncreaseMod(String id){
        return dynamicFlatMultiMods.get(id);
    }


    public void addFlatMod(String id, float value){
        flatMods.put(id,value);
    }
    public float getFlatMod(String id){
        return flatMods.get(id);
    }

    public void addMultiMod(String id, float value){
        multiMods.put(id,value);
    }
    public float getMultiMod(String id){
        return multiMods.get(id);
    }


    public int getModifiedInt() {
        return (int) getModifiedValue();
    }

    public float getModifiedValue() {
        float value = getBaseValue();
        float output = getBaseValue();

        for (float a : flatMods.values()) output += a;
        for (double a : flatCompressedMods.getMap()) output += (float) a;

        for (float a : multiMods.values()) output *= (a);
        for (double a : multiCompressedMods.getMap()) output *= (float) (a);

        for (float a : dynamicFlatMultiMods.values()) output += (value*a);
        for (double a : dynamicFlatCompressedMods.getMap()) output += (float) (value*a);
        return output;
    }
}
