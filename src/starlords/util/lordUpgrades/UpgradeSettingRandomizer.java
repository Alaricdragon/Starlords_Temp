package starlords.util.lordUpgrades;

import org.json.JSONObject;

import java.util.HashMap;

public class UpgradeSettingRandomizer{
    /*
    * so what does this do?
    * simple: it is a function that manages what the 'random values' for each upgrade setting are, before it is added to a starlords memory.
    * effectively, it is just a list that holds the random values each upgrade can have, and gives starlords an upgrade of an appropriate value.
    *
     */
    private HashMap<String,Double> minAI = new HashMap<>();
    private HashMap<String,Double> maxAI = new HashMap<>();

    private HashMap<String,Double> minUpgrade = new HashMap<>();
    private HashMap<String,Double> maxUpgrade = new HashMap<>();
    public UpgradeSettingRandomizer(JSONObject json){
        //????
    }

    public double getUpgradeWeight(String id) {
        double floor = minAI.get(id);
        double range = maxAI.get(id)- minAI.get(id);
        return (Math.random() * range)+floor;
    }

    public double getAIWeight(String id) {
        double floor = minUpgrade.get(id);
        double range = maxUpgrade.get(id)- minUpgrade.get(id);
        return (Math.random() * range)+floor;
    }
}
