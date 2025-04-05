package starlords.util.dialogControler;

import com.fs.starfarer.api.campaign.OptionPanelAPI;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import starlords.person.Lord;
import starlords.util.dialogControler.dialog_addon.DialogAddon_Base;

import java.util.ArrayList;

public class DialogOption {
    public String optionID;
    ArrayList<DialogAddon_Base> addons;
    public DialogOption(String optionID, ArrayList<DialogAddon_Base> addons){
        this.optionID = optionID;
        this.addons=addons;
    }
    public void applyAddons(TextPanelAPI textPanel, OptionPanelAPI options, Lord lord){
        if (addons == null) return;
        for (DialogAddon_Base a : addons){
            a.apply(textPanel, options, lord);
        }
    }
}
