package starlords.util.ScriptedValues;

import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.loading.WeaponSlotAPI;
import com.fs.starfarer.api.loading.WeaponSpecAPI;

public class SV_D_S_Weapons_Ratio extends SV_D_S_Weapons{
    @Override
    protected double getFailedValue(ShipVariantAPI variantAPI, WeaponSpecAPI spec, WeaponSlotAPI hullSpec, String type, String mount, String size, String ammo, String beamType, String damageType, String[] ai_hint, double minRange, double maxRange, Object linkedObject) {
        return 1;
    }
}
