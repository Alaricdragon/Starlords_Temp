package starlords.generator.dataBuilders;

import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.generator.LordBaseDataBuilder;
import starlords.person.Lord;
import starlords.util.ScriptedValues.ScriptedValueController;

public class FleetName implements LordBaseDataBuilder {
    @Override
    public boolean shouldGenerate(Lord lord, JSONObject json) {
        if (json.has("fleetName")) return false;
        return true;
    }

    @SneakyThrows
    @Override
    public void lordJSon(JSONObject json, Lord lord) {
        lord.setFleetName(new ScriptedValueController(json.getString("fleetName")).getNextString().getValue(lord));
    }

    @Override
    public void generate(Lord lord) {
        /*todo:
            so what have I dont:
            I have created the json file for the fleet names. I still need to add more fleet names as required.
            so what is left to do:
                1: fill in the json files fleet names
                2: create the holder for this csv compiled data, as well as the thing that reads said data (please note: I built a csv file reader. please use that to prevent doops)
                3: make it so the generator creates a starlord.

        */

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
