package starlords.util.ScriptedValues;

public class SV_B_B implements SV_Boolean{
    private SV_Boolean bol;
    private SV_Boolean valueA;
    private SV_Boolean valueB;
    @Override
    public void init(ScriptedValueController value) {
        bol = value.getNextBoolean();
        valueA = value.getNextBoolean();
        valueB = value.getNextBoolean();
    }

    @Override
    public boolean getValue(Object linkedObject) {
        if (bol.getValue(linkedObject)) return valueA.getValue(linkedObject);
        return valueB.getValue(linkedObject);
    }
}
