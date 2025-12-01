package starlords.generator.dataBuilders;

import com.fs.starfarer.api.Global;
import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.generator.LordBaseDataBuilder;
import starlords.person.Lord;
import starlords.util.Utils;
import starlords.util.fleetCompasition.FleetCompositionData;
import starlords.util.fleetCompasition.ShipCompositionData;
import starlords.util.memoryUtils.Compressed.MemCompressedPrimeSetterUtils;

import static starlords.util.memoryUtils.Compressed.MemCompressedMasterList.FLEETCOMP_COMBAT;
import static starlords.util.memoryUtils.Compressed.MemCompressedMasterList.KEY_LORD;

public class availableShipsCombat implements LordBaseDataBuilder {
    @SneakyThrows
    @Override
    public boolean shouldGenerate(JSONObject json) {
        return (json.has("fleetComposition") && json.getJSONObject("fleetComposition").has("combatFleet"));
    }
    @SneakyThrows
    @Override
    public void lordJSon(JSONObject json, Lord lord) {
        lord.getMemory().getDATA_HOLDER().setBoolean("json_combatFleet",true,1);
        try {
            if (json.getString("json_combatFleet").charAt(0) == '~') {
                lord.getMemory().setCompressedOther(FLEETCOMP_COMBAT, Global.getSettings().getInstanceOfScript(json.getString("json_combatFleet")));
                return;
            }
        }catch (Exception e){
            Utils.log.info("failed to get a script for a starlord. error of: "+e);
        }
        FleetCompositionData data = new FleetCompositionData();
        //for (){
            //ShipCompositionData ship = new ShipCompositionData();
        //}
        //read json file here.
        //so: all this needs to do is read every ship in space, and NOTHING ELSE.
        //only ships and there ratios need to be read.
        lord.getMemory().setCompressedOther(FLEETCOMP_COMBAT, data);
    }
    @Override
    public void generate(Lord lord) {
        lord.getMemory().getDATA_HOLDER().setBoolean("json_combatFleet",false,1);
    }
    @Override
    public void prepareStorgeInMemCompressedOrganizer() {
        MemCompressedPrimeSetterUtils mem = MemCompressedPrimeSetterUtils.getHolder(KEY_LORD);
        mem.setObject(FLEETCOMP_COMBAT, linkedObject -> new FleetCompositionData());
    }

    @Override
    public void saveLord(Lord lord) {

    }

    @Override
    public void loadLord(Lord lord) {

    }

    @Override
    public boolean shouldRepair(Lord lord, JSONObject json) {
        //todo: I need a way for any 'json' lord, to check between the current fleetComp and its containers and this one.
        //      if any differences are detected, simply reset everything
        //      additional note: completely ignore scripts for this EQ. they want there scripts to regernerate a lot? overwrite this function.
        return false;
    }
}
