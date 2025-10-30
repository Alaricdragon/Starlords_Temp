package starlords.util.lordUpgrades;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.util.Pair;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import starlords.person.Lord;
import starlords.util.Utils;
import starlords.util.memoryUtils.Compressed.MemCompressedMasterList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static starlords.util.memoryUtils.Compressed.MemCompressedMasterList.*;

public class UpgradeController {
    /*so: I am compleatly remaking this intier class.
    * why? because I changed the internal data, and it now makes more sense to just... not use the newly created MemCompressedOrganizer to handle the following:
    *   1) the random data applied to a starlord at starlord creation.
    *   2) the internal storge of the random data in relation to the star lords.
    * */

    /*more notes:
    * so, what structure do I want the upgrades to have?
    * first of all, I dont want each starlord to hold onto the identifying string for each upgrade. so:
    * I will create a stored bit of data that holds a hashmap organizing all Strings and what data they represent in an upgrade. (this will then be organized into a hashmap when given to an upgrade class).
    * each time I load a save, I will need to look and compare the new CSV file data to the old one, to make sure I am getting the right data.
    * (so if a new data point is added to a upgrade, all starlords get the new data point added to there 'inventory', and the data in storage related to said upgrade is reorganized)
    * this should prevent... issues. to be blunt. also it stops all starlords from needing to map each data point. that should count for something, right?*/
    /*ok... ok...
    * so heres what I know:
    * 1) I asked in the misc modding questions how to read a CSV file. I think that works, but it needs testing.
    * 2) I need to spend some time considering how to organize the upgrade systems, and how to determine what upgrade I should be using for a given ship
    *
    * notes on upgrade CSV:
    * 1) make it so the upgrade CSV can have multiple string Ids that match to a starlords ability to upgrade (for both the weight of preforming this upgrade, the AI weight of preforming a upgrade (unused until I fix the fucking AI), and for the cost of the upgrade)
    *    please note that the string Ids would need to be placed within a starlord in the starlord.json (with a value between -1 and 1), like so:
    *    "upgradeData":{
    *       "upgradeType":{
    *           "enabled": true,
    *
    *           "thingA": 0.75,
    *           "thingB": -0.15,
    *           "thingC": 0
    *       }
    *   }
    *
    * 2) make it so a given upgrade can have the 'default random' boolean. if true, the values of a given upgrade for each starlord would be randomized on starlord creation. otherwise, they would be zero.
    *    -note: have this be changeable in the class as well. just to make things easyer
    * 3) make it so there is a 'default enabled' boolean. if true, the upgrade can be used by everyone by default.
    * 4) remember to make everything modifiable in the 'upgrade' script. the one matching to each and every upgrade type (by default, this will be officers, ships, S-mods, and so forth.)
    * 5) the 'group' data allows a given upgrade to be added to a group. so you can enable and disable them in groups.
    *    if something is part of a 'group'....
    *    ... I dont like groups. I dont like them at all. they might be needed later, but for now let sleeping dogos lie.*/

    @Getter
    private static HashMap<String, UpgradeBase> upgrades = new HashMap<>();
    @Getter
    private static HashMap<String, Boolean> defaultOn = new HashMap<>();
    @Getter
    @Deprecated
    private static HashMap<String, ArrayList<String>> groups = new HashMap<>();

    private static final Logger log = Global.getLogger(UpgradeController.class);
    @SneakyThrows
    public static void init() {
        String path = "data/lords/upgrades.csv";
        JSONArray jsons = Global.getSettings().loadCSV(path, true);
        for (int a = 0; a < jsons.length(); a++){
            JSONObject json = jsons.getJSONObject(a);
            String id = json.getString("id");
            boolean bol = json.getBoolean("defaultEnabled");
            String path2 = json.getString("script");
            upgrades.put(id, (UpgradeBase) Global.getSettings().getInstanceOfScript(path2));
            defaultOn.put(id,bol);
        }
    }
    public static String getRandomDataID(String[] data,String id){
        String type = data[2];
        switch (type){
            case "COST":
                type = UPGRADE_COST_KEY;
                break;
            case "WEIGHT":
                type = UPGRADE_WEIGHT_KEY;
                break;
        }
        return getNameInMemory(data[1],type,id);
    }

