package starlords.util.dialogControler.dialogRull;

import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.controllers.PoliticsController;
import starlords.faction.LawProposal;
import starlords.person.Lord;
import starlords.util.Utils;
import starlords.util.dialogControler.dialogRull.randoms.DialogRule_random_base;
import starlords.util.dialogControler.dialogRull.randoms.DialogRule_random_opinionOfCurrProposal;
import starlords.util.dialogControler.dialogRull.randoms.DialogRule_random_opinionOfPlayerProposal;
import starlords.util.dialogControler.dialogRull.randoms.DialogRule_random_playerLordRelation;

import java.util.ArrayList;
import java.util.Iterator;

public class DialogRule_random extends DialogRule_Base {
    int range = 0;
    ArrayList<DialogRule_random_base> randoms = new ArrayList<DialogRule_random_base>();
    @SneakyThrows
    public DialogRule_random(JSONObject jsonObject) {
        range = jsonObject.getInt("range");
        for (Iterator it = jsonObject.keys(); it.hasNext(); ) {
            String key = (String) it.next();
            double value = jsonObject.getDouble(key);
            switch (key){
                case "base":
                    randoms.add(new DialogRule_random_base(value));
                    break;
                case "playerLordRelation":
                    randoms.add(new DialogRule_random_playerLordRelation(value));
                    break;
                case "opinionOfCurrProposal":
                    randoms.add(new DialogRule_random_opinionOfCurrProposal(value));
                    break;
                case "opinionOfPlayerProposal":
                    randoms.add(new DialogRule_random_opinionOfPlayerProposal(value));
                    break;
            }
        }
    }
    @Override
    public boolean condition(Lord lord) {
        int random = Utils.rand.nextInt(range);
        double value = 0;
        for (DialogRule_random_base a: randoms){
            value += a.value(lord);
        }
        //note to furture me: this can have negitive values, and thats why im not short cerceting this eq and have the final statment here
        return random <= value;
    }
}
