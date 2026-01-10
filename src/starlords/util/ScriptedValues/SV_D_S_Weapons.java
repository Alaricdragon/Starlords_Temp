package starlords.util.ScriptedValues;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.loading.WeaponSlotAPI;
import com.fs.starfarer.api.loading.WeaponSpecAPI;
import starlords.util.ScriptedValues.holders.H_SV_CombatSkills;
import starlords.util.fleetCompasition.ShipCompositionData;

public class SV_D_S_Weapons implements SV_Double{
    private SV_String type;
    private SV_String mount;
    private SV_String size;
    private SV_String ammo;
    private SV_String beamType;// (any,any beam,substaned,burst,not beam)
    private SV_String damageType;//ANY,ENERGY,HIGH EXPLOSIVE, KINETIC, FRAGMENTATION, ANY
    private SV_String[] ai_hint;//ANY,ENERGY,HIGH EXPLOSIVE, KINETIC, FRAGMENTATION, ANY
    private SV_Double minRange;
    private SV_Double maxRange;
    @Override
    public void init(ScriptedValueController value) {
        mount = value.getNextString();
        type = value.getNextString();
        size = value.getNextString();
        ammo = value.getNextString();

        beamType = value.getNextString();
        damageType = value.getNextString();
        int size = (int) value.getNextDouble().getValue(null);
        ai_hint = new SV_String[size];
        for (int a = 0; a < ai_hint.length; a++) ai_hint[a] = value.getNextString();

        minRange = value.getNextDouble();
        maxRange = value.getNextDouble();

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
        double devision = 1;
        String type = this.type.getValue(linkedObject);
        String mount = this.mount.getValue(linkedObject);
        String size = this.size.getValue(linkedObject);
        String ammo = this.ammo.getValue(linkedObject);
        String beamType = this.beamType.getValue(linkedObject);
        String damageType = this.damageType.getValue(linkedObject);
        String[] ai_hint = new String[this.ai_hint.length];
        for (int a = 0; a < ai_hint.length; a++) ai_hint[a] = this.ai_hint[a].getValue(linkedObject);
        double minRange = this.minRange.getValue(linkedObject);
        double maxRange = this.maxRange.getValue(linkedObject);
        ShipHullSpecAPI hull = variant.getHullSpec();
        //ship.getAllWeapons();
        for (String a : variant.getFittedWeaponSlots()){
            if (isUsable(variant.getWeaponSpec(a),hull.getWeaponSlot(a),type,mount,size,ammo,beamType,damageType,ai_hint,minRange,maxRange)) count+= getValueOfWeapon(variant,variant.getWeaponSpec(a),hull.getWeaponSlot(a),linkedObject);
            else devision+=getFailedValue(variant,variant.getWeaponSpec(a),hull.getWeaponSlot(a),type,mount,size,ammo,beamType,damageType,ai_hint,minRange,maxRange,linkedObject);
        }
        return count/devision;
    }
    protected double getFailedValue(ShipVariantAPI variant,WeaponSpecAPI spec, WeaponSlotAPI hullSpec, String type, String mount, String size,String ammo,String beamType,String damageType,String[] ai_hint,double minRange, double maxRange,Object linkedObject){
        return 0;
    }
    private boolean isUsable(WeaponSpecAPI spec, WeaponSlotAPI hullSpec, String type, String mount, String size,String ammo,String beamType,String damageType,String[] ai_hint,double minRange, double maxRange) {
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
                || !isAmmo(ammo,spec)
                || !isProjectile(beamType,spec)
                || !isDamage(damageType,spec)
                || !isRange(minRange,maxRange,spec)
                || !isAIHint(ai_hint,spec)
                ) return false;
        }
        return true;
    }
    private boolean isType(String type, WeaponAPI.WeaponType wepType){
        return switch (type) {
            case "ENERGY" -> wepType.equals(WeaponAPI.WeaponType.ENERGY);
            case "BALLISTIC" -> wepType.equals(WeaponAPI.WeaponType.BALLISTIC);
            case "MISSILES" -> wepType.equals(WeaponAPI.WeaponType.MISSILE);
            case "HYBRID" -> wepType.equals(WeaponAPI.WeaponType.HYBRID);
            case "SYNERGY" -> wepType.equals(WeaponAPI.WeaponType.SYNERGY);
            case "COMPOSITE" -> wepType.equals(WeaponAPI.WeaponType.COMPOSITE);
            case "UNIVERSAL" -> wepType.equals(WeaponAPI.WeaponType.UNIVERSAL);
            case "ANY_BALLISTIC" ->
                    wepType.equals(WeaponAPI.WeaponType.UNIVERSAL) || wepType.equals(WeaponAPI.WeaponType.HYBRID) || wepType.equals(WeaponAPI.WeaponType.COMPOSITE) || wepType.equals(WeaponAPI.WeaponType.BALLISTIC);
            case "ANY_MISSILE" ->
                    wepType.equals(WeaponAPI.WeaponType.UNIVERSAL) || wepType.equals(WeaponAPI.WeaponType.SYNERGY) || wepType.equals(WeaponAPI.WeaponType.COMPOSITE) || wepType.equals(WeaponAPI.WeaponType.MISSILE);
            case "ANY_ENERGY" ->
                    wepType.equals(WeaponAPI.WeaponType.UNIVERSAL) || wepType.equals(WeaponAPI.WeaponType.SYNERGY) || wepType.equals(WeaponAPI.WeaponType.HYBRID) || wepType.equals(WeaponAPI.WeaponType.ENERGY);
            case "BUILT_IN" -> wepType.equals(WeaponAPI.WeaponType.BUILT_IN);
            case "DECORATIVE" -> wepType.equals(WeaponAPI.WeaponType.DECORATIVE);
            case "LAUNCH_BAY" -> wepType.equals(WeaponAPI.WeaponType.LAUNCH_BAY);
            case "STATION_MODULE" -> wepType.equals(WeaponAPI.WeaponType.STATION_MODULE);
            case "SYSTEM" -> wepType.equals(WeaponAPI.WeaponType.SYSTEM);
            default -> true;
        };
    }
    private boolean isSize(String size,WeaponAPI.WeaponSize wepSize){
        return switch (size) {
            case "SMALL" -> wepSize.equals(WeaponAPI.WeaponSize.SMALL);
            case "MEDIUM" -> wepSize.equals(WeaponAPI.WeaponSize.MEDIUM);
            case "LARGE" -> wepSize.equals(WeaponAPI.WeaponSize.LARGE);
            default -> true;
        };
    }
    private boolean isAmmo(String ammo,WeaponSpecAPI spec){
        return switch (ammo) {
            case "RECHARGE" -> {
                if (spec.getMaxAmmo() == 0) yield false;
                yield spec.getMaxAmmo() > 0 && spec.getAmmoPerSecond() > 0;
            }
            case "NO_RECHARGE" -> spec.getMaxAmmo() > 0 && spec.getAmmoPerSecond() == 0;
            case "NO_AMMO" -> spec.getMaxAmmo() == 0;
            case "ANY_AMMO" -> spec.getMaxAmmo() > 0;
            default -> true;
        };
    }
    private boolean isDamage(String type,WeaponSpecAPI spec){
        return switch (type) {
            case "ANY" -> true;
            case "ENERGY" -> spec.getDamageType().equals(DamageType.ENERGY);
            case "FRAGMENTATION" -> spec.getDamageType().equals(DamageType.FRAGMENTATION);
            case "HIGH_EXPLOSIVE" -> spec.getDamageType().equals(DamageType.HIGH_EXPLOSIVE);
            case "KINETIC" -> spec.getDamageType().equals(DamageType.KINETIC);
            case "OTHER" -> spec.getDamageType().equals(DamageType.OTHER);
            default -> false;
        };
    }
    private boolean isProjectile(String type,WeaponSpecAPI spec){
        return switch (type) {
            case "ANY" -> true;
            case "ANY_BEAM" -> spec.isBeam() || spec.isBurstBeam();
            case "ANY_PROJECTILE" -> !spec.isBeam() && !spec.isBurstBeam();
            case "BURST_BEAM" -> spec.isBurstBeam();
            case "CONTINUES_BEAM" -> spec.isBeam() && !spec.isBurstBeam();
            default -> false;
        };
    }
    private boolean isRange(double min, double max,WeaponSpecAPI spec){
        return (min == 0 || spec.getMaxRange() >= min) && (max == 0 || spec.getMaxRange() <= max);
    }
    private boolean isAIHint(String[] hint, WeaponSpecAPI spec){
        for (String a : hint) if (!isAIHintSingle(a,spec)) return false;
        return true;
    }
    private boolean isAIHintSingle(String hint, WeaponSpecAPI spec){
        return switch (hint) {
            case "ANY" -> true;
            case "PD" -> spec.getAIHints().contains(WeaponAPI.AIHints.PD);
            case "NOT_PD" -> !spec.getAIHints().contains(WeaponAPI.AIHints.PD);
            case "STRIKE" -> spec.getAIHints().contains(WeaponAPI.AIHints.STRIKE);
            case "NOT_STRIKE" -> !spec.getAIHints().contains(WeaponAPI.AIHints.STRIKE);
            case "HEATSEEKER" -> spec.getAIHints().contains(WeaponAPI.AIHints.HEATSEEKER);
            case "NOT_HEATSEEKER" -> !spec.getAIHints().contains(WeaponAPI.AIHints.HEATSEEKER);
            default -> false;
        };
    }
    protected double getValueOfWeapon(ShipVariantAPI variant, WeaponSpecAPI spec, WeaponSlotAPI hullSpec, Object linkedObject){
        return 1;
    }
}
