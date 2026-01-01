package starlords.generator.dataBuilders;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.characters.FullName;
import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.generator.LordBaseDataBuilder;
import starlords.person.Lord;
import starlords.util.ScriptedValues.ScriptedValueController;

public class LordGender implements LordBaseDataBuilder {
    @SneakyThrows
    @Override
    public boolean shouldGenerate(Lord lord, JSONObject json) {
        if (json.has("person")){
            //Object script = Utils.isScriptOrObject(json, "person", lord);
            //if (script != null){
            //lord.getFlagship().getMemory().getDATA_HOLDER().setBoolean("isPersonScript",true,1);
            //    return true;
            //}
            if (json.getJSONObject("person").has("isMale")) return false;
        }
        return true;
    }

    @Override
    @SneakyThrows
    public void lordJSon(JSONObject json, Lord lord) {
        boolean bol = new ScriptedValueController(json.getJSONObject("person").getString("isMale")).getNextBoolean().getValue(lord);
        FullName.Gender gen = FullName.Gender.FEMALE;
        if (bol) gen = FullName.Gender.MALE;
        lord.setLordAPI(Global.getSector().getFaction(lord.getCulture()).createRandomPerson(gen));
    }

    @Override
    public void generate(Lord lord) {
        lord.setLordAPI(Global.getSector().getFaction(lord.getCulture()).createRandomPerson());
    }

    @Override
    public void prepareStorgeInMemCompressedOrganizer() {
        //this is the initialization of the lord API. very important.
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
