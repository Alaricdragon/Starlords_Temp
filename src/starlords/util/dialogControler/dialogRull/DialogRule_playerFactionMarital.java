package starlords.util.dialogControler.dialogRull;

import starlords.controllers.LordController;
import starlords.person.Lord;
import starlords.person.LordAction;

public class DialogRule_playerFactionMarital extends DialogRule_Base {
    boolean feast;
    public DialogRule_playerFactionMarital(boolean feast){
        this.feast = feast;
    }

    @Override
    public boolean condition(Lord lord) {
        boolean sameFaction = LordController.getPlayerLord().getFaction().equals(lord.getFaction());
        if (sameFaction){
            sameFaction = LordController.getPlayerLord().isMarshal();
        }
        return feast == (sameFaction);
    }

}
