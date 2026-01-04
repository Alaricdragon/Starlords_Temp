package starlords.util.ScriptedValues;

import starlords.person.Lord;
import starlords.util.ScriptedValues.holders.H_SV_CombatSkills;

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
        if (linkedObject instanceof H_SV_CombatSkills){
            Lord lord = ((H_SV_CombatSkills) linkedObject).lord;
            return lord.getCulture().equals(faction);
        }
        return false;
    }
}
