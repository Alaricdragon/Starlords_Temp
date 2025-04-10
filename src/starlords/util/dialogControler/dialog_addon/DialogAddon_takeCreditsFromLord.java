package starlords.util.dialogControler.dialog_addon;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.OptionPanelAPI;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import starlords.person.Lord;
import starlords.util.Utils;
import starlords.util.dialogControler.DialogSet;

import java.util.HashMap;

public class DialogAddon_takeCreditsFromLord extends DialogAddon_Base{
    int max, min;
    public DialogAddon_takeCreditsFromLord(int max,int min){
        this.max = max;
        this.min = min;
    }
    @Override
    public void apply(TextPanelAPI textPanel, OptionPanelAPI options, InteractionDialogAPI dialog, Lord lord){
        int change = min + Utils.rand.nextInt(max - min);
        Global.getSector().getPlayerFleet().getCargo().getCredits().add(change);
        lord.addWealth(change*-1);
        HashMap<String,String> inserts = new HashMap<>();
        inserts.put("%c0",""+change);
        DialogSet.addParaWithInserts("credits_increase",lord,textPanel,options,dialog);

    }
}
