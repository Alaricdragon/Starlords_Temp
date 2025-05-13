package starlords.util.dialogControler.dialogRull;

import com.fs.starfarer.api.campaign.econ.MarketAPI;
import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.person.Lord;
import starlords.util.Utils;

public class DialogRule_marketIsValidTarget extends DialogRule_Base{
    boolean bol;
    @SneakyThrows
    public DialogRule_marketIsValidTarget(JSONObject json, String key){
        bol = json.getBoolean(key);
    }

    @Override
    public boolean condition(Lord lord, Lord targetLord, MarketAPI market) {
        return Utils.canBeAttacked(market);
    }
}
