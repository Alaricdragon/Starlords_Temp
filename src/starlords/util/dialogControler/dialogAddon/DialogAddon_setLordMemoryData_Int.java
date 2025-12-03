package starlords.util.dialogControler.dialogAddon;

import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.OptionPanelAPI;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import org.json.JSONObject;
import starlords.person.Lord;
import starlords.util.memoryUtils.DataHolder;

public class DialogAddon_setLordMemoryData_Int extends DialogAddon_setDialogData_Int{
    public DialogAddon_setLordMemoryData_Int(String key, JSONObject json) {
        super(key, json);
    }

    @Override
    protected int getCurrentValue(TextPanelAPI textPanel, OptionPanelAPI options, InteractionDialogAPI dialog, Lord lord, Lord targetLord, MarketAPI targetMarket) {
        DataHolder DATA_HOLDER = lord.getDataHolder();
        return DATA_HOLDER.getDouble(key);
    }

    @Override
    protected void change(int value, TextPanelAPI textPanel, OptionPanelAPI options, InteractionDialogAPI dialog, Lord lord, Lord targetLord, MarketAPI targetMarket) {
        DataHolder DATA_HOLDER = lord.getDataHolder();
        value = DATA_HOLDER.getDouble(key)+value;
        if (time != null) {
            DATA_HOLDER.setDouble(key,value,time.getValue(lord, targetLord, targetMarket));
            return;
        }
        DATA_HOLDER.setDouble(key,value);
    }
}
