package starlords.util.dialogControler.dialogRull;

import starlords.person.Lord;
import starlords.person.LordAction;

public class DialogRule_playerSubject extends DialogRule_Base {
    boolean feast;
    public DialogRule_playerSubject(boolean feast){
        this.feast = feast;
    }

    @Override
    public boolean condition(Lord lord) {
        return feast == (lord.getFaction().isPlayerFaction());
    }

}
