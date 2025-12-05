package starlords.generator.dataBuilders;

import org.json.JSONObject;
import starlords.generator.LordBaseDataBuilder;
import starlords.person.Lord;

public class shipCompositionCombat implements LordBaseDataBuilder {
    @Override
    public boolean shouldGenerate(JSONObject json) {
        return true;
    }

    @Override
    public void lordJSon(JSONObject json, Lord lord) {
        //cant load from json
    }

    @Override
    public void generate(Lord lord) {
        //only generate if availableShipsCombat lets me. remember to save data.
        /*todo:
        *  */

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
