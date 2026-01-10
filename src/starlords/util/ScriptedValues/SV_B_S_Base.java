package starlords.util.ScriptedValues;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import starlords.util.ScriptedValues.holders.H_SV_CombatSkills;
import starlords.util.fleetCompasition.ShipCompositionData;

public abstract class SV_B_S_Base implements SV_Boolean{
    //this is for internal data. it just gets me the god dam ship I want to look at, from whatever data i need to look though.
    @Override
    public boolean getValue(Object linkedObject) {
        if (linkedObject instanceof H_SV_CombatSkills) return calculate(
                ((H_SV_CombatSkills) linkedObject).shipCompositionData.variant,linkedObject);
        if (linkedObject instanceof ShipCompositionData) return calculate(
                ((ShipCompositionData) linkedObject).variant,linkedObject);
        return false;
    }
    private boolean calculate(String variant,Object linkedObject){
        return calculate(Global.getSettings().getVariant(variant),linkedObject);
    }
    public abstract boolean calculate(ShipVariantAPI variant, Object linkedObject);
}
