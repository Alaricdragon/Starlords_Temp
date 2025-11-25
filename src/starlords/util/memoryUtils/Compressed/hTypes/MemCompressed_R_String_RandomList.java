package starlords.util.memoryUtils.Compressed.hTypes;

import starlords.util.Utils;

import java.util.ArrayList;

public class MemCompressed_R_String_RandomList extends MemCompressed_R_String_Base{
    private ArrayList<Object> strings;
    private ArrayList<Object> weight;
    public MemCompressed_R_String_RandomList(ArrayList<Object> strings, ArrayList<Object> weight){
        this.strings = strings;
        this.weight = weight;
    }

    @Override
    public String getRandom(Object linkedObject) {
        Utils.getStringsFromArrayWithScripts(strings,linkedObject);
        Object data = Utils.weightedSample(strings,Utils.getDoublesFromArrayWithScripts(weight,linkedObject), Utils.rand);
        Object script = Utils.isScript(data);
        if (script != null) return ((MemCompressed_R_String_Base)script).getRandom(linkedObject);
        return (String) data;
    }
}
