package starlords.util.ScriptedValues;

public class SV_O_Static implements SV_Object{
    private Object data;
    public SV_O_Static(Object data){
        this.data = data;
    }
    @Override
    public Object getValue(Object linkedObject) {
        return data;
    }
    @Override
    public void init(ScriptedValueController value) {

    }
}
