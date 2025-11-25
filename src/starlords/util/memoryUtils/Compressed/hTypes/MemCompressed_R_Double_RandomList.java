package starlords.util.memoryUtils.Compressed.hTypes;

import starlords.util.Utils;

import java.util.ArrayList;

public class MemCompressed_R_Double_RandomList extends MemCompressed_R_Double_Base{
    private ArrayList<Object> doubles;
    ArrayList<Object> weight;
    public MemCompressed_R_Double_RandomList(ArrayList<Object> doubles, ArrayList<Object> weight){
        this.doubles = doubles;
        this.weight = weight;
    }

    @Override
    public double getRandom(Object linkedObject) {
        Utils.getStringsFromArrayWithScripts(doubles,linkedObject);
        Object data = Utils.weightedSample(doubles,Utils.getDoublesFromArrayWithScripts(weight,linkedObject), Utils.rand);
        Object script = Utils.isScript(data);
        if (script != null) return ((MemCompressed_R_Double_Base)script).getRandom(linkedObject);
        return (Double) data;
    }
}
