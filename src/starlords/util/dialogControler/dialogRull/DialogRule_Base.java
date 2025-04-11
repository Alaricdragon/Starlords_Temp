package starlords.util.dialogControler.dialogRull;

import starlords.person.Lord;

public class DialogRule_Base {
    public boolean condition(Lord lord,Lord targetLord){
        if (targetLord != null) lord = targetLord;
        return condition(lord);
    }
    public boolean condition(Lord lord){
        return true;
    }
}
