package starlords.util.dialogControler.dialogRull;

import com.fs.starfarer.api.Global;
import lombok.SneakyThrows;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import starlords.lunaSettings.StoredSettings;
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
        Logger log = Global.getLogger(StoredSettings.class);
        log.info("getting floats in dialog data from key" +key+" as: "+LordInteractionDialogPluginImpl.DATA_HOLDER.getIntegers().get(key));
        int rel = 0;
        if (LordInteractionDialogPluginImpl.DATA_HOLDER.getIntegers().containsKey(key)) rel = LordInteractionDialogPluginImpl.DATA_HOLDER.getIntegers().get(key);
        return rel;
    }
}
