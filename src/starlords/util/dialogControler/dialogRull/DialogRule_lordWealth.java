package starlords.util.dialogControler.dialogRull;

import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.person.Lord;

public class DialogRule_lordWealth extends DialogRule_Base {
    int max = 1000000000;
    int min = -1000000;
    @SneakyThrows
    public DialogRule_lordWealth(JSONObject jsonObject){
        if (jsonObject.has("max")) max = jsonObject.getInt("max");
        if (jsonObject.has("min")) min = jsonObject.getInt("min");
    }

    @Override
    public boolean condition(Lord lord) {
        float rel = lord.getWealth();
        if (min <= rel && rel <= max) return true;
        return false;
    }
}
