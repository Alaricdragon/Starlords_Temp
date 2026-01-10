package starlords.util.ScriptedValues;

import com.fs.starfarer.api.combat.ShipVariantAPI;

public class SV_B_S_Manufacturer extends SV_B_S_Base{
    SV_String type;
    @Override
    public void init(ScriptedValueController value) {
        type = value.getNextString();
    }
    @Override
    public boolean calculate(ShipVariantAPI variant, Object linkedObject) {
        return  variant.getHullSpec().getManufacturer().equals(type.getValue(linkedObject));
    }

}
