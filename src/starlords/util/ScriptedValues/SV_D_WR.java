package starlords.util.ScriptedValues;

import starlords.util.randomLoader.WeightedRandomBase;

public class SV_D_WR implements SV_Double{
    private SV_Double min;
    private SV_Double max;
    private SV_Double i;
    private SV_Double target;
    @Override
    public double getValue(Object linkedObject) {
        return WeightedRandomBase.getRandom(target.getValue(linkedObject), i.getValue(linkedObject),max.getValue(linkedObject),min.getValue(linkedObject));
    }

    @Override
    public void init(ScriptedValueController value) {
        max = value.getNextDouble();
        min = value.getNextDouble();
        target = value.getNextDouble();
        i = value.getNextDouble();
    }
}
