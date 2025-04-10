package starlords.util.dialogControler.dialog_addon;

import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.OptionPanelAPI;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import starlords.person.Lord;
import starlords.util.Utils;
import starlords.util.dialogControler.DialogSet;

import java.util.HashMap;

public class DialogAddon_askForLordLocation extends DialogAddon_Base {
    Lord savedLord;
    public DialogAddon_askForLordLocation(Lord savedLord){
        this.savedLord=savedLord;
    }
    @Override
    public void apply(TextPanelAPI textPanel, OptionPanelAPI options, InteractionDialogAPI dialog, Lord lord) {
        if (savedLord != null && savedLord.getLordAPI().getFleet().isAlive()) {
            HashMap<String,String> insirts = new HashMap<>();
            insirts.put("%c0",savedLord.getTitle());
            insirts.put("%c1",savedLord.getLordAPI().getNameString());
            insirts.put("%c2",Utils.getNearbyDescription(savedLord.getLordAPI().getFleet()));
            DialogSet.addParaWithInserts("foundLordsLocation",lord,textPanel,options,dialog,false,insirts);
        } else {
            HashMap<String,String> insirts = new HashMap<>();
            insirts.put("%c0",savedLord.getTitle());
            insirts.put("%c1",savedLord.getLordAPI().getNameString());
            DialogSet.addParaWithInserts("failedToFindLordsLocation",lord,textPanel,options,dialog,false,insirts);
        }
    }
}
