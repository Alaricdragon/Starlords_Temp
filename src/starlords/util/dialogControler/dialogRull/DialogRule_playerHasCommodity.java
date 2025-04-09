package starlords.util.dialogControler.dialogRull;

import com.fs.starfarer.api.Global;
import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.person.Lord;

public class DialogRule_playerHasCommodity extends DialogRule_Base {
    int max = 2147483647;
    int min = -2147483647;
    String item;
    @SneakyThrows
    public DialogRule_playerHasCommodity(String key,JSONObject jsonObject){
        if (jsonObject.has("max")) max = jsonObject.getInt("max");
        if (jsonObject.has("min")) min = jsonObject.getInt("min");
        item = key;
    }

    @Override
    public boolean condition(Lord lord) {
        int rel = (int) Global.getSector().getPlayerFleet().getCargo().getCommodityQuantity(item);
        if (min <= rel && rel <= max) return true;
        return false;
    }
}
