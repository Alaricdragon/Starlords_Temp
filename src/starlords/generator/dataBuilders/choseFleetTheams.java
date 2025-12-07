package starlords.generator.dataBuilders;

import org.json.JSONObject;
import starlords.generator.LordBaseDataBuilder;
import starlords.generator.LordGenerator;
import starlords.generator.support.AvailableShipData;
import starlords.person.Lord;
import starlords.util.Utils;
import starlords.util.memoryUtils.DataHolder;

import java.util.ArrayList;

public class choseFleetTheams implements LordBaseDataBuilder {
    public static final String memoryKey_theams = "generator_fleetTheams_theams";
    public static final String memoryKey_sizeRatio = "generator_fleetTheams_sizeRatio";
    public static final String memoryKey_typeRatio = "generator_fleetTheams_typeRatio";
    @Override
    public boolean shouldGenerate(Lord lord, JSONObject json) {
        DataHolder a = lord.getMemory().getDATA_HOLDER();
        return !a.getBoolean("json_combatFleet") || !a.getBoolean(availableShipsCiv_Tug.hasLoadedJSonKey) || !a.getBoolean(availableShipsCiv_Cargo.hasLoadedJSonKey) || !a.getBoolean(availableShipsCiv_Fuel.hasLoadedJSonKey) || !a.getBoolean(availableShipsCiv_Personal.hasLoadedJSonKey);
    }

    @Override
    public void lordJSon(JSONObject json, Lord lord) {
        //does not load from json.
    }

    @Override
    public void generate(Lord lord) {
        /*this is done in a few steps.
        (done) step 1: get the fleet I am going to look at for theams. It is enter:
            a: I get the combat fleet
            b: I get the civ fleets -combined-.
        (done) step 2: I get 10 random theames from this order. (last one is always 'random')
        (done) step 3: I save said theams
        (HERE)step 4: I load the fleet compositions using said theams
        */
        lord.getMemory().getDATA_HOLDER().setObject(memoryKey_theams,getShipData(lord),1);
        lord.getMemory().getDATA_HOLDER().setObject(memoryKey_sizeRatio,getSizeRatio(lord),1);
        lord.getMemory().getDATA_HOLDER().setObject(memoryKey_typeRatio,getTypeRatio(lord),1);
    }
    public ArrayList<AvailableShipData> getShipData(Lord lord){
        AvailableShipData data = new AvailableShipData();
        if (!lord.getMemory().getDATA_HOLDER().getBoolean("json_combatFleet")){
            AvailableShipData a = (AvailableShipData) lord.getMemory().getDATA_HOLDER().getObject("generativeShips_Combat");
            AvailableShipData.mergeShips(data,a);
        }else{
            DataHolder m = lord.getMemory().getDATA_HOLDER();
            if(!m.getBoolean(availableShipsCiv_Tug.hasLoadedJSonKey)) AvailableShipData.mergeShips(data,(AvailableShipData) lord.getMemory().getDATA_HOLDER().getObject(availableShipsCiv_Tug.fleetMemoryKey));
            if(!m.getBoolean(availableShipsCiv_Cargo.hasLoadedJSonKey))AvailableShipData.mergeShips(data,(AvailableShipData) lord.getMemory().getDATA_HOLDER().getObject(availableShipsCiv_Cargo.fleetMemoryKey));
            if(!m.getBoolean(availableShipsCiv_Fuel.hasLoadedJSonKey))AvailableShipData.mergeShips(data,(AvailableShipData) lord.getMemory().getDATA_HOLDER().getObject(availableShipsCiv_Fuel.fleetMemoryKey));
            if(!m.getBoolean(availableShipsCiv_Personal.hasLoadedJSonKey))AvailableShipData.mergeShips(data,(AvailableShipData) lord.getMemory().getDATA_HOLDER().getObject(availableShipsCiv_Personal.fleetMemoryKey));
        }
        data = data.clone();//this copys the internal data, letting me preform modifications and get theams without fear.
        ArrayList<AvailableShipData> theams = new ArrayList<>();
        for (int a = 0; a < 10 && !data.getUnorganizedShips().isEmpty(); a++){
            AvailableShipData newData = LordGenerator.getRandomFleetGenerator().skimPossibleShips(data,true);
            theams.add(newData);
        }
        if (!data.getUnorganizedShips().isEmpty()) theams.add(LordGenerator.getFleetGeneratorBackup().skimPossibleShips(data,true));
        return theams;
    }
    public int[] getSizeRatio(Lord lord){

        int[] sizeratio = {
                LordGenerator.getSizeRatio()[0].getRandomInt(),
                LordGenerator.getSizeRatio()[1].getRandomInt(),
                LordGenerator.getSizeRatio()[2].getRandomInt(),
                LordGenerator.getSizeRatio()[3].getRandomInt()
        };
        if (sizeratio[0] == 0 && sizeratio[1] == 0 && sizeratio[2] == 0 && sizeratio[3] == 0){
            sizeratio[(int)(Utils.rand.nextInt(4))] = 1;
        }
        return sizeratio;
    }
    public int[] getTypeRatio(Lord lord){
        int[] typeratio = {
                (int)LordGenerator.getTypeRatio()[0].getRandom(),
                (int)LordGenerator.getTypeRatio()[1].getRandom(),
                (int)LordGenerator.getTypeRatio()[2].getRandom()
                /*typeRatio[3].getRandom(),
                typeRatio[4].getRandom(),
                typeRatio[5].getRandom(),
                typeRatio[6].getRandom(),
                typeRatio[7].getRandom(),
                typeRatio[8].getRandom(),
                typeRatio[9].getRandom()*/
        };
        //log.info("  sizeRatio: "+sizeratio[0]+", "+sizeratio[1]+", "+sizeratio[2]+", "+sizeratio[3]);
        //log.info("  typeRatio: "+typeratio[0]+", "+typeratio[1]+", "+typeratio[2]);
        if (typeratio[0] == 0 && typeratio[1] == 0 && typeratio[2] == 0){
            typeratio[(int)(Utils.rand.nextInt(3))] = 1;
        }
        return typeratio;
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
