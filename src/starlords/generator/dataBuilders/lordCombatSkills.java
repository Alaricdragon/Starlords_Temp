package starlords.generator.dataBuilders;

import org.json.JSONObject;
import starlords.generator.LordBaseDataBuilder;
import starlords.person.Lord;

public class lordCombatSkills implements LordBaseDataBuilder {
    @Override
    public boolean shouldGenerate(JSONObject json) {
        return false;
    }

    @Override
    public void lordJSon(JSONObject json, Lord lord) {

    }

    @Override
    public void generate(Lord lord) {

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
}
