package starlords.util.dialogControler.dialogRull;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import starlords.lunaSettings.StoredSettings;
import starlords.person.Lord;
import starlords.util.dialogControler.dialogRull.bases.DialogRule_minmax;

public class DialogRule_marketFactionRelationToTargetLord extends DialogRule_minmax {
    public DialogRule_marketFactionRelationToTargetLord(JSONObject jsonObject, String key) {
        super(jsonObject, key);
    }

    @Override
    protected int getValue(Lord lord, Lord targetLord, MarketAPI targetMarket) {
        Logger log = Global.getLogger(StoredSettings.class);
        int temp = (int) lord.getFaction().getRelationship(targetMarket.getFactionId())*100;
        log.info("got relationship between market: "+targetMarket.getName()+", of faction"+targetMarket.getFaction().getDisplayName()+" and lord of faction "+lord.getFaction().getDisplayName()+" as: "+ temp);
        return (int) lord.getFaction().getRelationship(targetMarket.getFaction().getId())*100;
    }
}
