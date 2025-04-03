package starlords.util.dialogControler.dialogRull;

import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.person.Lord;

public class DialogRule_lordLevel extends DialogRule_Base {
    int max = 1000000;
    int min = 0;
    @SneakyThrows
    public DialogRule_lordLevel(JSONObject jsonObject){
        if (jsonObject.has("max")) max = jsonObject.getInt("max");
        if (jsonObject.has("min")) min = jsonObject.getInt("min");
    }

    @Override
    public boolean condition(Lord lord) {
        int rel = lord.getLordAPI().getStats().getLevel();
        if (min <= rel && rel <= max) return true;
        return false;
    }
}
