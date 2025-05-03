package starlords.util.dialogControler.dialogRull;

import com.fs.starfarer.api.Global;
import starlords.person.Lord;
import starlords.util.memoryUtils.DataHolder;

import static starlords.util.Constants.STARLORD_ADDITIONAL_MEMORY_KEY;

public class DialogRule_getLordMemoryData_boolean extends DialogRule_getDialogData_boolean{
    public DialogRule_getLordMemoryData_boolean(String key, boolean feast) {
        super(key, feast);
    }

    @Override
    protected boolean getBoolean(Lord lord) {
        String key = STARLORD_ADDITIONAL_MEMORY_KEY+lord.getLordAPI().getId();
        DataHolder DATA_HOLDER;
        if (Global.getSector().getMemory().contains(key)){
            DATA_HOLDER = (DataHolder) Global.getSector().getMemory().get(key);
        }else{
            DATA_HOLDER = new DataHolder();
        }

        boolean out = false;
        out = DATA_HOLDER.getBoolean(this.key);
        return out;
    }
}
