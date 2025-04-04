package starlords.util.dialogControler.dialog_addon;

import com.fs.starfarer.api.campaign.OptionPanelAPI;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import starlords.person.Lord;
import starlords.util.Utils;
import starlords.util.dialogControler.DialogSet;

import java.awt.*;
import java.util.HashMap;

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

        HashMap<String,String> inserts = new HashMap<>();
        inserts.put("%c0",""+change);
        DialogSet.addParaWithInserts("relation_increase",lord,textPanel,options);

    }
}
