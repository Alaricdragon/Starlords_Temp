package starlords.generator.dataBuilders;

import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.generator.LordBaseDataBuilder;
import starlords.person.Lord;
import starlords.util.fleetCompasition.FleetCompositionData;
import starlords.util.memoryUtils.Compressed.MemCompressedPrimeSetterUtils;

import static starlords.util.memoryUtils.Compressed.MemCompressedMasterList.*;

public class availableShipsCiv_Cargo implements LordBaseDataBuilder {
    @SneakyThrows
    @Override
    public boolean shouldGenerate(JSONObject json) {
        return (json.has("fleetComposition") && json.getJSONObject("fleetComposition").has("json_civFleet_Cargo"));
    }

    @Override
    public void lordJSon(JSONObject json, Lord lord) {

    }

    @Override
    public void generate(Lord lord) {

    }

    @Override
    public void prepareStorgeInMemCompressedOrganizer() {

        MemCompressedPrimeSetterUtils mem = MemCompressedPrimeSetterUtils.getHolder(KEY_LORD);
        mem.setObject(FLEETCOMP_CARGO, linkedObject -> new FleetCompositionData());
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
