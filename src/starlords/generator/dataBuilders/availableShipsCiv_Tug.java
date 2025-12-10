package starlords.generator.dataBuilders;

import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;
import starlords.generator.LordBaseDataBuilder;
import starlords.generator.support.AvailableShipData;
import starlords.person.Lord;
import starlords.util.Utils;
import starlords.util.fleetCompasition.FleetCompositionData;
import starlords.util.fleetCompasition.ShipCompositionData;
import starlords.util.memoryUtils.Compressed.MemCompressedPrimeSetterUtils;

import static starlords.util.memoryUtils.Compressed.MemCompressedMasterList.*;
import static starlords.util.memoryUtils.Compressed.MemCompressedMasterList.FLEETCOMP_TUG;

public class availableShipsCiv_Tug implements LordBaseDataBuilder {
    protected static final String jsonKey = "civFleet_Tug";
    protected static final String fleetMemoryKey = FLEETCOMP_TUG;
    protected static final String hasLoadedJSonKey = "json_civTug_Fleet";
    @SneakyThrows
    @Override
    public boolean shouldGenerate(Lord lord, JSONObject json) {
        return !json.has("fleetComposition") || !json.getJSONObject("fleetComposition").has(jsonKey);
    }

    @Override
    public void prepareStorgeInMemCompressedOrganizer() {

        MemCompressedPrimeSetterUtils mem = MemCompressedPrimeSetterUtils.getHolder(KEY_LORD);
        mem.setObject(FLEETCOMP_TUG, linkedObject -> new FleetCompositionData());
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

    @SneakyThrows
    @Override
    public void lordJSon(JSONObject json, Lord lord) {
        loadJsonGeneric(json,lord,hasLoadedJSonKey,fleetMemoryKey,jsonKey);
    }
    @SneakyThrows
    protected static void loadJsonGeneric(JSONObject json, Lord lord,String hasRememberedKey,String fleetKey,String jsonKey){
        lord.getMemory().getDATA_HOLDER().setBoolean(hasRememberedKey,true,1);
        json = json.getJSONObject("fleetComposition");
        Object script = Utils.isScriptOrObject(json,jsonKey,lord);
        if (script!= null){
            FleetCompositionData data = (FleetCompositionData) script;
            lord.getMemory().setCompressed_Object(fleetKey, data);
            return;
        }
        FleetCompositionData data = new FleetCompositionData();
        JSONArray array = json.getJSONArray("ships");
        for (int a = 0; a < array.length(); a++){
            script = Utils.isScriptOrObject(array,a,lord);
            if (script != null){
                ShipCompositionData ship = (ShipCompositionData) script;
                ship.init(lord,fleetKey);
                continue;
            }
            JSONObject b = array.getJSONObject(a);
            ShipCompositionData.addShipToFleetCompFromJson(data,b,lord);
        }
        lord.getMemory().setCompressed_Object(fleetKey, data);

    }
    @Override
    public void generate(Lord lord) {
        lord.getMemory().getDATA_HOLDER().setObject(fleetMemoryKey, getPossibleShips(lord),1);
    }
    public AvailableShipData getPossibleShips(Lord lord){
        String fac = lord.getMemory().getCompressed_String("culture");
        AvailableShipData out = AvailableShipData.getAvailableShips(fac,AvailableShipData.HULLTYPE_TUG);
        if (out.getUnorganizedShips().isEmpty()) {
            Utils.log.info("WARNING: was forced to use the final emergency fleet generator for a starlords fleet (tug)");
            String finalBackup = "ox_Standard";//the ultimate weapon of the final war
            out.addShip(finalBackup,1,AvailableShipData.HULLTYPE_TUG);
        }
        return out;
    }
}
