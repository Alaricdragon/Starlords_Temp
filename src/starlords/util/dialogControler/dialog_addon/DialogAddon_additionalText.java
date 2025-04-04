package starlords.util.dialogControler.dialog_addon;

import com.fs.starfarer.api.campaign.OptionPanelAPI;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import starlords.person.Lord;
import starlords.util.dialogControler.DialogSet;

public class DialogAddon_additionalText extends DialogAddon_Base{
    String lineID;
    public DialogAddon_additionalText(String lineID){
        this.lineID = lineID;
    }

    @Override
    public void apply(TextPanelAPI textPanel, OptionPanelAPI options, Lord lord) {
        DialogSet.addParaWithInserts(lineID,lord,textPanel,options);
    }
}
