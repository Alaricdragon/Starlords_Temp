package starlords.util.lordUpgrades;

import lombok.Getter;
import org.json.JSONObject;
import starlords.person.Lord;

import java.util.HashMap;

public class UpgradeSettings {
    /*
    * this holds the full list of upgrade settings. basicly, every upgrade settings in the game?
    * a masterlist. I think?
    * or is it?
    * aaaa
    * I dont knowwwwwww
    * ok... I might need to redo some things..
    * */
    @Getter
    private HashMap<String,UpgradeSetting> upgradeValues = new HashMap<>();
    public UpgradeSettings(JSONObject settings,JSONObject SettingValues){
        //this will work by doing the following: 1: get the full list of AI upgrades, 2: get the full list of random values for said upgrade settings.
        //this is to only be used as a randomizer
    }
    public UpgradeSettings(Lord lord){
        //need to work this out
        //this would work by 1: checking the starlords upgrade settings, then 2: applying 'random' settings otherwise.
    }
    public UpgradeSetting getUpgrade(String id){
        return upgradeValues.get(id);
    }
}
