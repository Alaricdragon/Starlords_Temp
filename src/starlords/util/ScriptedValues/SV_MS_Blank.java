package starlords.util.ScriptedValues;

import starlords.util.math.StarLord_MutableStat;

public class SV_MS_Blank implements SV_MutableStat_Code{
    private String id, type;
    public SV_MS_Blank(String id, String type){
        this.id = id;
        this.type = type;
    }
    @Override
    public StarLord_MutableStat getValue(Object linkedObject) {
        return new StarLord_MutableStat(id,type,1,linkedObject);
    }
}
