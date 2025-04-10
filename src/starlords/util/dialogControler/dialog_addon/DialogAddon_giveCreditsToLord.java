package starlords.util.dialogControler.dialog_addon;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.OptionPanelAPI;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import starlords.person.Lord;
import starlords.util.Utils;
import starlords.util.dialogControler.DialogSet;

import java.util.HashMap;

public class DialogAddon_giveCreditsToLord extends DialogAddon_Base{
    int max, min;
    public DialogAddon_giveCreditsToLord(int max,int min){
        this.max = max;
        this.min = min;
    }
    @Override
    public void apply(TextPanelAPI textPanel, OptionPanelAPI options, InteractionDialogAPI dialog, Lord lord){
        int change = min + Utils.rand.nextInt(max - min);
        change = Math.min((int) change, (int) Global.getSector().getPlayerFleet().getCargo().getCredits().get());
        Global.getSector().getPlayerFleet().getCargo().getCredits().subtract(change);
        lord.addWealth(change);
        HashMap<String,String> inserts = new HashMap<>();
        inserts.put("%c0",""+change);
        DialogSet.addParaWithInserts("credits_decrease",lord,textPanel,options,dialog);

    }
}
