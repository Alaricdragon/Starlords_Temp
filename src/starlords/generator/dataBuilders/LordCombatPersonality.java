package starlords.generator.dataBuilders;

import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.generator.LordBaseDataBuilder;
import starlords.person.Lord;
import starlords.util.Utils;

import static starlords.generator.LordGenerator.*;

public class LordCombatPersonality implements LordBaseDataBuilder {
    /*things to remember:
    * 1: I set a ship data holder boolean if a ship is a script. this is called 'isScript'
    *   -in this case, I need to return true on the generator, then check and make sure the relevant data is set.
    * 2: I set a ship data holder boolean if a person is a script. 'isPersonScript'
    *   -in this case, I need to return true on the generator, then check and make sure the relevant data is set.*/
    @SneakyThrows
    @Override
    public boolean shouldGenerate(Lord lord, JSONObject json) {
        if (json.has("battle_personality")) return false;
        //todo: this is not suppose to be like this. if the lord person is a script should be set -before- this is set. it should be set in the LordName class.
        if (json.has("person")){
            if (json.getJSONObject("person").has("battle_personality")) return false;
        }
        return true;
    }

    @SneakyThrows
    @Override
    public void lordJSon(JSONObject json, Lord lord) {
        if (json.has("battle_personality")){
            lord.getLordAPI().setPersonality(json.getString("battle_personality").toLowerCase());
            return;
        }
        lord.getLordAPI().setPersonality(json.getJSONObject("person").getString("battle_personality").toLowerCase());
    }

    @Override
    public void generate(Lord lord) {
        lord.getLordAPI().setPersonality(battlePersonalities[getValueFromWeight(battlePersonalityRatio)]);
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
