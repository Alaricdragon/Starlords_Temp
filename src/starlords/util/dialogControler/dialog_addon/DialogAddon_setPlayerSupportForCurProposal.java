package starlords.util.dialogControler.dialog_addon;

import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.OptionPanelAPI;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import starlords.controllers.LordController;
import starlords.controllers.PoliticsController;
import starlords.faction.LawProposal;
import starlords.person.Lord;

public class DialogAddon_setPlayerSupportForCurProposal extends DialogAddon_Base{
    boolean hadDate;
    public DialogAddon_setPlayerSupportForCurProposal(boolean hadDate){
        this.hadDate=hadDate;
    }
    @Override
    public void apply(TextPanelAPI textPanel, OptionPanelAPI options, InteractionDialogAPI dialog, Lord lord){
        LawProposal currentProposal = PoliticsController.getCurrProposal(lord.getFaction());
        if (hadDate) {
            currentProposal.getPledgedAgainst().remove(LordController.getPlayerLord().getLordAPI().getId());
            currentProposal.getPledgedFor().add(LordController.getPlayerLord().getLordAPI().getId());
            currentProposal.setPlayerSupports(true);
        }else{
            currentProposal.getPledgedFor().remove(LordController.getPlayerLord().getLordAPI().getId());
            currentProposal.getPledgedAgainst().add(LordController.getPlayerLord().getLordAPI().getId());
            currentProposal.setPlayerSupports(false);
        }
        PoliticsController.updateProposal(currentProposal);
    }
}
