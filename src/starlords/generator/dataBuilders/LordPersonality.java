package starlords.generator.dataBuilders;

import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.generator.LordBaseDataBuilder;
import starlords.generator.LordGenerator;
import starlords.person.Lord;
import starlords.util.ScriptedValues.ScriptedValueController;
import starlords.util.memoryUtils.Compressed_outdated.MemCompressedPrimeSetterUtils;

import static starlords.util.memoryUtils.Compressed_outdated.MemCompressedMasterList.KEY_LORD;

public class LordPersonality implements LordBaseDataBuilder {
    /*todo: when I final change the dialog, remove the 'LordPersonality' class in its current form. it should not be.

    */
    @Override
    public boolean shouldGenerate(Lord lord, JSONObject json) {
        return !json.has("personality");
    }

    @SneakyThrows
    @Override
    public void lordJSon(JSONObject json, Lord lord) {
        String a = new ScriptedValueController(json.getString("personality")).getNextString().getValue(lord);
        starlords.person.LordPersonality personality = starlords.person.LordPersonality.valueOf(a.toUpperCase());
        lord.setPersonality(personality);
    }

    @Override
    public void generate(Lord lord) {
        String a = LordGenerator.getPersonalities()[LordGenerator.getValueFromWeight(LordGenerator.getPersonalityRatio())];
        starlords.person.LordPersonality personality = starlords.person.LordPersonality.valueOf(a.toUpperCase());
        lord.setPersonality(personality);
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
