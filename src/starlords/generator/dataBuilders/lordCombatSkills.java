package starlords.generator.dataBuilders;

import org.json.JSONObject;
import starlords.generator.LordBaseDataBuilder;
import starlords.person.Lord;

public class lordCombatSkills implements LordBaseDataBuilder {
    /*things to remember:
     * 1: I set a ship data holder boolean if a ship is a script. this is called 'isScript'
     *   -in this case, I need to return true on the generator, then check and make sure the relevant data is set.
     * 2: I set a ship data holder boolean if a person is a script. 'isPersonScript'
     *   -in this case, I need to return true on the generator, then check and make sure the relevant data is set.*/
    @Override
    public boolean shouldGenerate(JSONObject json) {
        return false;
    }

    @Override
    public void lordJSon(JSONObject json, Lord lord) {

    }

    @Override
    public void generate(Lord lord) {

    }

    @Override
    public void prepareStorgeInMemCompressedOrganizer() {

    }

    @Override
    public void saveLord(Lord lord) {

    }

    @Override
    public void loadLord(Lord lord) {

    }
}
