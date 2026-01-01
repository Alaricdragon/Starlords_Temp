package starlords.generator.dataBuilders;

import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.generator.LordBaseDataBuilder;
import starlords.person.Lord;
import starlords.util.ScriptedValues.SV_String;
import starlords.util.ScriptedValues.ScriptedValueController;
import starlords.util.Utils;

public class LordFavFood implements LordBaseDataBuilder {
    @SneakyThrows
    @Override
    public boolean shouldGenerate(Lord lord,JSONObject json) {
        if (json.has("preferredItem")) return false;
        return true;
    }

    @SneakyThrows
    @Override
    public void lordJSon(JSONObject json, Lord lord) {
        SV_String item = new ScriptedValueController(json.getString("preferredItem")).getNextString();
        lord.setPreferredItemId(item.getValue(lord));
    }

    @Override
    public void generate(Lord lord) {
        String[] items = {
                "domestic_goods",
                "drugs",
                "lobster",
                "luxury_goods",
                "food",
                "hand_weapons",
                "alpha_core"
        };
        double[] weight = {
                3,
                3,
                3,
                3,
                3,
                3,
                1
        };
        double maxValue = 0;
        for (double a : weight) maxValue+=a;
        double ran = Utils.rand.nextDouble()*maxValue;
        for (int a = 0; a < items.length; a++){
            ran-=weight[a];
            if (ran <= 0){
                lord.setPreferredItemId(items[a]);
                return;
            }
        }
        lord.setPreferredItemId(items[0]);
    }

    @Override
    public void prepareStorgeInMemCompressedOrganizer() {
        //this is stored directly in the lord class.
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
