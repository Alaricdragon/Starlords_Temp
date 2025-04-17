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
import starlords.util.dialogControler.DialogDataHolder;
import starlords.util.memoryUtils.DataHolder;

import java.util.HashMap;
import java.util.Iterator;

import static starlords.util.Constants.STARLORD_ADDITIONAL_MEMORY_KEY;

public class DialogAddon_setLordMemoryData extends DialogAddon_Base{
    HashMap<String,String> strings = new HashMap<>();
    HashMap<String,Boolean> booleans = new HashMap<>();
    HashMap<String,Integer> setInts = new HashMap<>();
    HashMap<String,Integer> addIntsMin = new HashMap<>();
    HashMap<String,Integer> addIntsMax = new HashMap<>();
    @SneakyThrows
    public DialogAddon_setLordMemoryData(JSONObject json){
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
        String key = STARLORD_ADDITIONAL_MEMORY_KEY+lord.getLordAPI().getId();
        DataHolder DATA_HOLDER;
        if (Global.getSector().getMemory().contains(key)){
            DATA_HOLDER = (DataHolder) Global.getSector().getMemory().get(key);
        }else{
            DATA_HOLDER = new DataHolder();
        }
        applyStrings(DATA_HOLDER);
        applyBooleans(DATA_HOLDER);
        applyFloats(DATA_HOLDER);
        applyAddFloats(DATA_HOLDER);
        Global.getSector().getMemory().set(key,DATA_HOLDER);
    }
    public void applyStrings(DataHolder DATA_HOLDER){
        for (String key : strings.keySet()) {
            DATA_HOLDER.getStrings().put(key,strings.get(key));
        }
    }
    public void applyBooleans(DataHolder DATA_HOLDER){
        for (String key : booleans.keySet()) {
            DATA_HOLDER.getBooleans().put(key,booleans.get(key));
        }

    }
    public void applyFloats(DataHolder DATA_HOLDER){
        for (String key : setInts.keySet()) {
            DATA_HOLDER.getIntegers().put(key, setInts.get(key));
        }
    }
    public void applyAddFloats(DataHolder DATA_HOLDER){
        for (String key : addIntsMin.keySet()) {
            int min = addIntsMin.get(key);
            int max = addIntsMax.get(key);
            int baseValue = DATA_HOLDER.getIntegers().get(key);
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
            DATA_HOLDER.getIntegers().put(key,data);
        }
    }
}
