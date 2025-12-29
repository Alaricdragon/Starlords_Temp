package starlords.generator.dataBuilders;

import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.generator.LordBaseDataBuilder;
import starlords.person.Lord;
import starlords.util.Utils;
import starlords.util.fleetCompasition.FleetCompositionData;

public class lordCombatPersonality implements LordBaseDataBuilder {
    /*things to remember:
    * 1: I set a ship data holder boolean if a ship is a script. this is called 'isScript'
    *   -in this case, I need to return true on the generator, then check and make sure the relevant data is set.
    * 2: I set a ship data holder boolean if a person is a script. 'isPersonScript'
    *   -in this case, I need to return true on the generator, then check and make sure the relevant data is set.*/
    @SneakyThrows
    @Override
    public boolean shouldGenerate(Lord lord, JSONObject json) {
        if (json.has("battle_personality")) return false;
        /*
        if (json.has("fleetComposition") && json.getJSONObject("fleetComposition").has("flagship")) {
            Object script = Utils.isScriptOrObject(json.getJSONObject("fleetComposition"), "flagship", lord);
            if (script != null) return false;
            if (json.getJSONObject("fleetComposition").getJSONObject("fleetComposition").has("")) return false;
        }*/

        //todo: this is not suppose to be like this. if the lord person is a script should be set -before- this is set. it should be set in the LordName class.
        //note: I should proboly change that to 'sprite' and 'name'
        if (lord.getFlagship().getMemory().getDATA_HOLDER().getBoolean("isScript")) return true;
        if (json.has("person")){
            Object script = Utils.isScriptOrObject(json, "person", lord);
            if (script != null){
                lord.getFlagship().getMemory().getDATA_HOLDER().setBoolean("isPersonScript",true,1);
                return true;
            }
            if (json.getJSONObject("person").has("")) return false;
        }
        return true;
    }

    @Override
    public void lordJSon(JSONObject json, Lord lord) {
        Object script = Utils.isScriptOrObject(json,"person",lord);
        if (script!= null){
        }
    }

    @Override
    public void generate(Lord lord) {

    }

    @Override
    public void prepareStorgeInMemCompressedOrganizer() {
        //this data is set directly stored in the ship class.
    }

    @Override
    public void saveLord(Lord lord) {

    }

    @Override
    public void loadLord(Lord lord) {

    }

    @Override
    public boolean shouldRepair(Lord lord, JSONObject json) {
        return false;
    }
}
