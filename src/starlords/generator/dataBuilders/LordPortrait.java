package starlords.generator.dataBuilders;

import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.generator.LordBaseDataBuilder;
import starlords.person.Lord;
import starlords.util.ScriptedValues.SV_String;
import starlords.util.ScriptedValues.ScriptedValueController;

public class LordPortrait implements LordBaseDataBuilder {
    @SneakyThrows
    @Override
    public boolean shouldGenerate(Lord lord, JSONObject json) {
        if (json.has("person")){
            if (json.getJSONObject("person").has("portrait")) return false;
        }
        return true;
    }

    @SneakyThrows
    @Override
    public void lordJSon(JSONObject json, Lord lord) {
        SV_String port = new ScriptedValueController(json.getJSONObject("person").getString("portrait")).getNextString();
        lord.getLordAPI().setPortraitSprite(port.getValue(lord));
    }

    @Override
    public void generate(Lord lord) {
        //in all cases, this should never be required on a lord. as it is handled at a different position.
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
