package starlords.util.dialogControler.dialogRull;

import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.controllers.RelationController;
import starlords.person.Lord;
import starlords.util.dialogControler.dialogRull.bases.DialogRule_minmax;

public class DialogRule_lordLoyalty extends DialogRule_minmax {
    @SneakyThrows
    public DialogRule_lordLoyalty(JSONObject jsonObject,String key){
        super(jsonObject, key);
    }

    @Override
    protected int getValue(Lord lord, Lord targetLord) {
        int rel = RelationController.getLoyalty(lord);
        return rel;
    }
}
