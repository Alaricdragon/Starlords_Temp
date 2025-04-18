package starlords.util.dialogControler.dialogRull;

import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.controllers.LordController;
import starlords.person.Lord;
import starlords.util.dialogControler.DialogSet;
import starlords.util.dialogControler.dialogRull.bases.DialogRule_minmax;

import java.util.ArrayList;

public class DialogRule_validLordNumbers extends DialogRule_minmax {
    ArrayList<DialogRule_Base> rules;
    @SneakyThrows
    public DialogRule_validLordNumbers(JSONObject jsonObject,String key){
        super(jsonObject, key);
        rules = DialogSet.getDialogRulesFromJSon(jsonObject.getJSONObject(key).getJSONObject("rules"));
    }
    @Override
    protected int getValue(Lord lord, Lord targetLord) {
        int rel = 0;
        for (Lord lord2 : LordController.getLordsList()){
            if (isLordValid(lord,lord2)) rel++;
        }
        return rel;
    }
    private boolean isLordValid(Lord lord, Lord lord2){
        for (DialogRule_Base a: rules){
            if (!a.condition(lord,lord2)) return false;
        }
        return true;
    }
}
