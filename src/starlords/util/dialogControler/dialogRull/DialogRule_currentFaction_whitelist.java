package starlords.util.dialogControler.dialogRull;

import starlords.person.Lord;

import java.util.ArrayList;

public class DialogRule_currentFaction_whitelist extends DialogRule_Base {
    ArrayList<String> data;
    public DialogRule_currentFaction_whitelist(ArrayList<String> data){
        this.data = data;
    }
    @Override
    public boolean condition(Lord lord) {
        for (String a : data){
            if (a.equals(lord.getFaction().getId())){
                return true;
            }
        }
        return false;
    }
}
