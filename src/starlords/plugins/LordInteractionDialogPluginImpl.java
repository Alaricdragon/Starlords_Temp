package starlords.plugins;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.characters.FullName;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import lombok.Getter;
import starlords.ai.LordAI;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.ai.ModularFleetAIAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.combat.EngagementResultAPI;
import com.fs.starfarer.api.impl.campaign.FleetInteractionDialogPluginImpl;
import com.fs.starfarer.api.impl.campaign.ids.MemFlags;
import com.fs.starfarer.api.impl.campaign.missions.cb.BaseCustomBounty;
import com.fs.starfarer.api.impl.campaign.missions.hub.*;
import com.fs.starfarer.api.impl.campaign.rulecmd.BeginMission;
import com.fs.starfarer.api.util.Misc;
import starlords.controllers.*;
import starlords.faction.LawProposal;
import org.apache.log4j.Logger;
import starlords.person.Lord;
import starlords.person.LordAction;
import starlords.person.LordEvent;
import starlords.ui.MissionPreviewIntelPlugin;
import starlords.util.DefectionUtils;
import starlords.util.GenderUtils;
import starlords.util.StringUtil;
import starlords.util.Utils;
import starlords.util.dialogControler.DialogDataHolder;
import starlords.util.dialogControler.DialogOption;
import starlords.util.dialogControler.DialogSet;

import java.awt.*;
import java.util.*;

import static com.fs.starfarer.api.impl.campaign.rulecmd.BeginMission.TEMP_MISSION_KEY;
import static starlords.ai.LordAI.BUSY_REASON;
import static starlords.util.Constants.*;

public class LordInteractionDialogPluginImpl implements InteractionDialogPlugin {
    public static DialogDataHolder DATA_HOLDER;
    public static Logger log = Global.getLogger(LordInteractionDialogPluginImpl.class);
    static String CATEGORY = "starlords_lords_dialog";

    InteractionDialogPlugin prevPlugin;
    InteractionDialogAPI dialog;
    TextPanelAPI textPanel;
    OptionPanelAPI options;
    VisualPanelAPI visual;

    private HashMap<String, Lord> lordsReference;
    private CampaignFleetAPI lordFleet;
    @Getter
    protected static Lord targetLord;
    private boolean hasGreeted;
    @Getter
    private static String DialogType;

    // for defection
    private String justification;
    private String bargainAmount;
    private int claimStrength;

    // whether political sway is for or against the law
    private boolean swayFor;
    private LawProposal proposal;

    @Override
    public void init(InteractionDialogAPI dialog) {
        this.dialog = dialog;
        prevPlugin = dialog.getPlugin();
        dialog.setPlugin(this);
        textPanel = dialog.getTextPanel();
        options = dialog.getOptionPanel();
        visual = dialog.getVisualPanel();
        // TODO unsafe cast
        lordFleet = (CampaignFleetAPI) dialog.getInteractionTarget();

        lordsReference = new HashMap<>();
        targetLord = LordController.getLordById(lordFleet.getCommander().getId());
        if (DATA_HOLDER == null || !DATA_HOLDER.getTargetID().equals(targetLord.getLordAPI().getId())){
            DATA_HOLDER = new DialogDataHolder();
            DATA_HOLDER.setTargetID(targetLord.getLordAPI().getId());
        }
        DialogType = setDialogType();
        DialogOption option = new DialogOption("greeting",new ArrayList<>());
        optionSelected(null, option);
    }
    protected String setDialogType(){
        return "default";
    }

    @Override
    public void optionSelected(String optionText, Object optionData) {
        if (optionSelected_NEW(optionText,optionData)){
            return;
        }
    }

    @Override
    public void optionMousedOver(String optionText, Object optionData) {

    }

    @Override
    public void advance(float amount) {

    }

    @Override
    public void backFromEngagement(EngagementResultAPI battleResult) {

    }

    @Override
    public Object getContext() {
        return null;
    }

    @Override
    public Map<String, MemoryAPI> getMemoryMap() {
        return null;
    }

    private String relToString(int rel) {
        if (rel <= -100 * RepLevel.HOSTILE.getMax()) {
            return "hated";
        } else if (rel <= -100 * RepLevel.SUSPICIOUS.getMax()) {
            return "disliked";
        } else if (rel <= 100 * RepLevel.FAVORABLE.getMax()) {
            return "neutral";
        } else if (rel <= 100 * RepLevel.WELCOMING.getMax()) {
            return "friendly";
        } else {
            return "trusted";
        }
    }

    private void displayAcceptSuggestAction() {
        if (targetLord.getFaction().isPlayerFaction()) {
            textPanel.addParagraph(StringUtil.getString(
                    CATEGORY, "accept_suggest_action_subject",
                    GenderUtils.sirOrMaam(Global.getSector().getPlayerPerson(), false)));
        } else {
            textPanel.addParagraph(StringUtil.getString(CATEGORY, "accept_suggest_action"));
        }
    }

