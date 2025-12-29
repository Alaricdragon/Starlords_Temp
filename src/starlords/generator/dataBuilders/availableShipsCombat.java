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
import starlords.util.memoryUtils.Compressed_outdated.MemCompressedPrimeSetterUtils;

import java.util.Iterator;

import static starlords.util.memoryUtils.Compressed_outdated.MemCompressedMasterList.FLEETCOMP_COMBAT;
import static starlords.util.memoryUtils.Compressed_outdated.MemCompressedMasterList.KEY_LORD;

public class availableShipsCombat implements LordBaseDataBuilder {
    @SneakyThrows
    @Override
    public boolean shouldGenerate(Lord lord, JSONObject json) {
        if (json.has("shipPref")) return false;
        return !json.has("fleetComposition") || !json.getJSONObject("fleetComposition").has("combatFleet");
    }

    @SneakyThrows
    @Override
    public void lordJSon(JSONObject json, Lord lord) {
        lord.getMemory().getDATA_HOLDER().setBoolean("json_combatFleet",true,1);
        if (json.has("shipPref")){
            loadJsonOutdated(json,lord);
            return;
        }
        json = json.getJSONObject("fleetComposition");
        Object script = Utils.isScriptOrObject(json,"json_combatFleet",lord);
        if (script!= null){
            FleetCompositionData data = (FleetCompositionData) script;
            lord.getFleetCompositionData().setCombat(data);
            return;
        }
        FleetCompositionData data = new FleetCompositionData();
        JSONArray array = json.getJSONArray("ships");
        for (int a = 0; a < array.length(); a++){
            script = Utils.isScriptOrObject(array,a,lord);
            if (script != null){
                ShipCompositionData ship = (ShipCompositionData) script;
                ship.init(lord,FLEETCOMP_COMBAT);
                ship.getMemory().getDATA_HOLDER().setBoolean("isScript",true,1);
                continue;
            }
            JSONObject b = array.getJSONObject(a);
            ShipCompositionData.addShipToFleetCompFromJson(data,b,lord);
        }
        //so: all this needs to do is read every ship in space, and NOTHING ELSE.
        //only ships and there ratios need to be read.
        lord.getFleetCompositionData().setCombat(data);
    }
    @Override
    public void generate(Lord lord) {
        lord.getMemory().getDATA_HOLDER().setObject("generativeShips_Combat",getPossableShips(lord),1);
    }
    public AvailableShipData getPossableShips(Lord lord){
        String fac = lord.getCulture();
        AvailableShipData out = AvailableShipData.getAvailableShips(fac,AvailableShipData.HULLTYPE_CARRIER,AvailableShipData.HULLTYPE_WARSHIP,AvailableShipData.HULLTYPE_PHASE);
        if (out.getUnorganizedShips().isEmpty()) {
            Utils.log.info("WARNING: was forced to use the final emergency fleet generator for a starlords fleet");
            String finalBackup = "kite_pirates_Raider";//the ultimate weapon of the final war
            out.addShip(finalBackup,1,AvailableShipData.HULLTYPE_WARSHIP);
        }
        return out;
    }
    @Override
    public void prepareStorgeInMemCompressedOrganizer() {
        //this data is set directly stored in the Lord class.
    }

    @Override
    public void saveLord(Lord lord) {

    }

    @Override
    public void loadLord(Lord lord) {

    }

    @Deprecated
    @SneakyThrows
    public void loadJsonOutdated(JSONObject json, Lord lord){
        FleetCompositionData data = new FleetCompositionData();
        json = json.getJSONObject("shipPref");
        for (Iterator it = json.keys(); it.hasNext(); ) {
            String key = (String) it.next();
            ShipCompositionData ship = new ShipCompositionData();
            ship.init(data,key,json.getDouble(key));
        }
        lord.getFleetCompositionData().setCombat(data);
    }

    @SneakyThrows
    @Override
    public boolean shouldRepair(Lord lord, JSONObject json) {
        //holy fuck this is confusing.
        //first: how do I even identify if this requires repair or not?
        //my mind says: look for whats diffrent between this

        //so, as mush as I would love to have the jsons beable to 'reset' a giving lords 'ships'....
        //this is going to have to wait for now. or is it? it is. I just fucking cant.
        //the structure is here now, but its 2 complecated for me to do all at once. lets do this one step at a time.
        //I can return here afterwords.
        return false;
    }
}
