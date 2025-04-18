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
    public DialogValuesList(JSONObject json){
        values = new ArrayList<>();
        if (json.has("base")) base = json.getInt("base");
        if (json.has("multi")) multi = json.getInt("multi");
        for (Iterator it2 = json.keys(); it2.hasNext();) {
            String key = (String) it2.next();
            switch (key){
                case "relationWithPlayer":
                    values.add(new DialogValue_relationWithPlayer(json,key));
                    break;
                case "lordLoyalty":
                    values.add(new DialogValue_lordLoyalty(json,key));
                    break;
                case "playerWealth":
                    values.add(new DialogValue_playerWealth(json,key));
                    break;
                case "lordWealth":
                    values.add(new DialogValue_lordWealth(json,key));
                    break;
                case "playerLevel":
                    values.add(new DialogValue_playerLevel(json,key));
                    break;
                case "lordLevel":
                    values.add(new DialogValue_lordLevel(json,key));
                    break;
                case "playerRank":
                    values.add(new DialogValue_playerRank(json,key));
                    break;
                case "lordRank":
                    values.add(new DialogValue_lordRank(json,key));
                    break;
                case "playerLordRomanceAction":
                    values.add(new DialogValue_playerLordRomanceAction(json,key));
                    break;
                case "lordsInFeast":
                    values.add(new DialogValue_lordsInFeast(json,key));
                    break;
                case "lordProposalSupporters":
                    values.add(new DialogValue_lordProposalSupporters(json,key));
                    break;
                case "lordProposalOpposers":
                    values.add(new DialogValue_lordProposalOpposers(json,key));
                    break;
                case "playerProposalSupporters":
                    values.add(new DialogValue_playerProposalSupporters(json,key));
                    break;
                case "playerProposalOpposers":
                    values.add(new DialogValue_playerProposalOpposers(json,key));
                    break;
                case "curProposalSupporters":
                    values.add(new DialogValue_curProposalSupporters(json,key));
                    break;
                case "curProposalOpposers":
                    values.add(new DialogValue_curProposalOpposers(json,key));
                    break;
                case "optionOfCurrProposal":
                    values.add(new DialogValue_optionOfCurrProposal(json,key));
                    break;
                case "optionOfPlayerProposal":
                    values.add(new DialogValue_optionOfPlayerProposal(json,key));
                    break;
                case "validLordNumbers":
                    values.add(new DialogValue_validLordNumbers(json,key));
                    break;
                case "conditionalValue":
                    if (json.get(key) instanceof JSONArray){
                        values.add(new DialogValue_conditionalValue_Array(json, key));
                    }else {
                        values.add(new DialogValue_conditionalValue(json, key));
                    }
                    break;
                case "DialogData":
                    values.add(new DialogValue_DialogData_list(json,key));
                    break;
                case "MemoryData":
                    values.add(new DialogValue_MemoryData_list(json,key));
                    break;
                case "LordMemoryData":
                    values.add(new DialogValue_LordMemoryData_list(json,key));
                    break;
            }
        }
    }
    public DialogValuesList(JSONObject json,int base){
        this(json);
        this.base = base;
    }
    public int getValue(Lord lord, Lord targetLord){
        int base = this.base;
        for (DialogValue_base a : values){
            base+=a.computeValue(lord,targetLord);
        }
        return (int) (base*multi);
    }
}
