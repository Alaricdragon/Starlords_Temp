package starlords.util.dialogControler.dialog_addon;

import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.OptionPanelAPI;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.person.Lord;
import starlords.plugins.LordInteractionDialogPluginImpl;
import starlords.util.Utils;
import starlords.util.dialogControler.dialogValues.DialogValuesList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class DialogAddon_setDialogData extends DialogAddon_Base{
    protected HashMap<String,String> strings = new HashMap<>();
    protected HashMap<String,Boolean> booleans = new HashMap<>();
    protected HashMap<String, DialogValuesList> setInts = new HashMap<>();
    protected HashMap<String,DialogValuesList> addIntsMin = new HashMap<>();
    protected HashMap<String,DialogValuesList> addIntsMax = new HashMap<>();
    @SneakyThrows
    public DialogAddon_setDialogData(JSONObject json){
        for (Iterator it = json.keys(); it.hasNext(); ) {
            String key2 = (String) it.next();
            if (json.get(key2) instanceof JSONObject && (json.getJSONObject(key2).has("min") || json.getJSONObject(key2).has("max"))){
                JSONObject a = json.getJSONObject(key2);
                if (json.getJSONObject(key2).has("min")) addIntsMin.put(key2,new DialogValuesList(json.getJSONObject(key2),"min"));
                if (json.getJSONObject(key2).has("max")) addIntsMax.put(key2,new DialogValuesList(json.getJSONObject(key2),"max"));
                continue;
            }
            if (json.get(key2) instanceof String){
                String a = json.getString(key2);
                strings.put(key2,a);
                continue;
            }
            if (json.get(key2) instanceof Boolean){
                boolean a = json.getBoolean(key2);
                booleans.put(key2,a);
                continue;
            }
            setInts.put(key2,new DialogValuesList(json,key2));
        }
    }
    @Override
    public void apply(TextPanelAPI textPanel, OptionPanelAPI options, InteractionDialogAPI dialog, Lord lord,Lord targetLord) {
        applyStrings(dialog, lord,targetLord);
        applyBooleans(dialog, lord,targetLord);
        applyFloats(dialog, lord,targetLord);
        applyAddFloats(dialog, lord,targetLord);
    }
    public void applyStrings(InteractionDialogAPI dialog, Lord lord,Lord targetLord){
        for (String key : strings.keySet()) {
            LordInteractionDialogPluginImpl.DATA_HOLDER.getStrings().put(key,strings.get(key));
        }
    }
    public void applyBooleans(InteractionDialogAPI dialog, Lord lord,Lord targetLord){
        for (String key : booleans.keySet()) {
            LordInteractionDialogPluginImpl.DATA_HOLDER.getBooleans().put(key,booleans.get(key));
        }

    }
    public void applyFloats(InteractionDialogAPI dialog, Lord lord,Lord targetLord){
        for (String key : setInts.keySet()) {
            LordInteractionDialogPluginImpl.DATA_HOLDER.getIntegers().put(key, setInts.get(key).getValue(lord,targetLord));
        }
    }
    public void applyAddFloats(InteractionDialogAPI dialog, Lord lord,Lord targetLord){
        for (String key : addIntsMin.keySet()) {
            int min = addIntsMin.get(key).getValue(lord,targetLord);
            int max = addIntsMax.get(key).getValue(lord,targetLord);;
            int baseValue = LordInteractionDialogPluginImpl.DATA_HOLDER.getIntegers().get(key);
            max = Math.max(min,max);
            int range = max - min;
            if (range != 0){
                boolean negitive = false;
                if (range < 0){
                    negitive = true;
                }
                if (negitive) range *=-1;
                range = Utils.rand.nextInt(range);
                if (negitive) range *=-1;
            }
            int data = baseValue + (min+range);
            LordInteractionDialogPluginImpl.DATA_HOLDER.getIntegers().put(key,data);
        }
    }
}
