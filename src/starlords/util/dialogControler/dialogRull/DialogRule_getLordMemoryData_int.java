package starlords.util.dialogControler.dialogRull;

import com.fs.starfarer.api.campaign.econ.MarketAPI;
import org.json.JSONObject;
import starlords.person.Lord;
import starlords.util.memoryUtils.DataHolder;

public class DialogRule_getLordMemoryData_int extends DialogRule_getDialogData_int{
    public DialogRule_getLordMemoryData_int(String key, JSONObject jsonObject) {
        super(key, jsonObject);
    }

    @Override
    protected int getValue(Lord lord,Lord targetLord, MarketAPI targetMarket){
        DataHolder DATA_HOLDER = lord.getDataHolder();
        int out = 0;
        out = DATA_HOLDER.getDouble(this.key);
        return out;
    }
}
