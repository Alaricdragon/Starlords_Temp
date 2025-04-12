package starlords.util.dialogControler.dialogRull;

import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.controllers.LordController;
import starlords.person.Lord;
import starlords.util.dialogControler.DialogSet;

import java.util.ArrayList;

public class DialogRule_validLordNumbers extends DialogRule_Base {
    int max = 2147483647;
    int min = -2147483647;
    ArrayList<DialogRule_Base> rules;
    @SneakyThrows
    public DialogRule_validLordNumbers(JSONObject jsonObject){
        if (jsonObject.has("max")) max = jsonObject.getInt("max");
        if (jsonObject.has("min")) min = jsonObject.getInt("min");
        rules = DialogSet.getDialogFromJSon(jsonObject.getJSONObject("rules"));
    }

    @Override
    public boolean condition(Lord lord) {
        int rel = 0;
        for (Lord lord2 : LordController.getLordsList()){
            if (isLordValid(lord2)) rel++;
        }
        if (min <= rel && rel <= max) return true;
        return false;
    }
    private boolean isLordValid(Lord lord){
        for (DialogRule_Base a: rules){
            if (!a.condition(lord)) return false;
        }
        return true;
    }
}
