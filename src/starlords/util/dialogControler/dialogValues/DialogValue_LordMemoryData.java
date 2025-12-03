package starlords.util.dialogControler.dialogValues;

import org.json.JSONObject;
import starlords.person.Lord;
import starlords.util.memoryUtils.DataHolder;

public class DialogValue_LordMemoryData extends DialogValue_base{
    String key;
    public DialogValue_LordMemoryData(JSONObject json, String key) {
        super(json, key);
        this.key=key;
    }

    @Override
    public int value(Lord lord, Lord targetLord) {
        DataHolder DATA_HOLDER = lord.getDataHolder();
        return DATA_HOLDER.getDouble(this.key);
    }

}
