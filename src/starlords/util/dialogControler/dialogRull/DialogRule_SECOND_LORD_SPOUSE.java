package starlords.util.dialogControler.dialogRull;

import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.controllers.LordController;
import starlords.person.Lord;
import starlords.util.dialogControler.DialogSet;

import java.util.ArrayList;

public class DialogRule_SECOND_LORD_SPOUSE extends DialogRule_Base {
    ArrayList<DialogRule_Base> rules;
    @SneakyThrows
    public DialogRule_SECOND_LORD_SPOUSE(JSONObject jsonObject){
        rules = DialogSet.getDialogRulesFromJSon(jsonObject);
    }

    @Override
    public boolean condition(Lord lord, Lord targetLord) {
        if (targetLord == null) return false;
        String id = targetLord.getSpouse();
        if (id == null) return false;
        Lord activeLord = LordController.getLordById(id);
        if (activeLord == null) return false;
        return rulesWork(activeLord, targetLord);
    }

    @Override
    public boolean condition(Lord lord) {
        return false;
    }
    private boolean rulesWork(Lord lord, Lord targetLord){
        for (DialogRule_Base a : rules){
            if (!a.condition(lord,targetLord)) return false;
        }
        return true;
    }

}
