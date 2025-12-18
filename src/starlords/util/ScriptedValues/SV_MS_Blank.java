package starlords.util.ScriptedValues;

import starlords.util.math.StarLord_MutableStat;

public class SV_MS_Blank implements SV_MutableStat_Code{
    public SV_MS_Blank(){
    }
    @Override
    public StarLord_MutableStat getValue(Object linkedObject) {
        return new StarLord_MutableStat(1);
    }
}
