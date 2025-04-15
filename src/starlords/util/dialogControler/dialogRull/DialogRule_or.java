package starlords.util.dialogControler.dialogRull;

import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;
import starlords.person.Lord;
import starlords.util.dialogControler.DialogSet;

import java.util.ArrayList;

public class DialogRule_or extends DialogRule_Base {
    ArrayList<ArrayList<DialogRule_Base>> rules;
    @SneakyThrows
    public DialogRule_or(JSONArray jsonArray){
        rules = new ArrayList<>();
        for (int a = 0; a < jsonArray.length(); a++){
            JSONObject object = jsonArray.getJSONObject(a);
            rules.add(DialogSet.getDialogRulesFromJSon(object));
        }
    }

    @Override
    public boolean condition(Lord lord, Lord targetLord) {
        for (ArrayList<DialogRule_Base> a : rules){
            if (rulesWork(a,lord,targetLord)) return true;
        }
        return false;
    }

    @Override
    public boolean condition(Lord lord) {
        return condition(lord,null);
    }
    private boolean rulesWork(ArrayList<DialogRule_Base> rules,Lord lord, Lord targetLord){
        for (DialogRule_Base a : rules){
            if (!a.condition(lord,targetLord)) return false;
        }
        return true;
    }
}
