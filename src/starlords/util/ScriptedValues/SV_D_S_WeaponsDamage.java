package starlords.util.ScriptedValues;

import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.loading.WeaponSlotAPI;
import com.fs.starfarer.api.loading.WeaponSpecAPI;

public class SV_D_S_WeaponsDamage extends SV_D_S_Weapons{
    //todo: find out how the hell I am going to get the relevant data.
    @Override
    protected double getValueOfWeapon(ShipVariantAPI variant, WeaponSpecAPI spec, WeaponSlotAPI hullSpec, Object linkedObject) {
        return super.getValueOfWeapon(variant, spec, hullSpec, linkedObject);
    }
}
