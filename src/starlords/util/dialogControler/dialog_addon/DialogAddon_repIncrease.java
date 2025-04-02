package starlords.util.dialogControler.dialog_addon;

import com.fs.starfarer.api.campaign.OptionPanelAPI;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import starlords.person.Lord;
import starlords.util.Utils;
import starlords.util.dialogControler.DialogSet;

import java.awt.*;

public class DialogAddon_repIncrease extends DialogAddon_Base{
    int max, min;
    public DialogAddon_repIncrease(int max,int min){
        this.max = max;
        this.min = min;
    }
    @Override
    public void apply(TextPanelAPI textPanel, OptionPanelAPI options, Lord lord){
        int change = min + Utils.rand.nextInt(max - min);
        lord.getLordAPI().getRelToPlayer().adjustRelationship((float) (change*0.01), null);
        String line = DialogSet.getLineWithInserts(lord,"relation_increase");
        line = DialogSet.insertData(line,"%c0",""+change);
        textPanel.addPara(line, Color.GREEN);
    }
}
