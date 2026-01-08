package starlords.util.ScriptedValues;

public class SV_B_B2 implements SV_Boolean{
    private SV_Boolean[] bols;
    private SV_Boolean[] values;
    @Override
    public void init(ScriptedValueController value) {
        int size = (int) value.getNextDouble().getValue(null);
        bols = new SV_Boolean[size/2];
        values = new SV_Boolean[size/2];
        for (int a = 0; a < size / 2; a++){
            bols[a] = value.getNextBoolean();
            values[a] = value.getNextBoolean();
        }
    }

    @Override
    public boolean getValue(Object linkedObject) {
        for (int a = 0; a < bols.length; a++){
            if (bols[a].getValue(linkedObject)) return values[a].getValue(linkedObject);
        }
        return false;
    }
}
