package starlords.util.lordUpgrades;

import lombok.Getter;
import org.json.JSONObject;
import starlords.person.Lord;

import java.util.HashMap;

public class UpgradeSetting {
    @Getter
    private HashMap<String,Double> upgradeValues = new HashMap<>();
    @Getter
    private HashMap<String,Double> AIValues = new HashMap<>();
    public double getUpgradeWeight(String id){
        return upgradeValues.get(id);
    }
    public double getAIWeight(String id){
        return AIValues.get(id);
    }
}
