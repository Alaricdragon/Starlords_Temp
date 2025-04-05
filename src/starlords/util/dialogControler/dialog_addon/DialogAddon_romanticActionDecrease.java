package starlords.util.dialogControler.dialog_addon;

import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.OptionPanelAPI;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import starlords.person.Lord;
import starlords.util.Utils;

public class DialogAddon_romanticActionDecrease extends DialogAddon_Base{
    int max, min;
    public DialogAddon_romanticActionDecrease(int max, int min){
        this.max = max;
        this.min = min;
    }
    @Override
    public void apply(TextPanelAPI textPanel, OptionPanelAPI options, InteractionDialogAPI dialog, Lord lord){
        int change = min + Utils.rand.nextInt(max - min);
        lord.setRomanticActions(lord.getRomanticActions()-change);
    }
}
