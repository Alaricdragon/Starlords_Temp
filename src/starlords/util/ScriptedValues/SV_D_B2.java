package starlords.util.ScriptedValues;

public class SV_D_B2 implements SV_Double{
    private SV_Boolean[] bols;
    private SV_Double[] values;
    @Override
    public void init(ScriptedValueController value) {
        int size = (int) value.getNextDouble().getValue(null);
        bols = new SV_Boolean[size/2];
        values = new SV_Double[size/2];
        for (int a = 0; a < size / 2; a++){
            bols[a] = value.getNextBoolean();
            values[a] = value.getNextDouble();
        }
    }

    @Override
    public double getValue(Object linkedObject) {
        for (int a = 0; a < bols.length; a++){
            if (bols[a].getValue(linkedObject)) return values[a].getValue(linkedObject);
        }
        return 0;
    }
}
