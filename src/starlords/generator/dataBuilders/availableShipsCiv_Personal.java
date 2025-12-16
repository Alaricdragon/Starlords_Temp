package starlords.generator.dataBuilders;

import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.generator.LordBaseDataBuilder;
import starlords.generator.support.AvailableShipData;
import starlords.person.Lord;
import starlords.util.Utils;
import starlords.util.fleetCompasition.FleetCompositionData;
import starlords.util.memoryUtils.Compressed_outdated.MemCompressedPrimeSetterUtils;

import static starlords.generator.dataBuilders.availableShipsCiv_Tug.loadJsonGeneric;
import static starlords.util.memoryUtils.Compressed_outdated.MemCompressedMasterList.*;
import static starlords.util.memoryUtils.Compressed_outdated.MemCompressedMasterList.FLEETCOMP_PERSONAL;

public class availableShipsCiv_Personal implements LordBaseDataBuilder {
    protected static final String jsonKey = "civFleet_Personal";
    protected static final String fleetMemoryKey = FLEETCOMP_PERSONAL;
    protected static final String hasLoadedJSonKey = "json_civFleet_Personal";
    @SneakyThrows
    @Override
    public boolean shouldGenerate(Lord lord, JSONObject json) {
        return !json.has("fleetComposition") || !json.getJSONObject("fleetComposition").has(jsonKey);
    }

    @Override
    public void lordJSon(JSONObject json, Lord lord) {
        loadJsonGeneric(json,lord,hasLoadedJSonKey,fleetMemoryKey,jsonKey);
    }

    @Override
    public void generate(Lord lord) {
        lord.getMemory().getDATA_HOLDER().setObject(fleetMemoryKey, getPossibleShips(lord),1);
    }
    public AvailableShipData getPossibleShips(Lord lord){
        String fac = lord.getMemory().getCompressed_String("culture");
        AvailableShipData out = AvailableShipData.getAvailableShips(fac,AvailableShipData.HULLTYPE_PERSONNEL);
        if (out.getUnorganizedShips().isEmpty()) {
            Utils.log.info("WARNING: was forced to use the final emergency fleet generator for a starlords fleet (personal)");
            String finalBackup = "nebula_Standard";//the ultimate weapon of the final war
            out.addShip(finalBackup,1,AvailableShipData.HULLTYPE_PERSONNEL);
        }
        return out;
    }

    @Override
    public void prepareStorgeInMemCompressedOrganizer() {
        MemCompressedPrimeSetterUtils mem = MemCompressedPrimeSetterUtils.getHolder(KEY_LORD);
        mem.setObject(FLEETCOMP_PERSONAL, linkedObject -> new FleetCompositionData());
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
