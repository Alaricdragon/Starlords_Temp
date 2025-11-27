package starlords.util.ScriptedValues;

import starlords.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class SV_S_LR implements SV_String{
    private ArrayList<SV_Double> wights = new ArrayList<>();
    private ArrayList<SV_String> values = new ArrayList<>();
    @Override
    public String getValue(Object linkedObject) {
        List<Double> weights = new ArrayList<>();
        for (SV_Double a: this.wights){
            weights.add(a.getValue(linkedObject));
        }
        List<String> values = new ArrayList<>();
        for (SV_String a: this.values){
            values.add(a.getValue(linkedObject));
        }
        return Utils.weightedSample(values,weights,Utils.rand);
    }

    @Override
    public void init(ScriptedValueController value) {
        int size = (int) value.getNextDouble().getValue(null) / 2;
        while(size >= 0){
            values.add(value.getNextString());
            wights.add(value.getNextDouble());
            size--;
        }
    }
}
