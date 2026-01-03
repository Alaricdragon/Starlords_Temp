package starlords.util.ScriptedValues;

public class SV_B_NOT implements SV_Boolean{
    SV_Boolean a;
    @Override
    public void init(ScriptedValueController value) {
        a = value.getNextBoolean();
    }

    @Override
    public boolean getValue(Object linkedObject) {
        return !a.getValue(linkedObject);
    }
}
