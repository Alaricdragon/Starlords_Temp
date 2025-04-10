package starlords.util.dialogControler.dialog_addon;

import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.OptionPanelAPI;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import starlords.controllers.LordController;
import starlords.controllers.PoliticsController;
import starlords.faction.LawProposal;
import starlords.person.Lord;

public class DialogAddon_setPledgedFor_PlayerProposal extends DialogAddon_Base{
    boolean hadDate;
    public DialogAddon_setPledgedFor_PlayerProposal(boolean hadDate){
        this.hadDate=hadDate;
    }
    @Override
    public void apply(TextPanelAPI textPanel, OptionPanelAPI options, InteractionDialogAPI dialog, Lord lord){
        LawProposal proposal = PoliticsController.getProposal(LordController.getPlayerLord());
        if (hadDate) proposal.getPledgedAgainst().remove(lord.getLordAPI().getId());
        if (hadDate && !proposal.getPledgedFor().contains(lord.getLordAPI().getId())){
            proposal.getPledgedFor().add(lord.getLordAPI().getId());
            return;
        }
        if (!hadDate){
            proposal.getPledgedFor().remove(lord.getLordAPI().getId());
        }
    }

}
