package starlords.util.dialogControler;

import org.json.JSONException;
import org.json.JSONObject;
import starlords.person.Lord;
import starlords.util.dialogControler.dialogRull.DialogRule_Base;

import java.util.ArrayList;
import java.util.Iterator;

public class LordDialog {
    public static ArrayList<ArrayList<DialogSet>> organizedDialogSets = new ArrayList<>();
    private static ArrayList<DialogRule_Base> rules = new ArrayList<>();
    public LordDialog(String key,JSONObject jsonObject) throws JSONException {
        organizedDialogSets = new ArrayList<>();
        rules = DialogSet.getDialogFromJSon(jsonObject.getJSONObject("rules"));
        jsonObject = jsonObject.getJSONObject("lines");
        for (Iterator it2 = jsonObject.keys(); it2.hasNext();) {
            String key2 = (String) it2.next();
            new DialogSet(key2,jsonObject.getJSONObject(key2),this);
        }
        int priority = 0;
        if (jsonObject.has("priority")) priority = jsonObject.getInt("priority");
        while (DialogSet.organizedDialogs.size() <= priority){
            DialogSet.organizedDialogs.add(new ArrayList<>());
        }
        DialogSet.organizedDialogs.get(priority).add(this);
        DialogSet.dialogs.put(key,this);
    }
    public DialogSet getSet(Lord lord, String id){
        if (!isAllowed(lord)) return null;
        for (int a = organizedDialogSets.size() - 1; a >= 0; a--){
            for (DialogSet b : organizedDialogSets.get(a)){
                if (b.canUseDialog(lord) && b.hasLine(id)){
                    return b;
                }
            }
        }
        return null;
    }
    private boolean isAllowed(Lord lord){
        for (DialogRule_Base a : rules){
            if (!a.condition(lord)) return false;
        }
        return true;
    }
}
