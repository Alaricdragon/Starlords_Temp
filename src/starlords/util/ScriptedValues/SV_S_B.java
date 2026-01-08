package starlords.util.ScriptedValues;

public class SV_S_B implements SV_String{
    private SV_Boolean bol;
    private SV_String valueA;
    private SV_String valueB;
    @Override
    public void init(ScriptedValueController value) {
        bol = value.getNextBoolean();
        valueA = value.getNextString();
        valueB = value.getNextString();
    }
    @Override
    public String getValue(Object linkedObject) {
        if (bol.getValue(linkedObject)) return valueA.getValue(linkedObject);
        return valueB.getValue(linkedObject);
    }
}
