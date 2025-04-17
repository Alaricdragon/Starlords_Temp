package starlords.util.dialogControler.dialogRull;

import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.controllers.PoliticsController;
import starlords.faction.LawProposal;
import starlords.person.Lord;
import starlords.util.dialogControler.dialogRull.bases.DialogRule_minmax;

public class DialogRule_lordProposalSupporters extends DialogRule_minmax {
    @SneakyThrows
    public DialogRule_lordProposalSupporters(JSONObject jsonObject,String key){
        super(jsonObject, key);

    }

    @Override
    protected int getValue(Lord lord, Lord targetLord) {
        LawProposal proposal = PoliticsController.getProposal(lord);
        if (proposal == null) return 0;
        int rel = proposal.getSupporters().size();
        return rel;
    }

    @Override
    public boolean condition(Lord lord) {
        LawProposal proposal = PoliticsController.getProposal(lord);
        if (proposal == null) return false;
        return super.condition(lord);
    }
}
