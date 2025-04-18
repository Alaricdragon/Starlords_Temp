package starlords.util.dialogControler.dialogRull;

import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.person.Lord;
import starlords.util.dialogControler.dialogRull.bases.DialogRule_minmax;

public class DialogRule_relationWithPlayer extends DialogRule_minmax {
    @SneakyThrows
    public DialogRule_relationWithPlayer(JSONObject jsonObject,String key){
        super(jsonObject, key);
    }

    @Override
    protected int getValue(Lord lord, Lord targetLord) {
        return lord.getPlayerRel();
    }
}
