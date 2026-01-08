package starlords.util.ScriptedValues;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipHullSpecAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.loading.WeaponSlotAPI;
import com.fs.starfarer.api.loading.WeaponSpecAPI;
import starlords.util.ScriptedValues.holders.H_SV_CombatSkills;
import starlords.util.fleetCompasition.ShipCompositionData;

public class SV_D_S_Weapons implements SV_Double{
    /*this is still missing the following varubles:
        projectile type: (beam, projectile, any)
        role: (strike, point defense, area denial, suppression, and whatever the hell else exists)
            -base this off 'role for variant' however that works.
        range: min / max. (maybe format it as min,max? no that would interfear when I try to get the value itself.)
            maybe make it so setting range to 0 or less will case that value to be iggnored? that could work.
            its a lot of values though.

        ...
        having one class for all weapon possibility is very nice. I think i like that.
        Its not like I will use it that mush at all.

    */

    private SV_String type;
    private SV_String mount;
    private SV_String size;
    private SV_String ammo;
    @Override
    public void init(ScriptedValueController value) {
        mount = value.getNextString();
        type = value.getNextString();
        size = value.getNextString();
        ammo = value.getNextString();
    }
    @Override
    public double getValue(Object linkedObject) {
        if (linkedObject instanceof H_SV_CombatSkills) return calculate(
                ((H_SV_CombatSkills) linkedObject).shipCompositionData.variant,linkedObject);
        if (linkedObject instanceof ShipCompositionData) return calculate(
                ((ShipCompositionData) linkedObject).variant,linkedObject);
        return 0;
    }
    private double calculate(String variant,Object linkedObject){
        return calculate(Global.getSettings().getVariant(variant),linkedObject);
    }
    private double calculate(ShipVariantAPI variant,Object linkedObject){
        double count = 0;
        String type = this.type.getValue(linkedObject);
        String mount = this.mount.getValue(linkedObject);
        String size = this.size.getValue(linkedObject);
        String ammo = this.ammo.getValue(linkedObject);
        ShipHullSpecAPI hull = variant.getHullSpec();
        for (String a : variant.getFittedWeaponSlots()){
            if (isUsable(variant.getWeaponSpec(a),hull.getWeaponSlot(a),type,mount,size,ammo)) count+=valueOfWeapon(variant,variant.getWeaponSpec(a),hull.getWeaponSlot(a),linkedObject);
        }
        return count;
    }
    private boolean isUsable(WeaponSpecAPI spec, WeaponSlotAPI hullSpec, String type, String mount, String size,String ammo) {
        boolean isMount = mount.equals("BOTH") || mount.equals("MOUNT");
        boolean isWeapon = mount.equals("BOTH") || mount.equals("WEAPON");
        if (isMount) {
            if (!isType(type,spec.getMountType())
                || !isSize(size,hullSpec.getSlotSize())
                ) return false;
        }
        if (isWeapon){
            if (!isType(type,spec.getType())
                || !isSize(size,spec.getSize())
                || !isAmmo(ammo,spec)) return false;
        }
        return true;
    }
    private boolean isType(String type, WeaponAPI.WeaponType wepType){
        switch (type) {
            case "ENERGY":
                return wepType.equals(WeaponAPI.WeaponType.ENERGY);
            case "BALLISTIC":
                return wepType.equals(WeaponAPI.WeaponType.BALLISTIC);
            case "MISSILES":
                return wepType.equals(WeaponAPI.WeaponType.MISSILE);

            case "HYBRID":
                return wepType.equals(WeaponAPI.WeaponType.HYBRID);
            case "SYNERGY":
                return wepType.equals(WeaponAPI.WeaponType.SYNERGY);
            case "COMPOSITE":
                return wepType.equals(WeaponAPI.WeaponType.COMPOSITE);
            case "UNIVERSAL":
                return wepType.equals(WeaponAPI.WeaponType.UNIVERSAL);

            case "ANY_BALLISTIC":
                return wepType.equals(WeaponAPI.WeaponType.UNIVERSAL) || wepType.equals(WeaponAPI.WeaponType.HYBRID) || wepType.equals(WeaponAPI.WeaponType.COMPOSITE) || wepType.equals(WeaponAPI.WeaponType.BALLISTIC);
            case "ANY_MISSILE":
                return wepType.equals(WeaponAPI.WeaponType.UNIVERSAL) || wepType.equals(WeaponAPI.WeaponType.SYNERGY) || wepType.equals(WeaponAPI.WeaponType.COMPOSITE) || wepType.equals(WeaponAPI.WeaponType.MISSILE);
            case "ANY_ENERGY":
                return wepType.equals(WeaponAPI.WeaponType.UNIVERSAL) || wepType.equals(WeaponAPI.WeaponType.SYNERGY) || wepType.equals(WeaponAPI.WeaponType.HYBRID) || wepType.equals(WeaponAPI.WeaponType.ENERGY);
            default:
            case "ANY":
                return true;

            case "BUILT_IN":
                return wepType.equals(WeaponAPI.WeaponType.BUILT_IN);
            case "DECORATIVE":
                return wepType.equals(WeaponAPI.WeaponType.DECORATIVE);
            case "LAUNCH_BAY":
                return wepType.equals(WeaponAPI.WeaponType.LAUNCH_BAY);
            case "STATION_MODULE":
                return wepType.equals(WeaponAPI.WeaponType.STATION_MODULE);
            case "SYSTEM":
                return wepType.equals(WeaponAPI.WeaponType.SYSTEM);
        }
    }
    private boolean isSize(String size,WeaponAPI.WeaponSize wepSize){
        switch (size){
            case "SMALL":
                return wepSize.equals(WeaponAPI.WeaponSize.SMALL);
            case "MEDIUM":
                return wepSize.equals(WeaponAPI.WeaponSize.MEDIUM);
            case "LARGE":
                return wepSize.equals(WeaponAPI.WeaponSize.LARGE);
            case "ANY":
            default:
                return true;
        }
    }
    private boolean isAmmo(String ammo,WeaponSpecAPI spec){
        switch (ammo){
            case "RECHARGE":
                if (spec.getMaxAmmo() == 0) return false;
                return spec.getMaxAmmo() > 0 && spec.getAmmoPerSecond() > 0;
            case "NO_RECHARGE":
                return spec.getMaxAmmo() > 0 && spec.getAmmoPerSecond() == 0;
            case "NO_AMMO":
                return spec.getMaxAmmo() == 0;
            case "ANY_AMMO":
                return spec.getMaxAmmo() > 0;
            case "ANY":
            default:
                return true;
        }
    }
    protected double valueOfWeapon(ShipVariantAPI variant,WeaponSpecAPI spec, WeaponSlotAPI hullSpec,Object linkedObject){
        return 1;
    }
}
