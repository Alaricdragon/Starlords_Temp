package starlords.generator.dataBuilders;

import org.json.JSONObject;
import starlords.generator.LordBaseDataBuilder;
import starlords.person.Lord;

public class LordCombatSkills implements LordBaseDataBuilder {
    /*things to remember:
     * 1: I set a ship data holder boolean if a ship is a script. this is called 'isScript'
     *   -in this case, I need to return true on the generator, then check and make sure the relevant data is set.
     * 2: I set a ship data holder boolean if a person is a script. 'isPersonScript'
     *   -in this case, I need to return true on the generator, then check and make sure the relevant data is set.*/

    /*todo:
        1: (done) create a csv file holding all the different combat skills, and there add conditions.
        2: create something that reads the CSV file. (and stores relevant data)
        3: make the conditions that allow for determining if a giving officer should have a giving skill. (this will be hard)
            -something to remember: SV_Values exist. remember that you can modify weight with this as well.
        4:

   */
    @Override
    public boolean shouldGenerate(Lord lord, JSONObject json) {
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

    @Override
    public boolean shouldRepair(Lord lord, JSONObject json) {
        return false;
    }
}
