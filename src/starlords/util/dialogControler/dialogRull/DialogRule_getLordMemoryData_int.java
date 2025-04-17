package starlords.util.dialogControler.dialogRull;

import com.fs.starfarer.api.Global;
import org.json.JSONObject;
import starlords.person.Lord;
import starlords.util.memoryUtils.DataHolder;

import static starlords.util.Constants.STARLORD_ADDITIONAL_MEMORY_KEY;

public class DialogRule_getLordMemoryData_int extends DialogRule_getDialogData_int{
    public DialogRule_getLordMemoryData_int(String key, JSONObject jsonObject) {
        super(key, jsonObject);
    }

    @Override
    protected int getValue(Lord lord,Lord targetLord){
        String key = STARLORD_ADDITIONAL_MEMORY_KEY+lord.getLordAPI().getId();
        DataHolder DATA_HOLDER;
        if (Global.getSector().getMemory().contains(key)){
            DATA_HOLDER = (DataHolder) Global.getSector().getMemory().get(key);
        }else{
            DATA_HOLDER = new DataHolder();
        }
        int out = 0;
        if (DATA_HOLDER.getIntegers().containsKey(this.key)) out = DATA_HOLDER.getIntegers().get(this.key);
        return out;
    }
}
