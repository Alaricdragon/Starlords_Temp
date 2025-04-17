package starlords.util.dialogControler.dialogValues;

import org.json.JSONObject;
import starlords.person.Lord;
import starlords.plugins.LordInteractionDialogPluginImpl;

public class DialogValue_DialogData extends DialogValue_base{
    String key;
    public DialogValue_DialogData(JSONObject json, String key) {
        super(json, key);
        this.key = key;
    }

    @Override
    public int value(Lord lord, Lord targetLord) {
        int rel = 0;
        if (LordInteractionDialogPluginImpl.DATA_HOLDER.getIntegers().containsKey(key)) rel = LordInteractionDialogPluginImpl.DATA_HOLDER.getIntegers().get(key);
        return rel;
    }

}
