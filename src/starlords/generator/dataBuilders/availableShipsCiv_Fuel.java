package starlords.generator.dataBuilders;

import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.generator.LordBaseDataBuilder;
import starlords.generator.support.AvailableShipData;
import starlords.person.Lord;
import starlords.util.Utils;
import starlords.util.fleetCompasition.FleetCompositionData;
import starlords.util.memoryUtils.Compressed.MemCompressedPrimeSetterUtils;

import static starlords.generator.dataBuilders.availableShipsCiv_Tug.loadJsonGeneric;
import static starlords.util.memoryUtils.Compressed.MemCompressedMasterList.*;
import static starlords.util.memoryUtils.Compressed.MemCompressedMasterList.FLEETCOMP_FUEL;
import static starlords.util.memoryUtils.Compressed.MemCompressedMasterList.FLEETCOMP_PERSONAL;
import static starlords.util.memoryUtils.Compressed.MemCompressedMasterList.FLEETCOMP_TUG;

public class availableShipsCiv_Fuel implements LordBaseDataBuilder {
    protected static final String jsonKey = "civFleet_Fuel";
    protected static final String fleetMemoryKey = FLEETCOMP_FUEL;
    protected static final String hasLoadedJSonKey = "json_civFleet_Fuel";
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
        AvailableShipData out = AvailableShipData.getAvailableShips(fac,AvailableShipData.HULLTYPE_TANKER);
        if (out.getUnorganizedShips().isEmpty()) {
            Utils.log.info("WARNING: was forced to use the final emergency fleet generator for a starlords fleet (fuel)");
            String finalBackup = "dram_Light";//the ultimate weapon of the final war
            out.addShip(finalBackup,1,AvailableShipData.HULLTYPE_TANKER);
        }
        return out;
    }

    @Override
    public void prepareStorgeInMemCompressedOrganizer() {
        MemCompressedPrimeSetterUtils mem = MemCompressedPrimeSetterUtils.getHolder(KEY_LORD);
        mem.setObject(FLEETCOMP_FUEL, linkedObject -> new FleetCompositionData());
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
