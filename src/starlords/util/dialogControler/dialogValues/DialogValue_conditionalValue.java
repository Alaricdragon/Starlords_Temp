package starlords.util.dialogControler.dialogValues;

import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import starlords.person.Lord;
import starlords.util.dialogControler.DialogSet;
import starlords.util.dialogControler.dialogRull.DialogRule_Base;

import java.util.ArrayList;

public class DialogValue_conditionalValue extends DialogValue_base{
    ArrayList<DialogRule_Base> rules;
    DialogValuesList list;

    public DialogValue_conditionalValue(JSONObject json, String key) throws JSONException {
        this(json.getJSONObject(key));
    }
    @SneakyThrows
    public DialogValue_conditionalValue(JSONObject json){
        super(json);
        if (json.has("dialogValue")) list = new DialogValuesList(json,"dialogValue");
        /*if (json.has("rules"))*/ rules = DialogSet.getDialogRulesFromJSon(json.getJSONObject("rules"));
    }

    @Override
    public int computeValue(Lord lord, Lord targetLord) {
        if (!canUse(lord,targetLord)) return 0;
        return super.computeValue(lord, targetLord);
    }

    @Override
    public int value(Lord lord, Lord targetLord) {
        if (list == null) return 0;
        return list.getValue(lord,targetLord);
    }
    private boolean canUse(Lord lord, Lord targetLord){
        for (DialogRule_Base a : rules){
            if (!a.condition(lord,targetLord)) return false;
        }
        return true;
    }
}
