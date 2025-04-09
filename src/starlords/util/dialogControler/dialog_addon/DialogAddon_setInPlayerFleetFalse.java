package starlords.util.dialogControler.dialog_addon;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.OptionPanelAPI;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.ids.MemFlags;
import com.fs.starfarer.api.util.Misc;
import starlords.controllers.EventController;
import starlords.person.Lord;
import starlords.person.LordAction;

import static starlords.ai.LordAI.BUSY_REASON;

public class DialogAddon_setInPlayerFleetFalse extends DialogAddon_Base{
    public DialogAddon_setInPlayerFleetFalse(){
    }
    @Override
    public void apply(TextPanelAPI textPanel, OptionPanelAPI options, InteractionDialogAPI dialog, Lord lord){
        if (lord.getCurrAction() == LordAction.COMPANION) {
            Misc.setMercenary(lord.getLordAPI(), false);
            Global.getSector().getPlayerFleet().getFleetData().removeOfficer(lord.getLordAPI());
            for (FleetMemberAPI ship : Global.getSector().getPlayerFleet().getFleetData().getMembersListCopy()) {
                if (lord.getLordAPI().equals(ship.getCaptain())) ship.setCaptain(null);
            }
            lord.getLordAPI().setFleet(lord.getOldFleet());
            lord.setOldFleet(null);
            lord.setCurrAction(null);
            lord.getFleet().fadeInIndicator();
            lord.getFleet().setHidden(false);
        }
    }
}
