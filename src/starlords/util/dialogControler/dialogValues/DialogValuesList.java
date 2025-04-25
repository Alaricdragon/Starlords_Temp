package starlords.util.dialogControler.dialogValues;

import com.fs.starfarer.api.Global;
import lombok.SneakyThrows;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import starlords.lunaSettings.StoredSettings;
import starlords.person.Lord;

import java.util.ArrayList;
import java.util.Iterator;

public class DialogValuesList {
    ArrayList<DialogValue_base> values;
    int base = 0;
    double multi=1;
    @SneakyThrows
    public DialogValuesList(JSONObject json,String key){
        values = new ArrayList<>();
        if (!(json.get(key) instanceof JSONObject)){
            base = json.getInt(key);
            return;
        }
        if (json.has("base")) base = json.getInt("base");
        if (json.has("multi")) multi = json.getDouble("multi");
        for (Iterator it2 = json.getJSONObject(key).keys(); it2.hasNext();) {
            String key2 = (String) it2.next();
            switch (key2){
                case "relationWithPlayer":
                    values.add(new DialogValue_relationWithPlayer(json.getJSONObject(key),key2));
                    break;
                case "lordLoyalty":
                    values.add(new DialogValue_lordLoyalty(json.getJSONObject(key),key2));
                    break;
                case "lordLoyaltyToPlayerLord":
                    values.add(new DialogValue_lordLoyaltyToPlayerLord(json.getJSONObject(key),key2));
                    break;
                case "playerWealth":
                    values.add(new DialogValue_playerWealth(json.getJSONObject(key),key2));
                    break;
                case "lordWealth":
                    values.add(new DialogValue_lordWealth(json.getJSONObject(key),key2));
                    break;
                case "playerLevel":
                    values.add(new DialogValue_playerLevel(json.getJSONObject(key),key2));
                    break;
                case "lordLevel":
                    values.add(new DialogValue_lordLevel(json.getJSONObject(key),key2));
                    break;
                case "playerFleetDP":
                    values.add(new DialogValue_playerFleetDP(json.getJSONObject(key),key2));
                    break;
                case "lordFleetDP":
                    values.add(new DialogValue_lordFleetDP(json.getJSONObject(key),key2));
                    break;
                case "playerRank":
                    values.add(new DialogValue_playerRank(json.getJSONObject(key),key2));
                    break;
                case "lordRank":
                    values.add(new DialogValue_lordRank(json.getJSONObject(key),key2));
                    break;
                case "playerLordRomanceAction":
                    values.add(new DialogValue_playerLordRomanceAction(json.getJSONObject(key),key2));
                    break;
                case "lordsInFeast":
                    values.add(new DialogValue_lordsInFeast(json.getJSONObject(key),key2));
                    break;
                case "lordProposalSupporters":
                    values.add(new DialogValue_lordProposalSupporters(json.getJSONObject(key),key2));
                    break;
                case "lordProposalOpposers":
                    values.add(new DialogValue_lordProposalOpposers(json.getJSONObject(key),key2));
                    break;
                case "playerProposalSupporters":
                    values.add(new DialogValue_playerProposalSupporters(json.getJSONObject(key),key2));
                    break;
                case "playerProposalOpposers":
                    values.add(new DialogValue_playerProposalOpposers(json.getJSONObject(key),key2));
                    break;
                case "curProposalSupporters":
                    values.add(new DialogValue_curProposalSupporters(json.getJSONObject(key),key2));
                    break;
                case "curProposalOpposers":
                    values.add(new DialogValue_curProposalOpposers(json.getJSONObject(key),key2));
                    break;
                case "optionOfCurrProposal":
                    values.add(new DialogValue_optionOfCurrProposal(json.getJSONObject(key),key2));
                    break;
                case "optionOfPlayerProposal":
                    values.add(new DialogValue_optionOfPlayerProposal(json.getJSONObject(key),key2));
                    break;
                case "playerMarketNumbers":
                    values.add(new DialogValue_playerMarketNumbers(json.getJSONObject(key),key2));
                    break;
                case "lordMarketNumbers":
                    values.add(new DialogValue_lordMarketNumbers(json.getJSONObject(key),key2));
                    break;
                case "playerCommissionedMarketNumbers":
                    values.add(new DialogValue_playerCommissionedMarketNumbers(json.getJSONObject(key),key2));
                    break;
                case "playerMarketAverageStability":
                    values.add(new DialogValue_playerMarketAverageStability(json.getJSONObject(key),key2));
                    break;
                case "lordMarketAverageStability":
                    values.add(new DialogValue_lordMarketAverageStability(json.getJSONObject(key),key2));
                    break;
                case "playerCommissionedMarketAverageStability":
                    values.add(new DialogValue_playerCommissionedMarketAverageStability(json.getJSONObject(key),key2));
                    break;
                case "validLordNumbers":
                    if (json.getJSONObject(key).get(key2) instanceof JSONArray){
                        values.add(new DialogValue_validLordNumbers_Array(json.getJSONObject(key), key2));
                    }else {
                        values.add(new DialogValue_validLordNumbers(json.getJSONObject(key), key2));
                    }
                    break;
                case "limitedValue":
                    if (json.getJSONObject(key).get(key2) instanceof JSONArray){
                        values.add(new DialogValue_limitedValue_Array(json.getJSONObject(key), key2));
                    }else {
                        values.add(new DialogValue_limitedValue(json.getJSONObject(key).getJSONObject(key2)));
                    }
                    break;
                case "conditionalValue":
                    if (json.getJSONObject(key).get(key2) instanceof JSONArray){
                        values.add(new DialogValue_conditionalValue_Array(json.getJSONObject(key), key2));
                    }else {
                        values.add(new DialogValue_conditionalValue(json.getJSONObject(key), key2));
                    }
                    break;
                case "DialogData":
                    values.add(new DialogValue_DialogData_list(json.getJSONObject(key),key2));
                    break;
                case "MemoryData":
                    values.add(new DialogValue_MemoryData_list(json.getJSONObject(key),key2));
                    break;
                case "LordMemoryData":
                    values.add(new DialogValue_LordMemoryData_list(json.getJSONObject(key),key2));
                    break;
                case "random":
                    values.add(new DialogValue_random(json.getJSONObject(key),key2));
                    break;
            }
        }
    }
    public int getValue(Lord lord, Lord targetLord){
        Logger log = Global.getLogger(StoredSettings.class);
        log.info("  getting dialog value list value....");
        int base = this.base;
        log.info("      added base as: "+base);
        for (DialogValue_base a : values){
            int temp = a.computeValue(lord,targetLord);
            base+=temp;
            log.info("      adding "+temp+" from source of "+a.getClass().getName()+" for a new total of "+base);
        }
        return (int) (base*multi);
    }
}
