package starlords.generator.dataBuilders;

import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.generator.LordBaseDataBuilder;
import starlords.person.Lord;
import starlords.util.ScriptedValues.ScriptedValueController;
import starlords.util.Utils;
import starlords.util.memoryUtils.Compressed.MemCompressedPrimeSetterUtils;

import static starlords.util.memoryUtils.Compressed.MemCompressedMasterList.KEY_LORD;

public class faction implements LordBaseDataBuilder {
    @Override
    public boolean shouldGenerate(JSONObject json) {
        return (json == null || !json.has("faction"));
    }

    @SneakyThrows
    @Override
    public void lordJSon(JSONObject json, Lord lord) {
        if (json.has("faction")) {
            //lord.getLordAPI().setFaction(json.getString("faction"));
            //please keep in mind, this is not changed on defection -yet-. it will need to be later though.
            String a = new ScriptedValueController(json.getString("faction")).getNextString().getValue(lord);
            lord.getMemory().setCompressedString("faction",a);
        }
    }

    @Override
    public void generate(Lord lord) {
        //todo: make it so the generator sets this before hand.
    }

    @Override
    public void prepareStorgeInMemCompressedOrganizer() {
        //this might be needed some day, but its not for now.
        MemCompressedPrimeSetterUtils mem = MemCompressedPrimeSetterUtils.getHolder(KEY_LORD);
        mem.setString("faction",linkedObject -> "");
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
