package starlords.util.dialogControler.dialogRull;

import com.fs.starfarer.api.Global;
import org.json.JSONObject;
import starlords.person.Lord;
import starlords.util.memoryUtils.DataHolder;

import static starlords.util.Constants.STARLORD_ADDITIONAL_MEMORY_KEY;

public class DialogRule_getLordMemoryData_string extends DialogRule_getDialogData_string{
    public DialogRule_getLordMemoryData_string(String key, JSONObject json) {
        super(key, json);
    }

    @Override
    protected String getString(Lord lord) {
        String key = STARLORD_ADDITIONAL_MEMORY_KEY+lord.getLordAPI().getId();
        DataHolder DATA_HOLDER;
        if (Global.getSector().getMemory().contains(key)){
            DATA_HOLDER = (DataHolder) Global.getSector().getMemory().get(key);
        }else{
            DATA_HOLDER = new DataHolder();
        }
        String out = "";
        if (DATA_HOLDER.getStrings().containsKey(this.key)) out = DATA_HOLDER.getStrings().get(this.key);
        return out;
    }
}
