package starlords.util.lordUpgrades;

import com.fs.starfarer.api.Global;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import starlords.person.Lord;
import starlords.util.Utils;
import starlords.util.factionUtils.FactionTemplate;
import starlords.util.factionUtils.FactionTemplateController;
import starlords.util.weights.UpgradeWeights;

import java.util.ArrayList;
import java.util.HashMap;

public class UpgradeController {
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
    private static HashMap<String, ArrayList<String>> groups = new HashMap<>();
    @SneakyThrows
    public static void init(){
        /*so heres the plan on preperation for this nonesense.
        * first, use my knowlage gained in my very first starlord generator on how the hell to read CSV files. we can go about building an 'upgrade.csv' from there.
        * aftorwords, I can start to look into farther improvements.*/
        String path = "data/lords/upgrades.csv";
        JSONArray jsons = Global.getSettings().loadCSV(path,true);
        for (int a = 0; a < jsons.length(); a++){
            addUpgrade(jsons.getJSONObject(a));

        }




        /*String[] paths = {
                "starlords.util.lordUpgrades.UpgradeTempAttempt",
        };
        for (String a : paths){
            try {
                attemptToFindAFile(a);
            }catch (Exception e){
                Logger log = Global.getLogger(UpgradeController.class);
                log.info("  ERROR: failed to get path of: "+a);
            }
        }*/
    }
    private static void addUpgrade(JSONObject json) throws JSONException {
        String path = json.getString("script");
        UpgradeBase newItem = (UpgradeBase) Global.getSettings().getInstanceOfScript(path);
        newItem.init(json);
        String id = json.getString("id");
        upgrades.put(id,newItem);
        if (json.has("group") && !json.isNull("group") && !json.getString("group").isEmpty()){
            String group = json.getString("group");
            ArrayList<String> list = groups.getOrDefault(group,new ArrayList<>());
            list.add(id);
            groups.put(group,list);
        }

    }

    public UpgradeBase getUpgrade(Lord lord,UpgradeData data){
        /*I need to focus:
        * first, how should the upgrades be desided in relation to cost? should there be more then one cost modifier for item (like how there can be more then one AI modifier?)
        * ... no. why would there? like, I cant think of a reason.
        * second, should there be more than one modifier related to weight?
        * ... no..? only one AI modifer per item then? mmmmm
        * ok, so cost modifers, in what would would there need to be more then one? when more then one cost is imposed. some type of dull upgrade?
        * mmmmmm I cant think of anything.
        *
        * what would multible AI modifers do? like, number of ships left, and credits left, and what have you?
        * I can see a situation were you would want like, a weight based on missing ships, and another based on extra credits (for more costly upgrades), and one for number of fiefs, so on so forth.
        * no wait, I cant see a reason for that. like, for Smods there is one. were one has higher AI value when there is more higher level officers, and one for number of missing S-mods.
        * for AI cores is another. one for adding AI cores, one for ships missing AI cores, one for cost relative to wealth (AI coes are costly)
        * so in conclusion...
        * 1 number for cost.
        * multible numbers for AI*/
        ArrayList<UpgradeBase> options = new ArrayList<>();
        ArrayList<Integer> weights = new ArrayList<>();
        UpgradeWeights factionUpgrades = FactionTemplateController.getTemplate(lord.getFaction()).getUpgradeWeights();
        UpgradeWeights lordUpgrades = lord.getUpgradeWeights();
        for (String a : upgrades.keySet()){
            UpgradeBase upgrade = upgrades.get(a);
            if (!factionUpgrades.getEnabled().get(a) && !factionUpgrades.getEnabled().get(a)) continue;
            if (data.lastUpgrade.equals(a)) continue;
            if (!upgrade.canPreformUpgrade(lord)) continue;
            /*int cost = upgrade.getMinWealthForUpgrade(lord);
            for (String b : upgrade.getCostWeightsId()){
                cost += upgrade.getMinWealthForUpgrade(lord,b) * lordUpgrades.getCostWeights().get(a).getWeights().get(b) * factionUpgrades.getCostWeights().get(a).getWeights().get(b);
            }*/
            if (upgrade.getMinWealthForUpgrade(lord) > lord.getWealth() * factionUpgrades.getCostWeights().get(a)*lordUpgrades.getCostWeights().get(a)) continue;
            int weight = 0;
            for (String b : upgrade.getCostWeightsId()){
                weight += (int) (upgrade.getAIWeightOfType(lord,b) * lordUpgrades.getAIWeights().get(a).getWeights().get(b) * factionUpgrades.getAIWeights().get(a).getWeights().get(b));
            }
            if (weight <0 )continue;
            options.add(upgrade);
            weights.add(weight);
        }
        return Utils.weightedSample(options,weights,Utils.rand);
    }
    public void performUpgrades(Lord lord){
        UpgradeData data = new UpgradeData();
        UpgradeBase upgrade = getUpgrade(lord,data);
        if (upgrade == null) return;
        /*ok. so a few things: I want a new 'seed' function. this function will be saved for... each upgrade on each ship maybe?
        * then when a save load is preformed, the upgrades can be readded? so long as the upgrade uses the same seed, it should be fine right????? right??????
        * ok, so ok... instead lets save one seed per ship. most upgrade types DO NOT REQUIRE there own seed. right now, its just S-mods. but it could also include things like exsotic technologys requireing an seed. so, one seed per ship.
        * so, on each lord I require a 'seed map' of some type. just... save the seed on each ship. it should be fine right??? RIGHT?!?!?! can I even save a seed on a given ship????
        * wait... um....
        * ok so an issue: right now, this is set to only get the 'seed' of the fleet. I would need to go though for each ship. so the upgrade is preformed on each ship.
        * that should be simple enouth.*/
        //upgrade.preformUpgrade(lord,!!!!);
    }

    private static void attemptToFindAFile(String path){
        Logger log = Global.getLogger(UpgradeController.class);
        log.info("attempting to get a class from a path of: "+path);
        Object thing = Global.getSettings().getInstanceOfScript(path);
        log.info("  scaning gotten thing....");
        log.info("  "+thing.getClass());
        log.info("  "+thing.toString());
        UpgradeBase thing2 = (UpgradeBase) thing;
        log.info("  "+"gotting thing as UpgradeBase...");
        ((UpgradeBase) thing).attemptThing();
        log.info("  "+thing2.getClass());
        log.info("  "+thing2.toString());
    }
}
