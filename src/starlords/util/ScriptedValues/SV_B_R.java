package starlords.util.ScriptedValues;

import starlords.util.Utils;

public class SV_B_R implements SV_Boolean {
    private SV_Double data;
    @Override
    public boolean getValue(Object linkedObject) {
        return Utils.rand.nextDouble() <= data.getValue(linkedObject);
    }
    @Override
    public void init(ScriptedValueController value) {
        data = value.getNextDouble();
    }
}
