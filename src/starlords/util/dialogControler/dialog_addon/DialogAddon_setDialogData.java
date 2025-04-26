package starlords.util.dialogControler.dialog_addon;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.OptionPanelAPI;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import lombok.SneakyThrows;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import starlords.lunaSettings.StoredSettings;
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
            if (json.get(key2) instanceof JSONObject){
                setInts.put(key2,new DialogValuesList(json,key2));
                continue;
            }
            boolean go = true;
            try{
                json.getInt(key2);
            }catch (Exception e){
                go = false;
            }
            if (go){
                setInts.put(key2,new DialogValuesList(json,key2));
                continue;
            }

            go = true;
            try {
                json.getBoolean(key2);
            }catch (Exception e){
                go = false;
            }
            if (go){
                boolean a = json.getBoolean(key2);
                booleans.put(key2,a);
                continue;

            }
            go = true;
            try {
                json.getString(key2);
            }catch (Exception e){
                go = false;
            }
            if (go){
                String a = json.getString(key2);
                strings.put(key2,a);
                continue;
            }
        }
    }
    @Override
    public void apply(TextPanelAPI textPanel, OptionPanelAPI options, InteractionDialogAPI dialog, Lord lord,Lord targetLord, MarketAPI targetMarket) {
        Logger log = Global.getLogger(StoredSettings.class);
        log.info("applying dialog data...");
        applyStrings(dialog, lord,targetLord);
        applyBooleans(dialog, lord,targetLord);
        applyFloats(dialog, lord,targetLord,targetMarket);
        applyAddFloats(dialog, lord,targetLord,targetMarket);

    }
    public void applyStrings(InteractionDialogAPI dialog, Lord lord,Lord targetLord){
        Logger log = Global.getLogger(StoredSettings.class);
        log.info("setting strings in dialog data...");
        for (String key : strings.keySet()) {
            log.info("  got key as: "+key);
            LordInteractionDialogPluginImpl.DATA_HOLDER.getStrings().put(key,strings.get(key));
        }
    }
    public void applyBooleans(InteractionDialogAPI dialog, Lord lord,Lord targetLord){
        Logger log = Global.getLogger(StoredSettings.class);
        log.info("seting booleans in dialog data...");
        for (String key : booleans.keySet()) {
            log.info("  got key as: "+key);
            LordInteractionDialogPluginImpl.DATA_HOLDER.getBooleans().put(key,booleans.get(key));
            log.info("  got new saved data as: "+LordInteractionDialogPluginImpl.DATA_HOLDER.getBooleans().get(key));
        }

    }
    public void applyFloats(InteractionDialogAPI dialog, Lord lord,Lord targetLord, MarketAPI targetMarket){
        Logger log = Global.getLogger(StoredSettings.class);
        log.info("HERE: setting floats in dialog data...");
        for (String key : setInts.keySet()) {
            LordInteractionDialogPluginImpl.DATA_HOLDER.getIntegers().put(key, setInts.get(key).getValue(lord,targetLord,targetMarket));
            log.info("  HERE: got new saved data as for "+key+": as: "+LordInteractionDialogPluginImpl.DATA_HOLDER.getIntegers().get(key));
        }
    }
    public void applyAddFloats(InteractionDialogAPI dialog, Lord lord,Lord targetLord, MarketAPI targetMarket){
        Logger log = Global.getLogger(StoredSettings.class);
        log.info("adding floats in dialog data...");
        for (String key : addIntsMin.keySet()) {
            log.info("  got key as: "+key);
            int min = addIntsMin.get(key).getValue(lord,targetLord,targetMarket);
            int max = addIntsMax.get(key).getValue(lord,targetLord,targetMarket);
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
            log.info("  got new saved data as: "+LordInteractionDialogPluginImpl.DATA_HOLDER.getIntegers().get(key));
        }
    }
}
