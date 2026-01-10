package starlords.util.ScriptedValues;

import com.fs.starfarer.api.combat.ShieldAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;

public class SV_B_S_DefenseType extends SV_B_S_Base{
    private SV_String type;
    @Override
    public void init(ScriptedValueController value) {
        type = value.getNextString();
    }

    @Override
    public boolean calculate(ShipVariantAPI variant, Object linkedObject) {
        String type = this.type.getValue(linkedObject);
        /*ANY
                SHIELD_ANY
        SHIELD_FRONT
                SHIELD_OMI
        PHASE
                DAMPER_FIELD*/
        return switch (type){
            case "ANY" -> true;
            case "NONE" -> variant.getHullSpec().getDefenseType().equals(ShieldAPI.ShieldType.NONE);
            case "SHIELD_ANY" -> variant.getHullSpec().getDefenseType().equals(ShieldAPI.ShieldType.FRONT) || variant.getHullSpec().getDefenseType().equals(ShieldAPI.ShieldType.OMNI);
            case "SHIELD_FRONT" -> variant.getHullSpec().getDefenseType().equals(ShieldAPI.ShieldType.FRONT);
            case "SHIELD_OMI" -> variant.getHullSpec().getDefenseType().equals(ShieldAPI.ShieldType.OMNI);
            case "PHASE" -> variant.getHullSpec().getDefenseType().equals(ShieldAPI.ShieldType.PHASE);
            //case "DAMPER_FIELD" -> true;
            default -> false;
        };
    }
}
