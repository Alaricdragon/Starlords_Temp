package starlords.util.dialogControler.dialogRull;

import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.person.Lord;

public class DialogRule_relationWithPlayer extends DialogRule_Base {
    int max = 100;
    int min = -100;
    @SneakyThrows
    public DialogRule_relationWithPlayer(JSONObject jsonObject){
        if (jsonObject.has("max")) max = jsonObject.getInt("max");
        if (jsonObject.has("min")) min = jsonObject.getInt("min");
    }

    @Override
    public boolean condition(Lord lord) {
        int rel = lord.getPlayerRel();
        if (min <= rel && rel <= max) return true;
        return false;
    }
}
