package starlords.util.lordUpgrades;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.util.Pair;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import starlords.PMC.PMC;
import starlords.person.Lord;
import starlords.util.CsvFilerReader;
import starlords.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static starlords.util.memoryUtils.Compressed.MemCompressedMasterList.*;

public class UpgradeController {
    /*
    * todo: 1) create the starlords 'csv' file, linking to all starlords, so upgrades can be considered from that data.
    *       2) create a command for printing the current fleet as a json file. for people to create there own starlords.
    *       3) create the 'types'
    *           -remember to read the to do on each one!
    *       4) create the random values for each type.
    *           -remember that Second In Command also needs random values. And it should be in the CSV file, no cheating!
    *       5) go into the starlord generater and CSV file every aspect of starlord generation
    *           -no. I am not fucking jokeing you asshole. This needs to happen. For real though, this upgrade upgrade has gotten out of control a little. but that should be alll.
    *
    *
    *
    *
    * */
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
        HashMap<String,JSONObject> list = CsvFilerReader.computeFile(jsons);
        for (String a : list.keySet()){
            JSONObject json = list.get(a);
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
        return BUILDER_UPGRADE_KEY +name+type+varuble;
    }
    public static Pair<HashMap<String,Double>,UpgradeBase> getUpgrade(Lord lord,UpgradeData data){
        //HashMap<String,Double> availableWeight = new HashMap<>();
        List<Pair<HashMap<String,Double>,UpgradeBase>> availableUpgrades = new ArrayList<>();
        List<Double> availableWeight = new ArrayList<>();
        HashMap<String,Double> costs = getCosts(lord,data);//weights and costs of all upgrades.
        HashMap<String,Double> weight = getWeights(lord,data);
        for (String b : weight.keySet()){
            if (costs.containsKey(b)){
                Pair<HashMap<String,Double>,UpgradeBase> temp = new Pair<>();
                temp.one = getCombined(b,lord,data,UPGRADE_COST_KEY);
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
            double cost = b.getCost(lord,data,getCombined(a,lord,data,UPGRADE_COST_KEY));
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
            double weight = b.getWeight(lord,data,getCombined(a,lord,data,UPGRADE_WEIGHT_KEY));
            if (weight <= 0) continue;
            output.put(a,weight);
        }
        return output;
    }
    private static HashMap<String,Double> getCombined(String upgradeID,Lord lord, UpgradeData data,String typeID){
        HashMap<String,Double> output = new HashMap<>();
        ArrayList<String> mods = upgrades.get(upgradeID).getWeightModifiers(lord,data);
        for (String a : mods) {
            String id = getNameInMemory(upgradeID,typeID,a);
            double value = (double) lord.getMemory().getCompressedDouble(id);
            for (Pair<Double, PMC> b : lord.getPMCs()){
                value *= (b.one * (double) b.two.getCOMPRESSED_MEMORY().getItem(MTYPE_KEY_DOUBLE).getItem(id));
            }
            output.put(a,value);
        }
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
            if (data.wealthReservedForUpgrades <= 0) break;
        }
        lord.addWealth(-1*data.wealthUsed);
    }
}