    private boolean optionSelected_NEW(String optionText, Object optionData){
        if (optionData instanceof DialogOption){
            if (prevPlugin.equals(this) && !visual.isShowingPersonInfo(targetLord.getLordAPI())) {
                visual.showPersonInfo(targetLord.getLordAPI(), false, true);
            }
            DialogOption data = (DialogOption) optionData;
            data.applyAddons(textPanel,options,dialog,targetLord);
            String selectedOption = data.optionID;

            Lord secondLord = data.getTargetLord();
            MarketAPI targetMarket = data.getTargetMarket();
            switch (selectedOption){
                case "greeting":
                    optionSelected_greetings(selectedOption,secondLord,targetMarket);
                    break;
                case "exitDialog":
                    optionSelected_exitDialog();
                    break;
                case "":
                    break;
                case "current_task_desc":
                    optionSelected_askCurrentTask(selectedOption,secondLord,targetMarket);
                    break;
                case "spend_time_together":
                    optionSelected_suggestDate(selectedOption,secondLord,targetMarket);
                    break;
                default:
                    DialogSet.addParaWithInserts(selectedOption,targetLord,secondLord,targetMarket,textPanel,options,dialog);
                    break;
            }
            return true;
        }
        return false;
    }


    private void optionSelected_greetings(String selectedOption,Lord secondLord,MarketAPI targetMarket){
        boolean isFeast = targetLord.getCurrAction() == LordAction.FEAST;
        LordEvent feast = isFeast ? EventController.getCurrentFeast(targetLord.getLordAPI().getFaction()) : null;
        //only run greetings if player has not yet heard them this conversation
        DialogSet.addParaWithInserts(selectedOption,targetLord,secondLord,targetMarket,textPanel,options,dialog,hasGreeted);
        if (!hasGreeted){
            hasGreeted = true;
        }
        //if lord is newly met, add intil on lord.
        if (!targetLord.isKnownToPlayer()) {
            DialogSet.addParaWithInserts("addedIntel",targetLord,textPanel,options,dialog);
            targetLord.setKnownToPlayer(true);
        }
        //if this is a feast, apply rep gained.
        if(feast != null) {
            if (!feast.getOriginator().isFeastInteracted()) {
                feast.getOriginator().setFeastInteracted(true);
                applyRepIncrease(textPanel, feast.getOriginator(), 3);
            }
            for (Lord lord : feast.getParticipants()) {
                if (!lord.isFeastInteracted()) {
                    lord.setFeastInteracted(true);
                    applyRepIncrease(textPanel, lord, 2);
                }
            }
        }
    }
    private void optionSelected_askCurrentTask(String selectedOption,Lord secondLord,MarketAPI targetMarket){
        //ok, so: the link to this needs to be set as 'current_task_desc'.
        //the options for this needs to be set to: 'greetings'.
        String args = "";
        if (targetLord.getCurrAction() != null) {
            selectedOption += "_" + targetLord.getCurrAction().base.toString().toLowerCase();
        }else{
            selectedOption += "_none";
        }
        if (targetLord.getTarget() != null) {
            args = targetLord.getTarget().getName();
        }
        HashMap<String,String> inserts = new HashMap<>();
        inserts.put("%c0",args);
        DialogSet.addParaWithInserts(selectedOption,targetLord,secondLord,targetMarket,textPanel,options,dialog,false,inserts);
    }
    private void optionSelected_suggestDate(String selectedOption,Lord secondLord,MarketAPI targetMarket){
        int dateType = 1 + Utils.rand.nextInt(36);
        DialogSet.addParaWithInserts(selectedOption+dateType,targetLord,secondLord,targetMarket,textPanel,options,dialog);
    }
    private void optionSelected_exitDialog(){
        if (prevPlugin.equals(this)) {
            dialog.dismiss();
        } else {
            dialog.setPlugin(prevPlugin);
            prevPlugin.optionSelected(null, FleetInteractionDialogPluginImpl.OptionId.CUT_COMM);
        }
    }




    private void applyRepIncrease(TextPanelAPI textPanel, Lord lord, int rep){
        lord.getLordAPI().getRelToPlayer().adjustRelationship((float) (rep*0.01), null);
        String line = DialogSet.getLineWithInserts(lord,"relation_increase");
        line = DialogSet.insertData(line,"%c0",""+rep);
        textPanel.addPara(line, Color.GREEN);
    }
    private void applyRepDecrease(TextPanelAPI textPanel,Lord lord, int rep){
        lord.getLordAPI().getRelToPlayer().adjustRelationship((float) (rep*-0.01), null);
        String line = DialogSet.getLineWithInserts(lord,"relation_decrease");
        line = DialogSet.insertData(line,"%c0",""+rep);
        textPanel.addPara(line, Color.RED);
    }
}
