package starlords.util.dialogControler;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.OptionPanelAPI;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import org.apache.log4j.Logger;
import starlords.controllers.LordController;
import starlords.person.Lord;
import starlords.util.dialogControler.dialogRull.DialogRule_Base;

import java.util.ArrayList;
import java.util.HashMap;

public class DialogGroupOption {
    public String line;
    private ArrayList<DialogRule_Base> rules;
    public DialogGroupOption(ArrayList<DialogRule_Base> rules,String line){
        this.rules = rules;
        this.line = line;
    }
    public boolean canAddLord(Lord lord, Lord pastLord){
        for (DialogRule_Base a : rules){
            if (!a.condition(lord,pastLord)) return false;
        }
        return true;
    }
    public void applyOptions(Lord pastLord, TextPanelAPI textPanel, OptionPanelAPI options, InteractionDialogAPI dialog, HashMap<String,String> markersReplaced){
        Logger log = Global.getLogger(DialogGroupOption.class);
        log.info("getting additional options....");
        ArrayList<Lord> goodLords = new ArrayList<>();
        for (Lord lord : LordController.getLordsList()){
            if (canAddLord(lord,pastLord)){
                log.info("  adding lord named "+lord.getLordAPI().getNameString());
                goodLords.add(lord);
            }
        }
        for (Lord lord : goodLords){
            log.info("  adding option of key, lord: "+ line +", "+lord.getLordAPI().getNameString());
            DialogSet.addOptionWithInserts(line,pastLord,lord,textPanel,options,dialog,markersReplaced);
        }
    }
}
