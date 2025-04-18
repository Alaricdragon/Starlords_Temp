package starlords.util.dialogControler.dialogRull;

import com.fs.starfarer.api.Global;
import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.person.Lord;
import starlords.util.dialogControler.dialogRull.bases.DialogRule_minmax;

public class DialogRule_playerWealth extends DialogRule_minmax {
    @SneakyThrows
    public DialogRule_playerWealth(JSONObject jsonObject,String key){
        super(jsonObject, key);
    }

    @Override
    protected int getValue(Lord lord, Lord targetLord) {
        int playerCredits = (int) Global.getSector().getPlayerFleet().getCargo().getCredits().get();
        return playerCredits;
    }
}
