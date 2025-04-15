package starlords.util.dialogControler.dialogRull;

import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.controllers.EventController;
import starlords.controllers.LordController;
import starlords.person.Lord;
import starlords.util.dialogControler.DialogSet;

import java.util.ArrayList;

public class DialogRule_LORD_HOST  extends DialogRule_Base {
    ArrayList<DialogRule_Base> rules;
    @SneakyThrows
    public DialogRule_LORD_HOST(JSONObject jsonObject){
        rules = DialogSet.getDialogRulesFromJSon(jsonObject);
    }

    @Override
    public boolean condition(Lord lord, Lord targetLord) {
        boolean check = EventController.getCurrentFeast(lord.getLordAPI().getFaction()) != null && EventController.getCurrentFeast(lord.getLordAPI().getFaction()).getOriginator() != null;
        if (!check) return false;
        Lord activeLord = EventController.getCurrentFeast(lord.getLordAPI().getFaction()).getOriginator();
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
