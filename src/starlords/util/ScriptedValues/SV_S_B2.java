package starlords.util.ScriptedValues;

public class SV_S_B2 implements SV_String{
    private SV_Boolean[] bols;
    private SV_String[] values;
    @Override
    public void init(ScriptedValueController value) {
        int size = (int) value.getNextDouble().getValue(null);
        bols = new SV_Boolean[size/2];
        values = new SV_String[size/2];
        for (int a = 0; a < size / 2; a++){
            bols[a] = value.getNextBoolean();
            values[a] = value.getNextString();
        }
    }

    @Override
    public String getValue(Object linkedObject) {
        for (int a = 0; a < bols.length; a++){
            if (bols[a].getValue(linkedObject)) return values[a].getValue(linkedObject);
        }
        return "";
    }
}
