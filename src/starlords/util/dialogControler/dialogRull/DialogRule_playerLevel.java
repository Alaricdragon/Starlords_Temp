package starlords.util.dialogControler.dialogRull;

import com.fs.starfarer.api.Global;
import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.person.Lord;

public class DialogRule_playerLevel extends DialogRule_Base {
    int max = 2147483647;
    int min = -2147483647;
    @SneakyThrows
    public DialogRule_playerLevel(JSONObject jsonObject){
        if (jsonObject.has("max")) max = jsonObject.getInt("max");
        if (jsonObject.has("min")) min = jsonObject.getInt("min");
    }

    @Override
    public boolean condition(Lord lord) {
        int rel = Global.getSector().getPlayerPerson().getStats().getLevel();
        if (min <= rel && rel <= max) return true;
        return false;
    }
}
