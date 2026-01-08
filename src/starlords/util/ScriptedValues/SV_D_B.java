package starlords.util.ScriptedValues;

public class SV_D_B implements SV_Double{
    private SV_Boolean bol;
    private SV_Double valueA;
    private SV_Double valueB;
    @Override
    public void init(ScriptedValueController value) {
        bol = value.getNextBoolean();
        valueA = value.getNextDouble();
        valueB = value.getNextDouble();
    }
    @Override
    public double getValue(Object linkedObject) {
        if (bol.getValue(linkedObject)) return valueA.getValue(linkedObject);
        return valueB.getValue(linkedObject);
    }

}
