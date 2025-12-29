package starlords.generator.dataBuilders;

import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.generator.LordBaseDataBuilder;
import starlords.person.Lord;
import starlords.util.Utils;

public class LordName implements LordBaseDataBuilder {
    @SneakyThrows
    @Override
    public boolean shouldGenerate(Lord lord, JSONObject json) {
        if (lord.getFlagship().getMemory().getDATA_HOLDER().getBoolean("isScript")) return true;
        if (json.has("person")){
            Object script = Utils.isScriptOrObject(json, "person", lord);
            if (script != null){
                lord.getFlagship().getMemory().getDATA_HOLDER().setBoolean("isPersonScript",true,1);
                return true;
            }
            if (json.getJSONObject("person").has("name")) return false;
        }
        return true;
    }

    @Override
    public void lordJSon(JSONObject json, Lord lord) {
        /*todo:
            so a few things I need to do:
            1: I need to create a class for handling officer names, and officer portraits. in a generative context.
               in theory, lords do not require this data, but it would be easier to create now.
            2: in each fleet composition type, and the full fleet composition type, I need to create a way to create 'default officers'.
                this will allow me to have officers with limited amounts of names on the intier fleet without having to set it on every single ship.
                so that will be nice.




        */
        /*todo:
            so, there are a few possibility's here.
                1: a string value.
                2: a json object with a 'faction' String. if its this, I want to get a random portrait from this faction.
                3: a json object with a 'nameSet' jsonobject. in this case, I want to take a random first and last name from this name set.
            for lords:
                I want to set this data to the lord immanently.
            for officers:
                I want to remember the 'creation conditions' and then set the portrait every time the officer is created.
        */
    }

    @Override
    public void generate(Lord lord) {
        /*
            todo:
                so here there are 3 possibility's for generations:
                1: the 'person data' is a script. in this case, I need to initialize the person's script here.
                2: (for officers) the 'ship data' is script. in this case, I need to check and make sure this data has been successfully set.
                3: (for officers) the 'fleet composition' is a script. in this case, I need to do as 2 as well.


        */
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
