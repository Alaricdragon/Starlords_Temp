package starlords.util.dialogControler.dialogValues;

import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;
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
        if (!(json.get(key) instanceof JSONObject)) base = json.getInt(key);
        if (json.has("base")) base = json.getInt("base");
        if (json.has("multi")) multi = json.getInt("multi");
        for (Iterator it2 = json.keys(); it2.hasNext();) {
            String key2 = (String) it2.next();
            switch (key2){
                case "relationWithPlayer":
                    values.add(new DialogValue_relationWithPlayer(json,key2));
                    break;
                case "lordLoyalty":
                    values.add(new DialogValue_lordLoyalty(json,key2));
                    break;
                case "lordLoyaltyToPlayerLord":
                    values.add(new DialogValue_lordLoyaltyToPlayerLord(json,key2));
                    break;
                case "playerWealth":
                    values.add(new DialogValue_playerWealth(json,key2));
                    break;
                case "lordWealth":
                    values.add(new DialogValue_lordWealth(json,key2));
                    break;
                case "playerLevel":
                    values.add(new DialogValue_playerLevel(json,key2));
                    break;
                case "lordLevel":
                    values.add(new DialogValue_lordLevel(json,key2));
                    break;
                case "playerFleetDP":
                    values.add(new DialogValue_playerFleetDP(json,key2));
                    break;
                case "lordFleetDP":
                    values.add(new DialogValue_lordFleetDP(json,key2));
                    break;
                case "playerRank":
                    values.add(new DialogValue_playerRank(json,key2));
                    break;
                case "lordRank":
                    values.add(new DialogValue_lordRank(json,key2));
                    break;
                case "playerLordRomanceAction":
                    values.add(new DialogValue_playerLordRomanceAction(json,key2));
                    break;
                case "lordsInFeast":
                    values.add(new DialogValue_lordsInFeast(json,key2));
                    break;
                case "lordProposalSupporters":
                    values.add(new DialogValue_lordProposalSupporters(json,key2));
                    break;
                case "lordProposalOpposers":
                    values.add(new DialogValue_lordProposalOpposers(json,key2));
                    break;
                case "playerProposalSupporters":
                    values.add(new DialogValue_playerProposalSupporters(json,key2));
                    break;
                case "playerProposalOpposers":
                    values.add(new DialogValue_playerProposalOpposers(json,key2));
                    break;
                case "curProposalSupporters":
                    values.add(new DialogValue_curProposalSupporters(json,key2));
                    break;
                case "curProposalOpposers":
                    values.add(new DialogValue_curProposalOpposers(json,key2));
                    break;
                case "optionOfCurrProposal":
                    values.add(new DialogValue_optionOfCurrProposal(json,key2));
                    break;
                case "optionOfPlayerProposal":
                    values.add(new DialogValue_optionOfPlayerProposal(json,key2));
                    break;
                case "playerMarketNumbers":
                    values.add(new DialogValue_playerMarketNumbers(json,key2));
                    break;
                case "lordMarketNumbers":
                    values.add(new DialogValue_lordMarketNumbers(json,key2));
                    break;
                case "playerCommissionedMarketNumbers":
                    values.add(new DialogValue_playerCommissionedMarketNumbers(json,key2));
                    break;
                case "playerMarketAverageStability":
                    values.add(new DialogValue_playerMarketAverageStability(json,key2));
                    break;
                case "lordMarketAverageStability":
                    values.add(new DialogValue_lordMarketAverageStability(json,key2));
                    break;
                case "playerCommissionedMarketAverageStability":
                    values.add(new DialogValue_playerCommissionedMarketAverageStability(json,key2));
                    break;
                case "validLordNumbers":
                    if (json.get(key2) instanceof JSONArray){
                        values.add(new DialogValue_validLordNumbers_Array(json, key2));
                    }else {
                        values.add(new DialogValue_validLordNumbers(json, key2));
                    }
                    break;
                case "limitedValue":
                    if (json.get(key2) instanceof JSONArray){
                        values.add(new DialogValue_limitedValue_Array(json, key2));
                    }else {
                        values.add(new DialogValue_limitedValue(json, key2));
                    }
                    break;
                case "conditionalValue":
                    if (json.get(key2) instanceof JSONArray){
                        values.add(new DialogValue_conditionalValue_Array(json, key2));
                    }else {
                        values.add(new DialogValue_conditionalValue(json, key2));
                    }
                    break;
                case "DialogData":
                    values.add(new DialogValue_DialogData_list(json,key2));
                    break;
                case "MemoryData":
                    values.add(new DialogValue_MemoryData_list(json,key2));
                    break;
                case "LordMemoryData":
                    values.add(new DialogValue_LordMemoryData_list(json,key2));
                    break;
            }
        }
    }
    public int getValue(Lord lord, Lord targetLord){
        int base = this.base;
        for (DialogValue_base a : values){
            base+=a.computeValue(lord,targetLord);
        }
        return (int) (base*multi);
    }
}
