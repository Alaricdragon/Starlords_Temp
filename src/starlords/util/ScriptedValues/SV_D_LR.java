package starlords.util.ScriptedValues;

import starlords.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class SV_D_LR implements SV_Double{
    private ArrayList<SV_Double> wights = new ArrayList<>();
    private ArrayList<SV_Double> values = new ArrayList<>();
    @Override
    public double getValue(Object linkedObject) {
        List<Double> weights = new ArrayList<>();
        for (SV_Double a: this.wights){
            weights.add(a.getValue(linkedObject));
        }
        List<Double> values = new ArrayList<>();
        for (SV_Double a: this.values){
            values.add(a.getValue(linkedObject));
        }
        return Utils.weightedSample(values,weights,Utils.rand);
    }

    @Override
    public void init(ScriptedValueController value) {
        int size = (int) value.getNextDouble().getValue(null) / 2;
        while(size >= 0){
            values.add(value.getNextDouble());
            wights.add(value.getNextDouble());
            size--;
        }
    }
}
