package starlords.util.dialogControler.dialogRull;

import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.controllers.RelationController;
import starlords.person.Lord;

public class DialogRule_relationsBetweenLords  extends DialogRule_Base {
    int max = 2147483647;
    int min = -2147483647;
    @SneakyThrows
    public DialogRule_relationsBetweenLords(JSONObject jsonObject){
        if (jsonObject.has("max")) max = jsonObject.getInt("max");
        if (jsonObject.has("min")) min = jsonObject.getInt("min");
    }
    @Override
    public boolean condition(Lord lord,Lord targetLord) {
        if (targetLord == null) return false;
        int rel = (RelationController.getRelation(lord,targetLord));
        if (min <= rel && rel <= max) return true;
        return false;
    }
}
