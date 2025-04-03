package starlords.util.dialogControler.dialogRull;

import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.controllers.LordController;
import starlords.person.Lord;

public class DialogRule_playerRank extends DialogRule_Base {
    int max = 100;
    int min = 0;
    @SneakyThrows
    public DialogRule_playerRank(JSONObject jsonObject){
        if (jsonObject.has("max")) max = jsonObject.getInt("max");
        if (jsonObject.has("min")) min = jsonObject.getInt("min");
    }

    @Override
    public boolean condition(Lord lord) {
        int rel = LordController.getPlayerLord().getRanking();
        if (min <= rel && rel <= max) return true;
        return false;
    }
}
