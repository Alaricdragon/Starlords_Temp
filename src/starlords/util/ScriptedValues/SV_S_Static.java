package starlords.util.ScriptedValues;

import lombok.Getter;

public class SV_S_Static implements SV_String{
    @Getter//this is a getter for rebuilding static string from none static constructs.
    private String data="";
    public SV_S_Static(String data){
        this.data = data;
    }
    @Override
    public String getValue(Object linkedObject) {
        return data;
    }

    @Override
    public void init(ScriptedValueController value) {

    }
}
