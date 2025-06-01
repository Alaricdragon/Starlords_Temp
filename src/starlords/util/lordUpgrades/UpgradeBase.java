package starlords.util.lordUpgrades;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import starlords.person.Lord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class UpgradeBase {
    @Getter
    private boolean defaultOn = true;
    @Getter
    private double weight = 0;
    @Getter
    private ArrayList<String> aiWeightsID = new ArrayList<>();
    @Getter
    private ArrayList<String> costWeightsId = new ArrayList<>();
    @SneakyThrows
    public void init(JSONObject json){
        if (json.has("defaultEnabled")){
            defaultOn = json.getBoolean("defaultEnabled");
        }
        if (json.has("weight")) weight = json.getDouble("weight");
    }
    public boolean canPreformUpgrade(Lord lord){
        return false;
    }
    public void preformUpgrade(Lord lord, Random seed){
        //todo: make this so it gets each ship, apples a 'seed' that is saved to the ship (or is gotten from saved data for this ship)
        //note: I also need to make a function that readds upgrades when the game is reloaded. so unsaved upgrades are reapplyed that way.
    }
    public void preformUpgradeOnShip(Lord lord, FleetMemberAPI ship, Random seed){

    }
    public float getCostForUpgrade(Lord lord){
        return 0;
    }
    public int getMinWealthForUpgrade(Lord lord){
        return 0;
    }
    public double getAIWeightOfType(Lord lord,String type){
        return 0;
    }

    public void attemptThing(){
        Logger log = Global.getLogger(UpgradeBase.class);
        log.info("  WE GOT IT BOYSSSS AHAHAH");
    }
}
