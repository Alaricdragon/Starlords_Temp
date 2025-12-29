package starlords.generator.dataBuilders;

import com.fs.starfarer.api.util.Pair;
import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.generator.LordBaseDataBuilder;
import starlords.generator.LordGenerator;
import starlords.generator.support.AvailableShipData;
import starlords.generator.types.flagship.LordFlagshipPickerBase;
import starlords.person.Lord;
import starlords.util.ScriptedValues.ScriptedValueController;
import starlords.util.Utils;
import starlords.util.fleetCompasition.ShipCompositionData;
import starlords.util.memoryUtils.Compressed_outdated.MemCompressedPrimeSetterUtils;

import java.util.ArrayList;
import java.util.HashMap;

import static starlords.util.memoryUtils.Compressed_outdated.MemCompressedMasterList.KEY_LORD;

public class flagship implements LordBaseDataBuilder {
    @SneakyThrows
    @Override
    public boolean shouldGenerate(Lord lord, JSONObject json) {
        return (!json.has("fleetComposition") || !json.getJSONObject("fleetComposition").has("flagship")) || json.has("flagship");
    }

    @SneakyThrows
    @Override
    public void lordJSon(JSONObject json, Lord lord) {
        if (json.has("flagship")){
            //old data
            ShipCompositionData ship = new ShipCompositionData();
            ship.variant = json.getString("flagship");
            lord.setFlagship(ship);
            return;
        }
        Object script = Utils.isScriptOrObject(json.getJSONObject("fleetComposition"),"flagship",lord);
        if (script != null){
            ShipCompositionData ship = (ShipCompositionData) script;
            ship.init(lord,"FLAGSHIP");
            lord.setFlagship(ship);
            ship.getMemory().getDATA_HOLDER().setBoolean("isScript",true,1);
            return;
        }
        String value = new ScriptedValueController(json.getJSONObject("fleetComposition").getJSONObject("flagship").getString("variant")).getNextString().getValue(lord);
        ShipCompositionData ship = new ShipCompositionData();
        ship.variant = value;
        lord.setFlagship(ship);
    }

    @Override
    public void generate(Lord lord) {

        Pair<HashMap<String,Double>, AvailableShipData> data = (Pair<HashMap<String, Double>, AvailableShipData>) lord.getMemory().getDATA_HOLDER().getObject(shipCompositionCombat.memoryKey_finalShips);
        //so... how the hell do I find the flagship again?
        //
        ArrayList<String> ships = new ArrayList<>();
        if (LordGenerator.getOddsOfNoneSelectedFlagship() < Utils.rand.nextDouble()){
            ships.addAll(data.two.getOrganizedShips().keySet());
        }else{
            ships.addAll(data.one.keySet());
        }
        LordFlagshipPickerBase generator = LordGenerator.getRandomFlagshipGenerator();
        String value = generator.pickFlagship(ships);
        ShipCompositionData ship = new ShipCompositionData();
        ship.variant = value;
        lord.setFlagship(ship);
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

    @Override
    public boolean shouldRepair(Lord lord, JSONObject json) {
        return false;
    }
}
