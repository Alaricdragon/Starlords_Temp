package starlords.util.dialogControler.dialogValues;

import com.fs.starfarer.api.Global;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import starlords.lunaSettings.StoredSettings;
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
        Logger log = Global.getLogger(StoredSettings.class);
        if (LordInteractionDialogPluginImpl.DATA_HOLDER.getIntegers().containsKey(key)) rel = LordInteractionDialogPluginImpl.DATA_HOLDER.getIntegers().get(key);
        log.info("  getting data of a key of :"+key+" and a value of "+rel);
        return rel;
    }

}
