package starlords.util.dialogControler.dialog_addon;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.OptionPanelAPI;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import starlords.person.Lord;
import starlords.util.Utils;
import starlords.util.dialogControler.DialogSet;

import java.util.HashMap;

public class DialogAddon_addCommoditysToPlayerFleet extends DialogAddon_Base{
    int max, min;
    String item;
    public DialogAddon_addCommoditysToPlayerFleet(String key,int max,int min){
        this.max = max;
        this.min = min;
        this.item=key;
    }
    @Override
    public void apply(TextPanelAPI textPanel, OptionPanelAPI options, InteractionDialogAPI dialog, Lord lord){
        int change = min + Utils.rand.nextInt(max - min);
        Global.getSector().getPlayerFleet().getCargo().removeCommodity(item,(change));

        HashMap<String,String> inserts = new HashMap<>();
        inserts.put("%c0",""+change);
        inserts.put("%c1",item);
        DialogSet.addParaWithInserts("item_lost",lord,textPanel,options,dialog);

    }
}
