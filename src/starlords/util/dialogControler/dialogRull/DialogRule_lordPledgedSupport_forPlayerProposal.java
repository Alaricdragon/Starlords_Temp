package starlords.util.dialogControler.dialogRull;

import starlords.controllers.EventController;
import starlords.controllers.LordController;
import starlords.controllers.PoliticsController;
import starlords.faction.LawProposal;
import starlords.person.Lord;
import starlords.person.LordAction;
import starlords.person.LordEvent;

public class DialogRule_lordPledgedSupport_forPlayerProposal extends DialogRule_Base {
    boolean feast;
    public DialogRule_lordPledgedSupport_forPlayerProposal(boolean feast){
        this.feast = feast;
    }

    @Override
    public boolean condition(Lord lord) {
        LawProposal currentProposal = PoliticsController.getProposal(LordController.getPlayerLord());
        if(currentProposal == null) return false;
        return feast == currentProposal.getPledgedFor().contains(lord.getLordAPI().getId());
    }
}
