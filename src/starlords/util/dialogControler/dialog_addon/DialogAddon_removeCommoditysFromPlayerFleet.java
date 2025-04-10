package starlords.util.dialogControler.dialog_addon;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.OptionPanelAPI;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import starlords.person.Lord;
import starlords.util.Utils;
import starlords.util.dialogControler.DialogSet;

import java.util.HashMap;

public class DialogAddon_removeCommoditysFromPlayerFleet extends DialogAddon_Base{
    int max, min;
    String item;
    public DialogAddon_removeCommoditysFromPlayerFleet(String key, int max, int min){
        this.max = max;
        this.min = min;
        this.item=key;
    }
    @Override
    public void apply(TextPanelAPI textPanel, OptionPanelAPI options, InteractionDialogAPI dialog, Lord lord){
        int ranChange = max - min;
        if (ranChange > 0) ranChange = Utils.rand.nextInt(ranChange);
        int change = min + ranChange;
        Global.getSector().getPlayerFleet().getCargo().addCommodity(item,(change));

        HashMap<String,String> inserts = new HashMap<>();
        inserts.put("%c0",""+change);
        inserts.put("%c1",item);
        DialogSet.addParaWithInserts("item_added",lord,textPanel,options,dialog,false,inserts);

    }
}
