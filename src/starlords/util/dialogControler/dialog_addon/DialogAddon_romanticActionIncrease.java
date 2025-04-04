package starlords.util.dialogControler.dialog_addon;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.OptionPanelAPI;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import starlords.person.Lord;
import starlords.util.Utils;
import starlords.util.dialogControler.DialogSet;

import java.util.HashMap;

public class DialogAddon_romanticActionIncrease extends DialogAddon_Base{
    int max, min;
    public DialogAddon_romanticActionIncrease(int max, int min){
        this.max = max;
        this.min = min;
    }
    @Override
    public void apply(TextPanelAPI textPanel, OptionPanelAPI options, Lord lord){
        int change = min + Utils.rand.nextInt(max - min);
        lord.setRomanticActions(lord.getRomanticActions()+change);
    }
}
