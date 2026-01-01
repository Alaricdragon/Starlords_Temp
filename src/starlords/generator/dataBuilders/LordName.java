package starlords.generator.dataBuilders;

import com.fs.starfarer.api.characters.FullName;
import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.generator.LordBaseDataBuilder;
import starlords.person.Lord;
import starlords.util.ScriptedValues.ScriptedValueController;
import starlords.util.Utils;

public class LordName implements LordBaseDataBuilder {
    @SneakyThrows
    @Override
    public boolean shouldGenerate(Lord lord, JSONObject json) {
        //if (lord.getFlagship().getMemory().getDATA_HOLDER().getBoolean("isScript")) return true;
        if (json.has("person")){
            //Object script = Utils.isScriptOrObject(json, "person", lord);
            //if (script != null){
                //lord.getFlagship().getMemory().getDATA_HOLDER().setBoolean("isPersonScript",true,1);
            //    return true;
            //}
            if (json.getJSONObject("person").has("name")) return false;
        }
        return true;
    }

    @SneakyThrows
    @Override
    public void lordJSon(JSONObject json, Lord lord) {
        /*before I confuse myself -again- by the fact that this seems to have no ability for things like random names:
          THAT IS WHAT SCRIPTED VALUES ARE FOR. if its really a issue CREATE A NEW SCRIPTED VALUE. its THAT SIMPLE!
        */
        String name = new ScriptedValueController(json.getJSONObject("person").getString("name")).getNextString().getValue(lord);
        String[] splitname = name.split(" ");
        String lastName = "";
        for (int i = 1; i < splitname.length; i++) {
            lastName += splitname[i] + " ";
        }
        FullName fullName = new FullName(splitname[0], lastName.trim(), lord.getLordAPI().getGender());
        lord.getLordAPI().setName(fullName);
    }

    @Override
    public void generate(Lord lord) {
        //generated data is gotten in an earlier stage.
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
