package starlords.generator.dataBuilders;

import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.generator.LordBaseDataBuilder;
import starlords.person.Lord;
import starlords.util.ScriptedValues.ScriptedValueController;
import starlords.util.randomStrings.FleetNameController;

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
        lord.setFleetName(FleetNameController.generateFleetName(lord));
        FleetNameController.confirmName();
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
