package starlords.util.dialogControler.dialog_addon;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.OptionPanelAPI;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.person.Lord;
import starlords.plugins.LordInteractionDialogPluginImpl;
import starlords.util.Utils;

import java.util.HashMap;
import java.util.Iterator;

public class DialogAddon_setMemoryData extends DialogAddon_Base{
    HashMap<String,String> strings = new HashMap<>();
    HashMap<String,Boolean> booleans = new HashMap<>();
    HashMap<String,Integer> setInts = new HashMap<>();
    HashMap<String,Integer> addIntsMin = new HashMap<>();
    HashMap<String,Integer> addIntsMax = new HashMap<>();
    @SneakyThrows
    public DialogAddon_setMemoryData(JSONObject json){
        for (Iterator it = json.keys(); it.hasNext(); ) {
            String key2 = (String) it.next();
            if (json.get(key2) instanceof JSONObject){
                JSONObject a = json.getJSONObject(key2);
                addIntsMin.put(key2,a.getInt("min"));
                addIntsMax.put(key2,a.getInt("max"));
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
            if (json.get(key2) instanceof Integer){
                int a = json.getInt(key2);
                setInts.put(key2,a);
                continue;
            }
            //this is bad...
        }
    }
    @Override
    public void apply(TextPanelAPI textPanel, OptionPanelAPI options, InteractionDialogAPI dialog, Lord lord) {
        applyStrings(dialog, lord);
        applyBooleans(dialog, lord);
        applyFloats(dialog, lord);
        applyAddFloats(dialog, lord);
    }
    public void applyStrings(InteractionDialogAPI dialog, Lord lord){
        for (String key : strings.keySet()) {
            Global.getSector().getMemory().set(key,strings.get(key));
        }
    }
    public void applyBooleans(InteractionDialogAPI dialog, Lord lord){
        for (String key : booleans.keySet()) {
            Global.getSector().getMemory().set(key,booleans.get(key));
        }

    }
    public void applyFloats(InteractionDialogAPI dialog, Lord lord){
        for (String key : setInts.keySet()) {
            Global.getSector().getMemory().set(key,setInts.get(key));
        }
    }
    public void applyAddFloats(InteractionDialogAPI dialog, Lord lord){
        for (String key : addIntsMin.keySet()) {
            int min = addIntsMin.get(key);
            int max = addIntsMax.get(key);
            int baseValue = Global.getSector().getMemory().getInt(key);
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
            Global.getSector().getMemory().set(key,data);
        }
    }
}
