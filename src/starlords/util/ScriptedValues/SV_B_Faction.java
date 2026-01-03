package starlords.util.ScriptedValues;

import starlords.person.Lord;

public class SV_B_Faction implements SV_Boolean{
    private SV_String a;
    @Override
    public void init(ScriptedValueController value) {
        a = value.getNextString();
    }

    @Override
    public boolean getValue(Object linkedObject) {
        String faction = a.getValue(linkedObject);
        if ((linkedObject instanceof Lord)){
            Lord lord = (Lord) linkedObject;
            return lord.getFaction().equals(faction);
        }
        return false;
    }
}
