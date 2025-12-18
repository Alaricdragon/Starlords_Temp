package starlords.util.math;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.util.Pair;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

public class StarLord_MutableStat{ //extends MutableStat {
    /*todo:
        NOTE: I am not doing this right now, but it does need to be compleated at some ponit.
        this has an inherent issue: I want to beable to recalculate this value with a bonch of other mutible stats attached.
        the issue:
            If I change a 'value' on any MutableStat that another MutableStat, the 'real' mutable stat fails to change.
        solutions:
            1) put a 'last changed' value on all StarLord_MutableStats.
            2) put StarLord_MutableStat in memory.
            3) put


    */

    private HashMap<String,Float> dynamicFlatMultiMods = new HashMap<>();
    private HashMap<String,Float> multiMods = new HashMap<>();
    private HashMap<String,Float> flatMods = new HashMap<>();
    @Getter
    private double calculatedValue;
    private double value;
    @Getter
    @Setter
    private double lastChanged=0;
    public StarLord_MutableStat(double value) {
        this.value = value;
    }

    public void setBaseValue(double value){
        this.value = value;
        recalculateValue();
    }
    public void setBaseValue(double value,StarLord_MutableStat... stats){
        this.value = value;
        recalculateValue(stats);
    }

    public float getDynamicIncreaseMod(String id){
        return dynamicFlatMultiMods.get(id);
    }
    public float getFlatMod(String id){
        return flatMods.get(id);
    }
    public float getMultiMod(String id){
        return multiMods.get(id);
    }

    public void addDynamicIncreaseMod(String id, float value){
        lastChanged = Global.getSector().getClock().getTimestamp();
        dynamicFlatMultiMods.put(id,value);
        recalculateValue();
    }
    public void addFlatMod(String id, float value){
        lastChanged = Global.getSector().getClock().getTimestamp();
        flatMods.put(id,value);
        recalculateValue();
    }
    public void addMultiMod(String id, float value){
        lastChanged = Global.getSector().getClock().getTimestamp();
        multiMods.put(id,value);
        recalculateValue();
    }

    public void removeDynamicIncreaseMod(String id){
        if (!dynamicFlatMultiMods.containsKey(id)) return;
        lastChanged = Global.getSector().getClock().getTimestamp();
        dynamicFlatMultiMods.remove(id);
        recalculateValue();
    }
    public void removeFlatMod(String id){
        if (!flatMods.containsKey(id)) return;
        lastChanged = Global.getSector().getClock().getTimestamp();
        flatMods.remove(id);
        recalculateValue();
    }
    public void removeMultiMod(String id){
        if (!multiMods.containsKey(id)) return;
        lastChanged = Global.getSector().getClock().getTimestamp();
        multiMods.remove(id);
        recalculateValue();
    }

    public void recalculateValue(){
        recalculateValue(this);
    }
    public void recalculateValue(StarLord_MutableStat... stats){
        double increase = 0;//calculateIncrease();
        //Pair<Double, Double> t = calculateDynamicMods();
        double neMulti = 1;
        double poMulti = 1;
        double finalMulti = 1;
        for (StarLord_MutableStat a : stats){
            increase += a.calculateIncrease();
            Pair<Double, Double> t = a.calculateDynamicMods();
            poMulti += (t.one - 1);
            neMulti *= t.two;
            finalMulti *= a.calculateMultiMods();
        }
        calculatedValue = value;
        calculatedValue += increase;
        calculatedValue += (value * poMulti);
        calculatedValue *= neMulti;
        calculatedValue *= finalMulti;
    }

    protected double calculateIncrease(){
        double value = 0;
        for (double a : this.flatMods.values()) value+=a;
        return value;
    }
    protected Pair<Double,Double> calculateDynamicMods(){
        double neMulti = 1;
        double poMulti = 1;
        for (double a : dynamicFlatMultiMods.values()){
            if (a >= 1) poMulti += (a-1);
            else neMulti *= a;
        }
        return new Pair<>(poMulti,neMulti);
    }
    protected double calculateMultiMods(){
        double multi = 1;
        for (double a : multiMods.values()) multi *= a;
        return multi;
    }
}
