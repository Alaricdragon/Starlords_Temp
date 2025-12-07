package starlords.generator.dataBuilders;

import org.json.JSONObject;
import starlords.generator.LordBaseDataBuilder;
import starlords.generator.support.AvailableShipData;
import starlords.person.Lord;

public class shipCompositionCombat implements LordBaseDataBuilder {
    @Override
    public boolean shouldGenerate(Lord lord, JSONObject json) {
        return !lord.getMemory().getDATA_HOLDER().getBoolean("json_combatFleet");
    }

    @Override
    public void lordJSon(JSONObject json, Lord lord) {
        //this does nothing in cases were generation might be required.
    }

    @Override
    public void generate(Lord lord) {
        //only generate if availableShipsCombat lets me. remember to save data.
        /*todo:
        *   ok, so here is my damamge:
        *   1) I need to create a sireus of random theams from the combined total of all ships. maybe 10 of them, at max
        *   2) I need to deside on the desired fleet comp (size and type) of all ships.
        *   3) I need to process this data into each fleet type (com,fuel,cargo,tug,personal) individually.
        *   -) this function is for processing the combat ships.
        *
        * */
        AvailableShipData data = (AvailableShipData) lord.getMemory().getDATA_HOLDER().getObject("generativeShips_Combat");

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
