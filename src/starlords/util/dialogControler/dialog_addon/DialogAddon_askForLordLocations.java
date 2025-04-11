package starlords.util.dialogControler.dialog_addon;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.OptionPanelAPI;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import org.apache.log4j.Logger;
import starlords.controllers.LordController;
import starlords.lunaSettings.StoredSettings;
import starlords.person.Lord;
import starlords.util.Utils;
import starlords.util.dialogControler.DialogOption;
import starlords.util.dialogControler.DialogSet;

import java.util.ArrayList;
import java.util.HashMap;

public class DialogAddon_askForLordLocations extends DialogAddon_Base {
    @Override
    public void apply(TextPanelAPI textPanel, OptionPanelAPI options, InteractionDialogAPI dialog, Lord lord){
        //NO LONGER IN USE!!!
        options.clearOptions();
        //Logger log = Global.getLogger(StoredSettings.class);
        //log.info("ADDING LORD SEARCH OPTIONS");
        ArrayList<Lord> toAdd = new ArrayList<>();
        for (Lord lord2 : LordController.getLordsList()) {
            if (!lord2.equals(lord)
                    && lord2.getLordAPI().getFaction().equals(lord.getLordAPI().getFaction())) {
                toAdd.add(lord2);
            }
        }
        Utils.canonicalLordSort(toAdd);
        for (Lord lord2 : toAdd) {
            //log.info("  adding additional things....");
            DialogAddon_Base addon = new DialogAddon_askForLordLocation(lord2);
            ArrayList<DialogAddon_Base> addons = new ArrayList<>();
            addons.add(addon);
            DialogOption option = new DialogOption("optionSet_askALordsLocation",addons);

            HashMap<String,String> insirts = new HashMap<>();
            insirts.put("%c0",lord2.getTitle());
            insirts.put("%c1",lord2.getLordAPI().getNameString());
            DialogSet.addOptionWithInserts("option_askALordsLocation",lord,textPanel,options,dialog,insirts,option);
        }
        DialogSet.addOptionWithInserts("option_nevermind_accept_ask_location",lord,textPanel,options,dialog,new HashMap<String,String>());
    }
}
