package starlords.util.dialogControler.dialog_addon;

import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.OptionPanelAPI;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import org.json.JSONObject;
import starlords.person.Lord;
import starlords.util.dialogControler.DialogSet;
import starlords.util.dialogControler.dialog_addon.bases.DialogAddon_changeValue;

import java.util.HashMap;

public class DialogAddon_repChange extends DialogAddon_changeValue {
    public DialogAddon_repChange(JSONObject json, String key) {
        super(json, key);
    }

    @Override
    protected void increaseChange(int value, TextPanelAPI textPanel, OptionPanelAPI options, InteractionDialogAPI dialog, Lord lord, Lord targetLord) {
        lord.getLordAPI().getRelToPlayer().adjustRelationship((float) (value*0.01), null);
        HashMap<String,String> inserts = new HashMap<>();
        inserts.put("%c0",""+value);
        DialogSet.addParaWithInserts("relation_increase",lord,textPanel,options,dialog,false,inserts);
    }

    @Override
    protected void decreaseChange(int value, TextPanelAPI textPanel, OptionPanelAPI options, InteractionDialogAPI dialog, Lord lord, Lord targetLord) {
        value*=-1;
        lord.getLordAPI().getRelToPlayer().adjustRelationship((float) (value*-0.01), null);
        HashMap<String,String> inserts = new HashMap<>();
        inserts.put("%c0",""+value);
        DialogSet.addParaWithInserts("relation_decrease",lord,textPanel,options,dialog,false,inserts);
    }
}
