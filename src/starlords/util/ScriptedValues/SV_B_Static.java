package starlords.util.ScriptedValues;

public class SV_B_Static implements SV_Boolean{
    private boolean data;
    public SV_B_Static(boolean data){
        this.data = data;
    }
    @Override
    public boolean getValue(Object linkedObject) {
        return data;
    }

    @Override
    public void init(ScriptedValueController value) {

    }
}
