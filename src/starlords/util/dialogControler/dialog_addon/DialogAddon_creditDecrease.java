package starlords.util.dialogControler.dialog_addon;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.OptionPanelAPI;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import starlords.person.Lord;
import starlords.util.Utils;
import starlords.util.dialogControler.DialogSet;

import java.awt.*;

public class DialogAddon_creditDecrease extends DialogAddon_Base{
    int max, min;
    public DialogAddon_creditDecrease(int max,int min){
        this.max = max;
        this.min = min;
    }
    @Override
    public void apply(TextPanelAPI textPanel, OptionPanelAPI options, Lord lord){
        int change = min + Utils.rand.nextInt(max - min);
        change = Math.min((int) change, (int) Global.getSector().getPlayerFleet().getCargo().getCredits().get());
        Global.getSector().getPlayerFleet().getCargo().getCredits().subtract(change);
        String line = DialogSet.getLineWithInserts(lord,"credits_decrease");
        line = DialogSet.insertData(line,"%c0",line);
        textPanel.addPara(line, Color.RED);

    }
}
