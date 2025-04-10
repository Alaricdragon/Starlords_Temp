package starlords.util.dialogControler.dialogRull;

import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.controllers.LordController;
import starlords.controllers.PoliticsController;
import starlords.faction.LawProposal;
import starlords.person.Lord;

public class DialogRule_playerProposalOpposers  extends DialogRule_Base {
    int max = 2147483647;
    int min = -2147483647;
    @SneakyThrows
    public DialogRule_playerProposalOpposers(JSONObject jsonObject){
        if (jsonObject.has("max")) max = jsonObject.getInt("max");
        if (jsonObject.has("min")) min = jsonObject.getInt("min");
    }

    @Override
    public boolean condition(Lord lord) {
        LawProposal proposal = PoliticsController.getProposal(LordController.getPlayerLord());
        if (proposal == null) return false;
        int rel = proposal.getOpposers().size();
        if (min <= rel && rel <= max) return true;
        return false;
    }
}
