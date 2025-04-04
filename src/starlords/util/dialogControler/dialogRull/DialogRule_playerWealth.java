package starlords.util.dialogControler.dialogRull;

import com.fs.starfarer.api.Global;
import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.person.Lord;

public class DialogRule_playerWealth extends DialogRule_Base {
    int max = 2147483647;
    int min = -2147483647;
    @SneakyThrows
    public DialogRule_playerWealth(JSONObject jsonObject){
        if (jsonObject.has("max")) max = jsonObject.getInt("max");
        if (jsonObject.has("min")) min = jsonObject.getInt("min");
    }

    @Override
    public boolean condition(Lord lord) {
        int playerCredits = (int) Global.getSector().getPlayerFleet().getCargo().getCredits().get();
        if (min <= playerCredits && playerCredits <= max) return true;
        return false;
    }
}
