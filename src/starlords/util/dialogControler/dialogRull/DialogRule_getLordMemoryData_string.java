package starlords.util.dialogControler.dialogRull;

import org.json.JSONObject;
import starlords.person.Lord;
import starlords.util.memoryUtils.DataHolder;

public class DialogRule_getLordMemoryData_string extends DialogRule_getDialogData_string{
    public DialogRule_getLordMemoryData_string(String key, JSONObject json) {
        super(key, json);
    }

    @Override
    protected String getString(Lord lord) {
        DataHolder DATA_HOLDER = lord.getDataHolder();
        String out = "";
        out = DATA_HOLDER.getString(this.key);
        return out;
    }
}
