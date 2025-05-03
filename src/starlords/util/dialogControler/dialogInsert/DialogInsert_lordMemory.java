package starlords.util.dialogControler.dialogInsert;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.person.Lord;
import starlords.plugins.LordInteractionDialogPluginImpl;
import starlords.util.memoryUtils.DataHolder;

import static starlords.util.Constants.STARLORD_ADDITIONAL_MEMORY_KEY;

public class DialogInsert_lordMemory extends DialogInsert_Base{
    String key2;
    @SneakyThrows
    public DialogInsert_lordMemory(JSONObject json){
        key = json.getString("replaced");
        key2 = json.getString("lordMemory");
    }

    @Override
    public String getInsertedData(String line, Lord lord, Lord targetLord, MarketAPI targetMarket) {
        String key = STARLORD_ADDITIONAL_MEMORY_KEY+lord.getLordAPI().getId();
        DataHolder DATA_HOLDER;
        if (Global.getSector().getMemory().contains(key)){
            DATA_HOLDER = (DataHolder) Global.getSector().getMemory().get(key);
        }else{
            DATA_HOLDER = new DataHolder();
        }
        String out = "";
        out = DATA_HOLDER.getString(this.key);
        return out;
    }
}
