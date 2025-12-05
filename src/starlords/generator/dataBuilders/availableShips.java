package starlords.generator.dataBuilders;

import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.generator.LordBaseDataBuilder;
import starlords.person.Lord;
import starlords.util.fleetCompasition.FleetCompositionData;
import starlords.util.memoryUtils.Compressed.MemCompressedPrimeSetterUtils;

import static starlords.util.memoryUtils.Compressed.MemCompressedMasterList.*;

@Deprecated
public class availableShips implements LordBaseDataBuilder {
    @Override
    public boolean shouldGenerate(JSONObject json) {
        return false;
    }

    @SneakyThrows
    @Override
    public void lordJSon(JSONObject json, Lord lord) {
        //todo: add in the backup system for lords that use the old hashmap system, letting said things be calculated.
        //tells the game what fleets I can load.
        boolean hasCom = json.has("combatFleet");
        boolean hasTug = json.has("civFleet_Tug");
        boolean hasFuel = json.has("civFleet_Fuel");
        boolean hasCargo = json.has("civFleet_Cargo");
        boolean hasPer = json.has("civFleet_Personal");
        lord.getMemory().getDATA_HOLDER().setBoolean("json_combatFleet",hasCom,1);
        lord.getMemory().getDATA_HOLDER().setBoolean("json_civFleet_Tug",hasTug,1);
        lord.getMemory().getDATA_HOLDER().setBoolean("json_civFleet_Fuel",hasFuel,1);
        lord.getMemory().getDATA_HOLDER().setBoolean("json_civFleet_Cargo",hasCargo,1);
        lord.getMemory().getDATA_HOLDER().setBoolean("json_civFleet_Personal",hasPer,1);
        if (!hasCom || !hasTug || !hasFuel || !hasCargo || !hasPer){
            //lord.getMemory().getDATA_HOLDER().setObject("availableShips",getPossibleShips(lord),1);
        }

        if (hasCom) loadCombat(json.getJSONObject("combatFleet"), lord);
        else randomCombat(lord);
        if (hasTug) loadTug(json.getJSONObject("civFleet_Tug"), lord);
        else randomTug(lord);
        if (hasFuel) loadFuel(json.getJSONObject("civFleet_Fuel"),lord);
        else randomFuel(lord);
        if (hasCargo) loadCargo(json.getJSONObject("civFleet_Cargo"), lord);
        else randomCargo(lord);
        if (hasPer) loadPersonal(json.getJSONObject("civFleet_Personal"), lord);
        else randomPersonal(lord);
        /*
        * todo:
        *       1) create an 'available ships' class, were I take the lords ethnisity and compute ships from that.
        *           -should have a divided output into multiple ships
        *           -should have a function in this class for each ship type from available ships. so I can easly overwrite it for custom lords.
        *           -should have a function in this class for getting all available ship blueprints. so I can easly overwrite it for custom lords.
        *       2) this class, to continue any farther, I need to have completed my ability to read the other parts of the lord. AKA the lords starting faction.
        *       culture
        * */
    }
    protected void loadCombat(JSONObject json, Lord lord){

    }
    protected void loadTug(JSONObject json, Lord lord){

    }
    protected void loadCargo(JSONObject json, Lord lord){

    }
    protected void loadPersonal(JSONObject json, Lord lord){

    }
    protected void loadFuel(JSONObject json, Lord lord){

    }
/*
    protected AvailableShipData_OUTDATED getPossibleShips(Lord lord){

    }*/

    protected void randomCombat(Lord lord){}
    protected void randomTug(Lord lord){}
    protected void randomCargo(Lord lord){}
    protected void randomPersonal(Lord lord){}
    protected void randomFuel(Lord lord){}
    @SneakyThrows
    protected void setShipStructureFromJSon(FleetCompositionData data, JSONObject json){
        //data;
        json.get("ships");
        /*todo:
        *       so apparently, I have set this up to have a link to a class that will set what ships I use in a given fleet.
        *       in effect, that measn that this part of the data is pushed off onto that class, provided it exists.
        *       so heres what I need to do:
        *           1) change the way the Fleet Comp works in the json, so I can ether have the full fleet comp, OR a "~path/to/fleet/builder" to determine what ships to build.
        *           2) create a 'fleetCreationClass'. this class will handle building the 'FleetCompositionData'.
        *       -
        *       issues:
        *           1) this makes no sense, from a desing prespective. everything here can already be overriden, in the lordGenerator.csv file.
        *              -having a second area that can override this makes no sense.
        *
        *
        * */
    }

    @Override
    public void generate(Lord lord) {

    }

    @Override
    public void prepareStorgeInMemCompressedOrganizer() {
        //this should save the fleet composition data.
        //now the lord has a stored fleetCompositionData for FLEET_COMBAT.
        MemCompressedPrimeSetterUtils mem = MemCompressedPrimeSetterUtils.getHolder(KEY_LORD);
        mem.setObject(FLEETCOMP_COMBAT, linkedObject -> new FleetCompositionData());
        mem.setObject(FLEETCOMP_CARGO, linkedObject -> new FleetCompositionData());
        mem.setObject(FLEETCOMP_FUEL, linkedObject -> new FleetCompositionData());
        mem.setObject(FLEETCOMP_PERSONAL, linkedObject -> new FleetCompositionData());
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
        return false;//if I need to repair this, everything has gone wrong.
    }
}
