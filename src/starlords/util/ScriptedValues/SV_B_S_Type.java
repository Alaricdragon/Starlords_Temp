package starlords.util.ScriptedValues;

import com.fs.starfarer.api.combat.ShipVariantAPI;

public class SV_B_S_Type extends SV_B_S_Base{
    //ha ha ha... no.
    //I would have to look at this variant and gets its role. most things wont beable to see this do to the new nature of starlords.
    SV_String type;
    @Override
    public void init(ScriptedValueController value) {
        type = value.getNextString();
    }
    @Override
    public boolean calculate(ShipVariantAPI variant, Object linkedObject) {
        String type = this.type.getValue(linkedObject);
        return switch (type){
            default -> false;
        };
    }
}
