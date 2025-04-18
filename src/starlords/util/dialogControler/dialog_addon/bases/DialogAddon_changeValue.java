package starlords.util.dialogControler.dialog_addon.bases;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.OptionPanelAPI;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.person.Lord;
import starlords.util.Utils;
import starlords.util.dialogControler.dialogValues.DialogValuesList;
import starlords.util.dialogControler.dialog_addon.DialogAddon_Base;

public class DialogAddon_changeValue extends DialogAddon_Base {
    private int max = 2147483647;
    private int min = -2147483647;
    private DialogValuesList maxList;
    private DialogValuesList minList;
    @SneakyThrows
    public DialogAddon_changeValue(JSONObject json, String key){
        if (!(json.get(key) instanceof JSONObject)){
            int value = json.getInt(key);
            max=value;
            min=value;
            return;
        }
        JSONObject json2 = json.getJSONObject(key);
        if (json2.has("max")){
            maxList = new DialogValuesList(json2,"max");
        }
        if (json2.has("min")){
            minList = new DialogValuesList(json2,"min");
        }
        if ((!json2.has("min") && !json2.has("max"))){
            minList = new DialogValuesList(json2,key);
            maxList = minList;
            return;
        }
        if ((!json2.has("min") && !json2.has("max"))){
            minList = new DialogValuesList(json,key);
            maxList = minList;
            return;
        }
    }
    protected int getValue(Lord lord, Lord targetLord){
        int max = this.max;
        if (maxList != null) max = maxList.getValue(lord, targetLord);
        int min = this.min;
        if (minList != null) {
            if (!minList.equals(maxList)) {
                min = minList.getValue(lord, targetLord);
            }else{
                min = max;
            }
        }

        int ranChange = max - min;
        boolean isNegative = false;
        if (ranChange < 0){
            isNegative = true;
            ranChange*=-1;
        }
        if (ranChange != 0) ranChange = Utils.rand.nextInt(ranChange);
        if (isNegative) ranChange*=-1;
        int change = min + ranChange;
        return change;
    }

    @Override
    public void apply(TextPanelAPI textPanel, OptionPanelAPI options, InteractionDialogAPI dialog, Lord lord, Lord targetLord) {
        int value = getValue(lord, targetLord);
        if (value > 0){
            increaseChange(value,textPanel,options,dialog,lord,targetLord);
        }else if(value < 0){
            decreaseChange(value,textPanel,options,dialog,lord,targetLord);
        }
    }
    protected void increaseChange(int value,TextPanelAPI textPanel, OptionPanelAPI options, InteractionDialogAPI dialog, Lord lord, Lord targetLord){

    }
    protected void decreaseChange(int value,TextPanelAPI textPanel, OptionPanelAPI options, InteractionDialogAPI dialog, Lord lord, Lord targetLord){

    }
}
