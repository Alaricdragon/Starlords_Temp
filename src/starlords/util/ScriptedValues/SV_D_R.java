package starlords.util.ScriptedValues;

import starlords.util.Utils;

public class SV_D_R implements SV_Double{
    SV_Double min;
    SV_Double max;
    @Override
    public double getValue(Object linkedObject) {
        double min = this.min.getValue(linkedObject);
        double range = max.getValue(linkedObject)-min;
        return (Utils.rand.nextDouble()*range)+min;
    }

    @Override
    public void init(ScriptedValueController value) {
        min = value.getNextDouble();
        max = value.getNextDouble();
    }
}
