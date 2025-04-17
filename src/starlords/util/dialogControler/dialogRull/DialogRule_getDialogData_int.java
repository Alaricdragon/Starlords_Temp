package starlords.util.dialogControler.dialogRull;

import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.person.Lord;
import starlords.plugins.LordInteractionDialogPluginImpl;
import starlords.util.dialogControler.dialogRull.bases.DialogRule_minmax;

public class DialogRule_getDialogData_int extends DialogRule_minmax {
    String key;
    @SneakyThrows
    public DialogRule_getDialogData_int(String key,JSONObject jsonObject){
        super(jsonObject, key);
        this.key = key;
    }
    @Override
    protected int getValue(Lord lord,Lord targetLord){
        int rel = 0;
        if (LordInteractionDialogPluginImpl.DATA_HOLDER.getIntegers().containsKey(key)) rel = LordInteractionDialogPluginImpl.DATA_HOLDER.getIntegers().get(key);
        return rel;
    }
}
