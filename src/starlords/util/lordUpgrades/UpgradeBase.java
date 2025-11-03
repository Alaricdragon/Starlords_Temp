package starlords.util.lordUpgrades;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import starlords.person.Lord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public interface UpgradeBase {
    /*@SneakyThrows
    void init(JSONObject json){
        if (json.has("defaultEnabled")){
            defaultOn = json.getBoolean("defaultEnabled");
        }
        if (json.has("weight")) weight = json.getDouble("weight");
    }*/
    ArrayList<String> getAIModifiers(Lord lord, UpgradeData data);
    ArrayList<String> getCostModifiers(Lord lord, UpgradeData data);
    ArrayList<String> getWeightModifiers(Lord lord, UpgradeData data);
    double getWeight(Lord lord, UpgradeData data, HashMap<String,Double> modifiers);
    double getAIWeight(Lord lord, HashMap<String,Double> modifiers);
    double getCost(Lord lord,UpgradeData data, HashMap<String,Double> modifiers);
    void applyUpgrade(Lord lord,UpgradeData data, double creditsSpent, HashMap<String,Double> costModifiers);
    boolean canPreformUpgrade(Lord lord, UpgradeData data);
    //boolean hasEnoughCreditsForUpgrade(Lord lord, UpgradeData data);


}
