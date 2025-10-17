package starlords.util.lordUpgrades;

import com.fs.starfarer.api.Global;
import lombok.Getter;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import starlords.person.Lord;
import starlords.util.memoryUtils.Compressed.MemCompressedMasterList;
import starlords.util.memoryUtils.Compressed.MemCompressedOrganizer;

import java.util.ArrayList;
import java.util.HashMap;

import static starlords.util.Constants.COMPRESSED_ORGANIZER_LORD_KEY;

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

    @SneakyThrows
    public static void init(){
        /*so heres the plan on preperation for this nonesense.
        * first, use my knowlage gained in my very first starlord generator on how the hell to read CSV files. we can go about building an 'upgrade.csv' from there.
        * aftorwords, I can start to look into farther improvements.*/
        String path = "data/lords/upgrades.csv";
        JSONArray jsons = Global.getSettings().loadCSV(path,true);
        MemCompressedOrganizer<?, ?> organizer = MemCompressedMasterList.getMemory().get(COMPRESSED_ORGANIZER_LORD_KEY);
        for (int a = 0; a < jsons.length(); a++){
            addUpgrade(jsons.getJSONObject(a),organizer);
        }

    }
    private static void addUpgrade(JSONObject json, MemCompressedOrganizer<?, ?> organizer) throws JSONException {
        String path = json.getString("script");
        UpgradeBase newItem = (UpgradeBase) Global.getSettings().getInstanceOfScript(path);
        String id = json.getString("id");
        defaultOn.put(id,json.getBoolean("defaultEnabled"));
        upgrades.put(id,newItem);

        /*the following needs to happen here:
        * 1) I need to add all the 'upgrade data' the the organizer. (this just means I need to manage the upgrade weights to the organizer.)
        * remember that the key to the upgrade data equals:
        * a) STARLORD_COMPRESSED_ORGANIZER_UPGRADE_KEY + "NameOfUpgrade_" + STARLORD_COMPRESSED_ORGANIZER_UPGRADE_WEIGHT_KEY+"NameOfWeight"
        * b) STARLORD_COMPRESSED_ORGANIZER_UPGRADE_KEY + "NameOfUpgrade_" + STARLORD_COMPRESSED_ORGANIZER_UPGRADE_COST_KEY+"NameOfWeight"
        *
        * */
        prepareUpgradeDefaults_Cost(id,json,organizer);
        prepareUpgradeDefaults_Weight(id,json,organizer);
    }
    private static void prepareUpgradeDefaults_Cost(String name,JSONObject json,MemCompressedOrganizer<?, ?> organizer){
        //STARLORD_COMPRESSED_ORGANIZER_UPGRADE_KEY + name + STARLORD_COMPRESSED_ORGANIZER_UPGRADE_COST_KEY+"NameOfWeight"
    }
    private static void prepareUpgradeDefaults_Weight(String name,JSONObject json,MemCompressedOrganizer<?, ?> organizer){
       //STARLORD_COMPRESSED_ORGANIZER_UPGRADE_KEY + name + STARLORD_COMPRESSED_ORGANIZER_UPGRADE_WEIGHT_KEY+"NameOfWeight"
    }
    public UpgradeBase getUpgrade(Lord lord,UpgradeData data){
        return null;
    }





    public void performUpgrades(Lord lord){
        UpgradeData data = new UpgradeData();
        UpgradeBase upgrade = getUpgrade(lord,data);
        if (upgrade == null) return;
    }
}
