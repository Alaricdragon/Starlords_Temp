package starlords.util.ScriptedValues;

import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.loading.WeaponSlotAPI;
import com.fs.starfarer.api.loading.WeaponSpecAPI;

public class SV_D_S_Weapons_Ratio_OP extends SV_D_S_Weapons_OP {
    @Override
    protected double getFailedValue(ShipVariantAPI variant, WeaponSpecAPI spec, WeaponSlotAPI hullSpec, String type, String mount, String size, String ammo, String beamType, String damageType, String[] ai_hint, double minRange, double maxRange, Object linkedObject) {
        //todo: make sure this calculation works. and does not, you know, crash the fucking game????
        return spec.getOrdnancePointCost(null, variant.getStatsForOpCosts());
    }
}
