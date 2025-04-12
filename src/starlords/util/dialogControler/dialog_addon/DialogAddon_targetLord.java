package starlords.util.dialogControler.dialog_addon;

import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.OptionPanelAPI;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.person.Lord;
import starlords.util.dialogControler.DialogSet;

import java.util.ArrayList;

public class DialogAddon_targetLord extends DialogAddon_Base {
    ArrayList<DialogAddon_Base> addons;
    @SneakyThrows
    public DialogAddon_targetLord(JSONObject jsonObject){
        addons = DialogSet.getDialogAddonsFromJSon(jsonObject);
    }

    @Override
    public void apply(TextPanelAPI textPanel, OptionPanelAPI options, InteractionDialogAPI dialog, Lord lord, Lord targetLord) {
        if (targetLord == null) return;
        applyAddons(textPanel,options,dialog,targetLord, lord);
    }
    private void applyAddons(TextPanelAPI textPanel, OptionPanelAPI options, InteractionDialogAPI dialog, Lord lord, Lord targetLord){
        for (DialogAddon_Base a : addons){
            a.apply(textPanel,options,dialog,lord,targetLord);
        }
    }
}
