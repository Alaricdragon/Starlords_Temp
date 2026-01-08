package starlords.util.ScriptedValues;

import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.loading.WeaponSlotAPI;
import com.fs.starfarer.api.loading.WeaponSpecAPI;

public class SV_D_S_Weapons_OP extends SV_D_S_Weapons{
    protected double valueOfWeapon(ShipVariantAPI variant,WeaponSpecAPI spec, WeaponSlotAPI hullSpec,Object linkedObject){
        //todo: make sure this calculation works.
        return spec.getOrdnancePointCost(null, variant.getStatsForOpCosts());
    }
}