    private static String getNameInMemory(String name,String type,String varuble){
        return TYPE_UPGRADE_KEY+name+type+varuble;
    }
    public static Pair<HashMap<String,Double>,UpgradeBase> getUpgrade(Lord lord,UpgradeData data){
        //HashMap<String,Double> availableWeight = new HashMap<>();
        List<Pair<HashMap<String,Double>,UpgradeBase>> availableUpgrades = new ArrayList<>();
        List<Double> availableWeight = new ArrayList<>();
        HashMap<String,Double> costs = getCosts(lord,data);
        HashMap<String,Double> weight = getWeights(lord,data);
        for (String b : weight.keySet()){
            if (costs.containsKey(b)){
                Pair<HashMap<String,Double>,UpgradeBase> temp = new Pair<>();
                temp.one = getCombinedCost(b,lord,data);
                temp.two = upgrades.get(b);
                availableUpgrades.add(temp);
                availableWeight.add(weight.get(b));
            }
        }
        //get a random upgrade from here.
        return Utils.weightedSample(availableUpgrades,availableWeight,Utils.rand);
    }
    public static HashMap<String,Double> getCosts(Lord lord, UpgradeData data){
        HashMap<String,Double> output = new HashMap<>();
        double credits = data.wealthReservedForUpgrades;
        for (String a : upgrades.keySet()){
            UpgradeBase b = upgrades.get(a);
            double cost = b.getCost(lord,data,getCombinedCost(a,lord,data));
            //todo: modify the weight with the weight from all available modifiers here (example: faction mods, PMC mods, Settings mods)
            if (cost < credits) continue;
            output.put(a,cost);
        }
        return output;
    }
    public static HashMap<String,Double> getWeights(Lord lord, UpgradeData data){
        HashMap<String,Double> output = new HashMap<>();
        for (String a : upgrades.keySet()){
            UpgradeBase b = upgrades.get(a);
            if(!b.canPreformUpgrade(lord,data)) continue;
            double weight = b.getWeight(lord,data,getCombinedWeight(a,lord,data));
            if (weight <= 0) continue;
            output.put(a,weight);
        }
        return output;
    }
    public static HashMap<String,Double> getCombinedWeight(String upgradeID,Lord lord, UpgradeData data){
        HashMap<String,Double> output = new HashMap<>();
        //MemCompressedMasterList.getMemory().get(LORD_KEY);

        //todo: modify the weight with the weight from all available modifiers here (example: faction mods, PMC mods, Settings mods)
        //so... how is this going to work:
        //1: I need to save all the identifiers of all the upgrade datas when I am loading the random data.
        //2: I need to call them here, retranslating them back though only the upgrade ID, and the modifier ID.
        //3: I need to remember that there are multiple 'forms' a given random can take. I need to...
        //   ... ok this is not the case. the stored data --not the organizer-- is always a double. so I only need to multiply them all together
        //   ... also note: I  might need more modifiers, like max S-mods or something. that would be kinda cool I suppose....
        return output;
    }
    public static HashMap<String,Double> getCombinedCost(String upgradeID, Lord lord, UpgradeData data){
        HashMap<String,Double> output = new HashMap<>();

        return output;
    }
    public void performUpgrades(Lord lord){
        UpgradeData data = new UpgradeData();
        data.wealthAtUpgradeStart = lord.getWealth();
        data.wealthReservedForUpgrades = lord.getWealth();
        while(true) {
            Pair<HashMap<String,Double>,UpgradeBase> pair = getUpgrade(lord, data);
            if (pair == null) break;
            UpgradeBase upgrade = pair.two;
            HashMap<String,Double> combinedCost = pair.one;
            double cost = upgrade.getCost(lord,data,combinedCost);
            data.wealthReservedForUpgrades -= cost;
            data.wealthUsed += cost;
            upgrade.applyUpgrade(lord,data,cost,combinedCost);
        }
        lord.addWealth(-1*data.wealthUsed);
    }
}
