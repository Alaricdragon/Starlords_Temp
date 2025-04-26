package starlords.util.dialogControler.dialog_addon;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.OptionPanelAPI;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
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

public class DialogAddon_setLordMemoryData extends DialogAddon_setDialogData{
    public DialogAddon_setLordMemoryData(JSONObject json){
        super(json);
    }
    @Override
    public void apply(TextPanelAPI textPanel, OptionPanelAPI options, InteractionDialogAPI dialog, Lord lord,Lord targetLord, MarketAPI targetMarket) {
        String key = STARLORD_ADDITIONAL_MEMORY_KEY+lord.getLordAPI().getId();
        DataHolder DATA_HOLDER;
        if (Global.getSector().getMemory().contains(key)){
            DATA_HOLDER = (DataHolder) Global.getSector().getMemory().get(key);
        }else{
            DATA_HOLDER = new DataHolder();
        }
        applyStrings(DATA_HOLDER,lord,targetLord);
        applyBooleans(DATA_HOLDER,lord,targetLord);
        applyFloats(DATA_HOLDER,lord,targetLord,targetMarket);
        applyAddFloats(DATA_HOLDER,lord,targetLord,targetMarket);
        Global.getSector().getMemory().set(key,DATA_HOLDER);
    }
    public void applyStrings(DataHolder DATA_HOLDER,Lord lord,Lord targetLord){
        for (String key : strings.keySet()) {
            DATA_HOLDER.getStrings().put(key,strings.get(key));
        }
    }
    public void applyBooleans(DataHolder DATA_HOLDER,Lord lord,Lord targetLord){
        for (String key : booleans.keySet()) {
            DATA_HOLDER.getBooleans().put(key,booleans.get(key));
        }

    }
    public void applyFloats(DataHolder DATA_HOLDER,Lord lord,Lord targetLord, MarketAPI targetMarket){
        for (String key : setInts.keySet()) {
            DATA_HOLDER.getIntegers().put(key, setInts.get(key).getValue(lord, targetLord,targetMarket));
        }
    }
    public void applyAddFloats(DataHolder DATA_HOLDER,Lord lord,Lord targetLord, MarketAPI targetMarket){
        for (String key : addIntsMin.keySet()) {
            int min = addIntsMin.get(key).getValue(lord, targetLord,targetMarket);
            int max = addIntsMax.get(key).getValue(lord, targetLord,targetMarket);
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
