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
import java.util.Random;

public interface UpgradeBase {
    /*@SneakyThrows
    void init(JSONObject json){
        if (json.has("defaultEnabled")){
            defaultOn = json.getBoolean("defaultEnabled");
        }
        if (json.has("weight")) weight = json.getDouble("weight");
    }*/
    double getWeight(Lord lord,UpgradeData data);
    double getAIWeight(Lord lord);
    int getCost(Lord lord,UpgradeData data);
    void applyUpgrade(Lord lord,UpgradeData data, int creditsSpent);
    boolean canPreformUpgrade(Lord lord, UpgradeData data);
    boolean hasEnoughCreditsForUpgrade(Lord lord, UpgradeData data);


}
