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

public class DialogAddon_setMemoryData extends DialogAddon_setDialogData{
    public DialogAddon_setMemoryData(JSONObject json){
        super(json);
    }
    @Override
    public void applyStrings(InteractionDialogAPI dialog, Lord lord,Lord targetLord){
        for (String key : strings.keySet()) {
            Global.getSector().getMemory().set(key,strings.get(key));
        }
    }
    @Override
    public void applyBooleans(InteractionDialogAPI dialog, Lord lord,Lord targetLord){
        for (String key : booleans.keySet()) {
            Global.getSector().getMemory().set(key,booleans.get(key));
        }

    }
    @Override
    public void applyFloats(InteractionDialogAPI dialog, Lord lord,Lord targetLord){
        for (String key : setInts.keySet()) {
            Global.getSector().getMemory().set(key,setInts.get(key));
        }
    }
    @Override
    public void applyAddFloats(InteractionDialogAPI dialog, Lord lord,Lord targetLord){
        for (String key : addIntsMin.keySet()) {
            int min = addIntsMin.get(key).getValue(lord, targetLord);
            int max = addIntsMax.get(key).getValue(lord, targetLord);
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
