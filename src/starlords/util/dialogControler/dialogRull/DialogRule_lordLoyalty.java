package starlords.util.dialogControler.dialogRull;

import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.controllers.RelationController;
import starlords.person.Lord;

public class DialogRule_lordLoyalty extends DialogRule_Base {
    int max = 2147483647;
    int min = -2147483647;
    @SneakyThrows
    public DialogRule_lordLoyalty(JSONObject jsonObject){
        if (jsonObject.has("max")) max = jsonObject.getInt("max");
        if (jsonObject.has("min")) min = jsonObject.getInt("min");
    }

    @Override
    public boolean condition(Lord lord) {
        int rel = RelationController.getLoyalty(lord);
        if (min <= rel && rel <= max) return true;
        return false;
    }
}
