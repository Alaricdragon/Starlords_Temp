package starlords.util.dialogControler.dialogValues;

import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.controllers.LordController;
import starlords.person.Lord;
import starlords.util.dialogControler.DialogSet;
import starlords.util.dialogControler.dialogRull.DialogRule_Base;

import java.util.ArrayList;

public class DialogValue_validLordNumbers extends DialogValue_base{
    ArrayList<DialogRule_Base> rules = new ArrayList<>();
    @SneakyThrows
    public DialogValue_validLordNumbers(JSONObject json, String key) {
        super(json, key);
        if (json.get(key) instanceof JSONObject) DialogSet.getDialogRulesFromJSon(json.getJSONObject("key").getJSONObject("rules"));
    }

    @Override
    public int value(Lord lord, Lord targetLord) {
        int value = 0;
        for (Lord a : LordController.getLordsList()){
            if (can(a,lord)) value++;
        }
        return value;
    }
    private boolean can(Lord lord, Lord targetLord){
        for (DialogRule_Base a : rules){
            if (!a.condition(lord,targetLord)) return false;
        }
        return true;
    }
}
