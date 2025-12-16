package starlords.generator.dataBuilders;

import com.fs.starfarer.api.util.Pair;
import org.json.JSONObject;
import starlords.generator.LordBaseDataBuilder;
import starlords.generator.LordGenerator;
import starlords.generator.support.AvailableShipData;
import starlords.generator.types.fleet.LordFleetGeneratorBase;
import starlords.person.Lord;
import starlords.util.Utils;
import starlords.util.fleetCompasition.FleetCompositionData;
import starlords.util.fleetCompasition.ShipCompositionData;

import java.util.ArrayList;
import java.util.HashMap;

import static starlords.generator.dataBuilders.choseFleetTheams.*;
import static starlords.util.memoryUtils.Compressed_outdated.MemCompressedMasterList.FLEETCOMP_COMBAT;

public class shipCompositionCombat implements LordBaseDataBuilder {
    public static final String memoryKey_finalShips = "generator_shipCompCombat";

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
        ArrayList<Pair<LordFleetGeneratorBase,Object>> fleets = (ArrayList<Pair<LordFleetGeneratorBase,Object>>) lord.getMemory().getDATA_HOLDER().getObject(memoryKey_theams);
        int[] sizes = (int[]) lord.getMemory().getDATA_HOLDER().getObject(memoryKey_sizeRatio);
        int[] types = (int[]) lord.getMemory().getDATA_HOLDER().getObject(memoryKey_typeRatio);
        AvailableShipData ships = (AvailableShipData) lord.getMemory().getDATA_HOLDER().getObject("generativeShips_Combat");
        int maxShip = (int) LordGenerator.getMaxShipRatio().getRandom();
        int minShip = (int)LordGenerator.getMinShipRatio().getRandom();
        int targetShip = (int) ((Utils.rand.nextDouble() * (maxShip-minShip)) + minShip);

        //skim ships from all preset theams...
        AvailableShipData acceptedShips = new AvailableShipData();
        AvailableShipData shipsTemp = ships.clone();
        for (Pair<LordFleetGeneratorBase,Object> a : fleets){
            AvailableShipData temp = a.one.skimPossibleShips(shipsTemp,a.two,true);
            AvailableShipData.mergeShips(acceptedShips,temp);
            if (acceptedShips.getUnorganizedShips().size() >= targetShip) break;
        }
        HashMap<String,Double> shipsToUse = LordGenerator.assingFleetSpawnWeights(acceptedShips,sizes,types);

        //move new ships into FLEETCOMP_COMBAT
        FleetCompositionData data = new FleetCompositionData();
        for (String a : shipsToUse.keySet()){
            ShipCompositionData.addShipToFleetCompFromGenerator(data,a,shipsToUse.get(a));
        }
        lord.getMemory().setCompressed_Object(FLEETCOMP_COMBAT, data);

        //save both used ships, and all possable ships. this is for flagship generation
        Pair<HashMap<String,Double>,AvailableShipData> out = new Pair<>(shipsToUse,ships);
        lord.getMemory().getDATA_HOLDER().setObject(memoryKey_finalShips,out,1);
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
