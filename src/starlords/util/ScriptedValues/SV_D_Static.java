package starlords.util.ScriptedValues;

public class SV_D_Static implements SV_Double{
    private double data;
    public SV_D_Static(double data){
        this.data = data;
    }
    @Override
    public double getValue(Object linkedObject) {
        return data;
    }

    @Override
    public void init(ScriptedValueController value) {

    }
}
