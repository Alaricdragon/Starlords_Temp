package starlords.util.ScriptedValues;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import starlords.util.ScriptedValues.holders.H_SV_CombatSkills;
import starlords.util.fleetCompasition.ShipCompositionData;

public class SV_B_S_Size extends SV_B_S_Base{
    SV_String size;
    @Override
    public void init(ScriptedValueController value) {
        size = value.getNextString();
    }
    public boolean calculate(ShipVariantAPI variant, Object linkedObject){
        String key = size.getValue(linkedObject);
        return switch (key){
            case "FIGHTER" -> variant.getHullSpec().getHullSize().equals(ShipAPI.HullSize.FIGHTER);
            case "FRIGATE" -> variant.getHullSpec().getHullSize().equals(ShipAPI.HullSize.FRIGATE);
            case "DESTROYER" -> variant.getHullSpec().getHullSize().equals(ShipAPI.HullSize.DESTROYER);
            case "CRUISER" -> variant.getHullSpec().getHullSize().equals(ShipAPI.HullSize.CRUISER);
            case "CAPITAL_SHIP" -> variant.getHullSpec().getHullSize().equals(ShipAPI.HullSize.CAPITAL_SHIP);
            default -> false;
        };
    }
}
