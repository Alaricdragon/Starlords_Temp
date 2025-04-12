package starlords.plugins;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.characters.FullName;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
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
import starlords.util.dialogControler.DialogOption;
import starlords.util.dialogControler.DialogSet;

import java.awt.*;
import java.util.*;

import static com.fs.starfarer.api.impl.campaign.rulecmd.BeginMission.TEMP_MISSION_KEY;
import static starlords.ai.LordAI.BUSY_REASON;
import static starlords.util.Constants.*;

public class LordInteractionDialogPluginImpl implements InteractionDialogPlugin {

    public static Logger log = Global.getLogger(LordInteractionDialogPluginImpl.class);
    static String CATEGORY = "starlords_lords_dialog";
    public enum OptionId {
        INIT,
        START_WEDDING,
        DEDICATE_TOURNAMENT,
        ASK_TOURNAMENT,
        CONTINUE_TO_TOURNAMENT,
        ASK_CURRENT_TASK,
        ASK_QUESTION,
        ASK_QUEST,
        ASK_LOCATION,
        ASK_LOCATION_CHOICE,
        SWAY_PROPOSAL_PLAYER,
        SWAY_PROPOSAL_COUNCIL,
        SWAY_PROPOSAL_BARGAIN,
        PROFESS_ADMIRATION,
        SUGGEST_DATE,
        OFFER_GIFT,
        OFFER_GIFT_SELECTION,
        SUGGEST_MARRIAGE,
        SUGGEST_JOIN_PARTY,
        SUGGEST_LEAVE_PARTY,
        CONFIRM_TOGGLE_PARTY,
        SPEAK_PRIVATELY,
        ASK_WORLDVIEW,
        ASK_LIEGE_OPINION,
        ASK_FRIEND_FAVORITE_GIFT,
        ASK_FRIEND_FAVORITE_GIFT_LIST,
        SUGGEST_CEASEFIRE,
        SUGGEST_DEFECT,
        BARGAIN_DEFECT,
        JUSTIFY_DEFECT,
        CONFIRM_SUGGEST_DEFECT,
        SUGGEST_ACTION,
        FOLLOW_ME,
        STOP_FOLLOW_ME,
        SUGGEST_RAID,
        SUGGEST_RAID_LOC,
        SUGGEST_PATROL,
        SUGGEST_PATROL_LOC,
        SUGGEST_UPGRADE,
        LEAVE,
    }

    InteractionDialogPlugin prevPlugin;
    InteractionDialogAPI dialog;
    TextPanelAPI textPanel;
    OptionPanelAPI options;
    VisualPanelAPI visual;

    private HashMap<String, Lord> lordsReference;
    private CampaignFleetAPI lordFleet;
    Lord targetLord;
    private OptionId nextState;
    private boolean hasGreeted;

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
        DialogOption option = new DialogOption("greeting",new ArrayList<>());
        optionSelected(null, option);
    }

    @Override
    public void optionSelected(String optionText, Object optionData) {
        OptionId option = null;
        if (optionData instanceof OptionId) {
            option = (OptionId) optionData;
        } else if (nextState != null) {
            option = nextState;
        }
        if (optionSelected_NEW(optionText,optionData)){
            nextState = null;
            return;
        }
        nextState = null;

        optionSelected_OLD(optionText,optionData,option);
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
            DialogOption data = (DialogOption) optionData;
            data.applyAddons(textPanel,options,dialog,targetLord);
            String selectedOption = data.optionID;

            Lord secondLord = data.getTargetLord();
            switch (selectedOption){
                case "greeting":
                    optionSelected_greetings(selectedOption,secondLord);
                    break;
                case "exitDialog":
                    optionSelected_exitDialog();
                    break;
                case "":
                    break;
                case "current_task_desc":
                    optionSelected_askCurrentTask(selectedOption,secondLord);
                    break;
                case "selectedOption":
                    optionSelected_suggestDate(selectedOption,secondLord);
                    break;
                default:
                    DialogSet.addParaWithInserts(selectedOption,targetLord,secondLord,textPanel,options,dialog);
                    break;
            }
            return true;
        }
        return false;
    }

    private void optionSelected_OLD(String optionText, Object optionData,OptionId option){
        boolean willEngage = false;
        boolean hostile = false;
        if (lordFleet.getFaction().isHostileTo(Global.getSector().getPlayerFleet().getFaction())) {
            hostile = true;
            SectorEntityToken target = ((ModularFleetAIAPI) lordFleet.getAI()).getTacticalModule().getTarget();
            if (lordFleet.isHostileTo(Global.getSector().getPlayerFleet()) && Global.getSector().getPlayerFleet().equals(target)) {
                willEngage = true;
            }
        }

        if (prevPlugin.equals(this) && !visual.isShowingPersonInfo(targetLord.getLordAPI())) {
            visual.showPersonInfo(targetLord.getLordAPI(), false, true);
        }

        PersonAPI player = Global.getSector().getPlayerPerson();
        FactionAPI faction;
        Random rand;
        boolean isFeast = targetLord.getCurrAction() == LordAction.FEAST;
        LordEvent feast = isFeast ? EventController.getCurrentFeast(targetLord.getLordAPI().getFaction()) : null;
        switch (option) {
            case INIT:
                options.clearOptions();
                optionSelected_INIT(optionText,optionData,player,willEngage,hostile,feast,option);
                break;
            case DEDICATE_TOURNAMENT:
                optionSelected_DEDICATE_TOURNAMENT(optionText,optionData,player,willEngage,hostile,feast,option);
                break;
            case ASK_TOURNAMENT:
                optionSelected_ASK_TOURNAMENT(optionText,optionData,player,willEngage,hostile,feast,option);
                break;
            case CONTINUE_TO_TOURNAMENT:
                optionSelected_CONTINUE_TO_TOURNAMENT(optionText,optionData,player,willEngage,hostile,feast,option);
                break;
            case ASK_CURRENT_TASK:
                optionSelected_ASK_CURRENT_TASK(optionText,optionData,player,willEngage,hostile,feast,option);
                break;
            case ASK_QUESTION:
                optionSelected_ASK_QUESTION(optionText,optionData,player,willEngage,hostile,feast,option);
                break;
            case PROFESS_ADMIRATION:
                optionSelected_PROFESS_ADMIRATION(optionText,optionData,player,willEngage,hostile,feast,option);
                break;
            case SUGGEST_DATE:
                optionSelected_SUGGEST_DATE(optionText,optionData,player,willEngage,hostile,feast,option);
                break;
            case OFFER_GIFT:
                optionSelected_OFFER_GIFT(optionText,optionData,player,willEngage,hostile,feast,option);
                break;
            case OFFER_GIFT_SELECTION:
                optionSelected_OFFER_GIFT_SELECTION(optionText,optionData,player,willEngage,hostile,feast,option);
                break;
            case START_WEDDING:
                optionSelected_START_WEDDING(optionText,optionData,player,willEngage,hostile,feast,option);
                break;
            case SUGGEST_MARRIAGE:
                optionSelected_SUGGEST_MARRIAGE(optionText,optionData,player,willEngage,hostile,feast,option);
                break;
            case SUGGEST_JOIN_PARTY:
                optionSelected_SUGGEST_JOIN_PARTY(optionText,optionData,player,willEngage,hostile,feast,option);
                break;
            case SUGGEST_LEAVE_PARTY:
                optionSelected_SUGGEST_LEAVE_PARTY(optionText,optionData,player,willEngage,hostile,feast,option);
                break;
            case CONFIRM_TOGGLE_PARTY:
                optionSelected_CONFIRM_TOGGLE_PARTY(optionText,optionData,player,willEngage,hostile,feast,option);
                break;
            case SWAY_PROPOSAL_PLAYER:
                //yes, this was empty when I started editing. so it just runs SWAY_PROPOSAL_COUNCIL.
            case SWAY_PROPOSAL_COUNCIL:
                optionSelected_SWAY_PROPOSAL_COUNCIL(optionText,optionData,player,willEngage,hostile,feast,option);
                break;
            case SWAY_PROPOSAL_BARGAIN:
                optionSelected_SWAY_PROPOSAL_BARGAIN(optionText,optionData,player,willEngage,hostile,feast,option);
                break;
            case ASK_LOCATION:
                optionSelected_ASK_LOCATION(optionText,optionData,player,willEngage,hostile,feast,option);
                break;
            case ASK_LOCATION_CHOICE:
                optionSelected_ASK_LOCATION_CHOICE(optionText,optionData,player,willEngage,hostile,feast,option);
                break;
            case SPEAK_PRIVATELY:
                optionSelected_SPEAK_PRIVATELY(optionText,optionData,player,willEngage,hostile,feast,option);
                break;
            case ASK_WORLDVIEW:
                optionSelected_ASK_WORLDVIEW(optionText,optionData,player,willEngage,hostile,feast,option);
                break;
            case ASK_LIEGE_OPINION:
                optionSelected_ASK_LIEGE_OPINION(optionText,optionData,player,willEngage,hostile,feast,option);
                break;
            case ASK_FRIEND_FAVORITE_GIFT:
                optionSelected_ASK_FRIEND_FAVORITE_GIFT(optionText,optionData,player,willEngage,hostile,feast,option);
                break;
            case ASK_FRIEND_FAVORITE_GIFT_LIST:
                optionSelected_ASK_FRIEND_FAVORITE_GIFT_LIST(optionText,optionData,player,willEngage,hostile,feast,option);
                break;
            case SUGGEST_DEFECT:
                optionSelected_SUGGEST_DEFECT(optionText,optionData,player,willEngage,hostile,feast,option);
                break;
            case JUSTIFY_DEFECT:
                optionSelected_JUSTIFY_DEFECT(optionText,optionData,player,willEngage,hostile,feast,option);
                break;
            case BARGAIN_DEFECT:
                optionSelected_BARGAIN_DEFECT(optionText,optionData,player,willEngage,hostile,feast,option);
                break;
            case CONFIRM_SUGGEST_DEFECT:
                optionSelected_CONFIRM_SUGGEST_DEFECT(optionText,optionData,player,willEngage,hostile,feast,option);
                break;
            case ASK_QUEST:
                optionSelected_ASK_QUEST(optionText,optionData,player,willEngage,hostile,feast,option);
                break;
            case SUGGEST_ACTION:
                optionSelected_SUGGEST_ACTION(optionText,optionData,player,willEngage,hostile,feast,option);
                break;
            case FOLLOW_ME:
                optionSelected_FOLLOW_ME(optionText,optionData,player,willEngage,hostile,feast,option);
                break;
            case STOP_FOLLOW_ME:
                optionSelected_STOP_FOLLOW_ME(optionText,optionData,player,willEngage,hostile,feast,option);
                break;
            case SUGGEST_PATROL:
                optionSelected_SUGGEST_PATROL(optionText,optionData,player,willEngage,hostile,feast,option);
                break;
            case SUGGEST_PATROL_LOC:
                optionSelected_SUGGEST_PATROL_LOC(optionText,optionData,player,willEngage,hostile,feast,option);
                break;
            case SUGGEST_RAID:
                optionSelected_SUGGEST_RAID(optionText,optionData,player,willEngage,hostile,feast,option);
                break;
            case SUGGEST_RAID_LOC:
                optionSelected_SUGGEST_RAID_LOC(optionText,optionData,player,willEngage,hostile,feast,option);
                break;
            case SUGGEST_UPGRADE:
                optionSelected_SUGGEST_UPGRADE(optionText,optionData,player,willEngage,hostile,feast,option);
                break;
            case SUGGEST_CEASEFIRE:
                optionSelected_SUGGEST_CEASEFIRE(optionText,optionData,player,willEngage,hostile,feast,option);
                break;
            case LEAVE:
                optionSelected_LEAVE(optionText,optionData,player,willEngage,hostile,feast,option);
                break;
        }
    }

    private void optionSelected_greetings(String selectedOption,Lord secondLord){
        boolean isFeast = targetLord.getCurrAction() == LordAction.FEAST;
        LordEvent feast = isFeast ? EventController.getCurrentFeast(targetLord.getLordAPI().getFaction()) : null;
        //only run greetings if player has not yet heard them this conversation
        DialogSet.addParaWithInserts(selectedOption,targetLord,secondLord,textPanel,options,dialog,hasGreeted);
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
    private void optionSelected_askCurrentTask(String selectedOption,Lord secondLord){
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
        DialogSet.addParaWithInserts(selectedOption,targetLord,secondLord,textPanel,options,dialog,false,inserts);
    }
    private void optionSelected_suggestDate(String selectedOption,Lord secondLord){
        int dateType = 1 + Utils.rand.nextInt(36);
        DialogSet.addParaWithInserts(selectedOption+dateType,targetLord,secondLord,textPanel,options,dialog);
    }
    private void optionSelected_exitDialog(){
        if (prevPlugin.equals(this)) {
            dialog.dismiss();
        } else {
            dialog.setPlugin(prevPlugin);
            prevPlugin.optionSelected(null, FleetInteractionDialogPluginImpl.OptionId.CUT_COMM);
        }
    }


    private void optionSelected_NAMETEMP(String optionText, Object optionData,PersonAPI player,boolean willEngage,boolean hostile, LordEvent feast,OptionId option){
        optionSelected_NAMETEMP(optionText,optionData,player,willEngage,hostile,feast,option);
    }
    private void optionSelected_INIT(String optionText, Object optionData,PersonAPI player,boolean willEngage,boolean hostile, LordEvent feast,OptionId option){
        /*ok, so order of liness:
            OK. OK OK OK OK OK OK OK OK OK OK.
            WHAT THE FUCK AM I DOING?
            there is no ponit in attempting to go for a full upfront singelarity of data here:
            NEW PLAN:
                1) keep most of the conditional likes (like the 5 or whatever greeting types here). this has to use a custom line or 3 anyways.
                2) DO NOT attempt to run every little peace of code though the newly built data system. ONLY RUN WHAT OPTIONS NEED TO BE RAN. there is no ponit in anything else.
                    -I am not fucking joking you rude person! respect your past work! yes, its not perfect, but it fucking works, so lets keep it that fucking way!

                what I am going to do:
                1) build the default_dialog_options and the dialogSets data as planed.
                2) add the required conditions to make that work, for fucks sakes.

            the first thing we need to do is get the available options:
                (options in the following order: hostile, feast, none hostile, always)
                hostile:
                    (willEngage == true) willEngage : option_avoid_battle : OptionId.SUGGEST_CEASEFIRE
                    option_speak_privately : OptionId.SPEAK_PRIVATELY
                    option_cutComLink : OptionId.LEAVE
                host_feast:
                feast:
                    if(not yet has tournament)
                        option_ask_tournament : OptionId.ASK_TOURNAMENT
                    if(lord is courted, player won tournament, victory ! dedecated)
                        option_dedicate_tournament : OptionId.DEDICATE_TOURNAMENT
                    if(feast.isHostingWedding)
                        inserts.put("%c0",spouse.getLordAPI().getNameString());
                        option_host_wedding : OptionId.START_WEDDING

                    option_ask_current_task : OptionId.ASK_CURRENT_TASK
                    option_ask_question : OptionId.ASK_QUESTION
                    option_suggest_action : OptionId.SUGGEST_ACTION
                    option_speak_privately : OptionId.SPEAK_PRIVATELY
                    option_cutComLink : OptionId.LEAVE
                first:
                other:
                    option_ask_current_task : OptionId.ASK_CURRENT_TASK
                    option_ask_question : OptionId.ASK_QUESTION
                    option_suggest_action : OptionId.SUGGEST_ACTION
                    option_speak_privately : OptionId.SPEAK_PRIVATELY
                    option_cutComLink : OptionId.LEAVE





                always: (no condition)
                    DialogSet.addOptionWithInserts("option_speak_privately",null,OptionId.SPEAK_PRIVATELY,targetLord,textPanel,options);
                    DialogSet.addOptionWithInserts("option_cutComLink",null,OptionId.LEAVE,targetLord,textPanel,options);
                none hostile: (no conditions (again))
                    DialogSet.addOptionWithInserts("option_ask_current_task",null,OptionId.ASK_CURRENT_TASK,targetLord,textPanel,options);
                    DialogSet.addOptionWithInserts("option_ask_question",null,OptionId.ASK_QUESTION,targetLord,textPanel,options);
                    DialogSet.addOptionWithInserts("option_suggest_action",null,OptionId.SUGGEST_ACTION,targetLord,textPanel,options);
                feast dialog: (conditions)
                    if(not yet has tournament)
                        DialogSet.addOptionWithInserts("option_ask_tournament",null,OptionId.ASK_TOURNAMENT,targetLord,textPanel,options);
                    if(lord is courted, player won tournament, victory ! dedecated)
                        DialogSet.addOptionWithInserts("option_dedicate_tournament",null,OptionId.DEDICATE_TOURNAMENT,targetLord,textPanel,options);
                    if(feast.isHostingWedding)
                        inserts.put("%c0",spouse.getLordAPI().getNameString());
                        DialogSet.addOptionWithInserts("option_host_wedding",null,OptionId.START_WEDDING,targetLord,textPanel,options,inserts);

        */
        if (hostile){
            optionSelected_INIT_HOSTILE(optionText,optionData,player,willEngage,hostile,feast,option);
            return;
        }
        if (feast != null){
            if (feast.getOriginator().equals(targetLord)){
                optionSelected_INIT_FEAST_HOST(optionText,optionData,player,willEngage,hostile,feast,option);
                return;
            }
            optionSelected_INIT_FEAST(optionText,optionData,player,willEngage,hostile,feast,option);
        }
        if (targetLord.isKnownToPlayer()){
            optionSelected_INIT_OTHER(optionText,optionData,player,willEngage,hostile,feast,option);
            //note: have different texts in json for is married, or is subject, or otherwize.
            return;
        }
        optionSelected_INIT_FIRSTCONTACT(optionText,optionData,player,willEngage,hostile,feast,option);
    }
    private void support_INIT_BASICOPTIONS(String optionText, Object optionData,PersonAPI player,boolean willEngage,boolean hostile, LordEvent feast,OptionId option){
        if (!targetLord.isKnownToPlayer()) {
            DialogSet.addParaWithInserts("addedIntel",targetLord,textPanel,options);
            targetLord.setKnownToPlayer(true);
        }
        DialogSet.addOptionWithInserts("option_speak_privately",null,OptionId.SPEAK_PRIVATELY,targetLord,textPanel,options);
        DialogSet.addOptionWithInserts("option_cutComLink",null,OptionId.LEAVE,targetLord,textPanel,options);
        options.setShortcut(OptionId.LEAVE, 1, false, false, false, true);
    }
    private void support_INIT_NOTHOSTILEOPTIONS(String optionText, Object optionData,PersonAPI player,boolean willEngage,boolean hostile, LordEvent feast,OptionId option){
        DialogSet.addOptionWithInserts("option_ask_current_task",null,OptionId.ASK_CURRENT_TASK,targetLord,textPanel,options);
        DialogSet.addOptionWithInserts("option_ask_question",null,OptionId.ASK_QUESTION,targetLord,textPanel,options);
        DialogSet.addOptionWithInserts("option_suggest_action",null,OptionId.SUGGEST_ACTION,targetLord,textPanel,options);
    }
    private void support_INIT_FEASTOPTIONS(String optionText, Object optionData,PersonAPI player,boolean willEngage,boolean hostile, LordEvent feast,OptionId option){
        if (!feast.getOriginator().isFeastInteracted()) {
            feast.getOriginator().setFeastInteracted(true);
            applyRepIncrease(textPanel,feast.getOriginator(),3);
        }
        for (Lord lord : feast.getParticipants()) {
            if (!lord.isFeastInteracted()) {
                lord.setFeastInteracted(true);
                applyRepIncrease(textPanel,lord,2);
            }
        }
        if (!feast.isHeldTournament()) {
            DialogSet.addOptionWithInserts("option_ask_tournament",null,OptionId.ASK_TOURNAMENT,targetLord,textPanel,options);
        }
        if (targetLord.isCourted() && feast.getTournamentWinner() != null
                && !feast.isVictoryDedicated() && feast.getTournamentWinner().isPlayer()) {

            DialogSet.addOptionWithInserts("option_dedicate_tournament",null,OptionId.DEDICATE_TOURNAMENT,targetLord,textPanel,options);
        }
        if (feast.getWeddingCeremonyTarget() != null && !feast.getWeddingCeremonyTarget().isMarried()) {
            Lord spouse = feast.getWeddingCeremonyTarget();
            HashMap<String,String> inserts = new HashMap<>();
            inserts.put("%c0",spouse.getLordAPI().getNameString());
            //NOTE: this hidden line is only hidden because I broke it somehow.
            //DialogSet.addOptionWithInserts("option_host_wedding",null,OptionId.START_WEDDING,targetLord,textPanel,options,inserts);
        }
    }

    private void optionSelected_INIT_FEAST_HOST(String optionText, Object optionData,PersonAPI player,boolean willEngage,boolean hostile, LordEvent feast,OptionId option){
        if (!hasGreeted){
            hasGreeted = true;
            textPanel.addParagraph(DialogSet.getLineWithInserts(targetLord,"greeting_host_feast"));
        }
        support_INIT_FEASTOPTIONS(optionText,optionData,player,willEngage,hostile,feast,option);
        support_INIT_NOTHOSTILEOPTIONS(optionText,optionData,player,willEngage,hostile,feast,option);
        support_INIT_BASICOPTIONS(optionText,optionData,player,willEngage,hostile,feast,option);
    }
    private void optionSelected_INIT_FEAST(String optionText, Object optionData,PersonAPI player,boolean willEngage,boolean hostile, LordEvent feast,OptionId option){
        if (!hasGreeted){
            hasGreeted = true;
            textPanel.addParagraph(DialogSet.getLineWithInserts(targetLord,"greeting_feast"));
        }
        support_INIT_FEASTOPTIONS(optionText,optionData,player,willEngage,hostile,feast,option);
        support_INIT_NOTHOSTILEOPTIONS(optionText,optionData,player,willEngage,hostile,feast,option);
        support_INIT_BASICOPTIONS(optionText,optionData,player,willEngage,hostile,feast,option);
    }
    private void optionSelected_INIT_FIRSTCONTACT(String optionText, Object optionData,PersonAPI player,boolean willEngage,boolean hostile, LordEvent feast,OptionId option) {
        if (!hasGreeted) {
            hasGreeted = true;
            DialogSet.addParaWithInserts("greetings_first",targetLord,textPanel,options);
        }
        support_INIT_NOTHOSTILEOPTIONS(optionText,optionData,player,willEngage,hostile,feast,option);
        support_INIT_BASICOPTIONS(optionText,optionData,player,willEngage,hostile,feast,option);
    }
    private void optionSelected_INIT_OTHER(String optionText, Object optionData,PersonAPI player,boolean willEngage,boolean hostile, LordEvent feast,OptionId option){
        if (!hasGreeted){
            hasGreeted = true;
            DialogSet.addParaWithInserts("greetings_other",targetLord,textPanel,options);
        }
        support_INIT_NOTHOSTILEOPTIONS(optionText,optionData,player,willEngage,hostile,feast,option);
        support_INIT_BASICOPTIONS(optionText,optionData,player,willEngage,hostile,feast,option);
    }
    private void optionSelected_INIT_HOSTILE(String optionText, Object optionData,PersonAPI player,boolean willEngage,boolean hostile, LordEvent feast,OptionId option) {
        if (!hasGreeted){
            hasGreeted = true;
            DialogSet.addParaWithInserts("greetings_hostile",targetLord,textPanel,options);
            //put text here.
        }
        if (willEngage) {
            DialogSet.addOptionWithInserts("option_avoid_battle","tooltip_avoid_battle",OptionId.SUGGEST_CEASEFIRE,targetLord,textPanel,options);
        }
        support_INIT_BASICOPTIONS(optionText,optionData,player,willEngage,hostile,feast,option);
    }

    private void optionSelected_DEDICATE_TOURNAMENT(String optionText, Object optionData,PersonAPI player,boolean willEngage,boolean hostile, LordEvent feast,OptionId option){
        if (targetLord.isDedicatedTournament()) {
            DialogSet.addParaWithInserts("dedicate_tournament_again",targetLord,textPanel,options);
        } else {
            DialogSet.addParaWithInserts("dedicate_tournament",targetLord,textPanel,options);
        }
        feast.setVictoryDedicated(true);
        // other courted lords get jealous
        for (Lord lord: LordController.getLordsList()) {
            if (lord.isCourted() && !lord.equals(targetLord)) {
                int decrease = 1;
                if (feast.getOriginator().equals(lord) || feast.getParticipants().contains(lord)) {
                    decrease = 2;
                }
                applyRepDecrease(textPanel,lord,decrease);
            }
        }
        options.removeOption(OptionId.DEDICATE_TOURNAMENT);
    }
    private void optionSelected_ASK_TOURNAMENT(String optionText, Object optionData,PersonAPI player,boolean willEngage,boolean hostile, LordEvent feast,OptionId option){
        if (feast == null || feast.getParticipants().size() < 3) {
            DialogSet.addParaWithInserts("cant_start_tournament",targetLord,textPanel,options);
        } else {
            DialogSet.addParaWithInserts("confirm_start_tournament",targetLord,textPanel,options);
            options.clearOptions();
            DialogSet.addOptionWithInserts("option_continue_to_tournament",null,OptionId.CONTINUE_TO_TOURNAMENT,targetLord,textPanel,options);
            DialogSet.addOptionWithInserts("option_avoid_tournament",null,OptionId.INIT,targetLord,textPanel,options);
            options.setShortcut(OptionId.INIT, 1, false, false, false, true);
        }
    }
    private void optionSelected_CONTINUE_TO_TOURNAMENT(String optionText, Object optionData,PersonAPI player,boolean willEngage,boolean hostile, LordEvent feast,OptionId option){
        dialog.dismiss();
        // cant open new dialogue immediately
        Global.getSector().addTransientScript(new EveryFrameScript() {
            boolean isDone;
            @Override
            public boolean isDone() {
                return isDone;
            }

            @Override
            public boolean runWhilePaused() {
                return true;
            }

            @Override
            public void advance(float amount) {
                if (!isDone && Global.getSector().getCampaignUI().showInteractionDialog(
                        new TournamentDialogPlugin(EventController.getCurrentFeast(targetLord.getFaction())), null)) {
                    isDone = true;
                }
            }
        });
    }
    private void optionSelected_ASK_CURRENT_TASK(String optionText, Object optionData,PersonAPI player,boolean willEngage,boolean hostile, LordEvent feast,OptionId option){

        String id = "current_task_desc_none";
        String args = "";
        if (targetLord.getCurrAction() != null) {
            id = "current_task_desc_" + targetLord.getCurrAction().base.toString().toLowerCase();
        }
        if (targetLord.getTarget() != null) {
            args = targetLord.getTarget().getName();
        }
        /*String text = DialogSet.getLineWithInserts(targetLord,id);
        text = DialogSet.insertData(text,"%n0",args);
        textPanel.addParagraph(text);*/
        HashMap<String,String> inserts = new HashMap<>();
        inserts.put("%n0",args);
        DialogSet.addParaWithInserts(id,targetLord,textPanel,options,inserts);
    }
    private void optionSelected_ASK_QUESTION(String optionText, Object optionData,PersonAPI player,boolean willEngage,boolean hostile, LordEvent feast,OptionId option){
        options.clearOptions();
        DialogSet.addOptionWithInserts("option_ask_location",null,OptionId.ASK_LOCATION,targetLord,textPanel,options);
        DialogSet.addOptionWithInserts("option_ask_quest",null,OptionId.ASK_QUEST,targetLord,textPanel,options);
        if (feast != null) {
            boolean playerIsMarried = LordController.getSpouse() != null;
            if (!feast.isProfessedAdmiration() && !targetLord.isCourted() && !playerIsMarried) {
                DialogSet.addOptionWithInserts("option_profess_admiration",null,OptionId.PROFESS_ADMIRATION,targetLord,textPanel,options);
            }
            if (targetLord.isCourted()) {
                if (!feast.isHeldDate() && !playerIsMarried) {
                    DialogSet.addOptionWithInserts("option_ask_date",null,OptionId.SUGGEST_DATE,targetLord,textPanel,options);
                }
                if (!targetLord.isMarried() && feast.getWeddingCeremonyTarget() == null
                        && !playerIsMarried) {
                    DialogSet.addOptionWithInserts("option_ask_marriage",null,OptionId.SUGGEST_MARRIAGE,targetLord,textPanel,options);
                }
            }
        }
        if (targetLord.isMarried()) {
            if (targetLord.getCurrAction() == LordAction.COMPANION) {
                DialogSet.addOptionWithInserts("option_ask_leave_party",null,OptionId.SUGGEST_LEAVE_PARTY,targetLord,textPanel,options);
            } else {
                DialogSet.addOptionWithInserts("option_ask_join_party",null,OptionId.SUGGEST_JOIN_PARTY,targetLord,textPanel,options);
            }
        }
        FactionAPI faction = targetLord.getFaction();
        if (faction.equals(Utils.getRecruitmentFaction())) {
            LawProposal councilProposal = PoliticsController.getCurrProposal(faction);
            if (councilProposal != null
                    && !councilProposal.getPledgedAgainst().contains(targetLord.getLordAPI().getId())
                    && !councilProposal.getPledgedFor().contains(targetLord.getLordAPI().getId())) {
                if (councilProposal.isPlayerSupports()) {
                    DialogSet.addOptionWithInserts("option_sway_council_support",null,OptionId.SWAY_PROPOSAL_COUNCIL,targetLord,textPanel,options);
                    swayFor = true;
                } else {
                    DialogSet.addOptionWithInserts("option_sway_council_oppose",null,OptionId.SWAY_PROPOSAL_COUNCIL,targetLord,textPanel,options);
                    swayFor = false;
                }
            }
            LawProposal playerProposal = PoliticsController.getProposal(LordController.getPlayerLord());
            if (playerProposal != null && !playerProposal.equals(councilProposal)
                    && !playerProposal.getPledgedAgainst().contains(targetLord.getLordAPI().getId())
                    && !playerProposal.getPledgedFor().contains(targetLord.getLordAPI().getId())) {
                DialogSet.addOptionWithInserts("option_sway_player",null,OptionId.SWAY_PROPOSAL_PLAYER,targetLord,textPanel,options);
            }
        }
        DialogSet.addOptionWithInserts("option_nevermind_askQuestion",null,OptionId.INIT,targetLord,textPanel,options);
        options.setShortcut(OptionId.INIT, 1, false, false, false, true);
    }
    private void optionSelected_PROFESS_ADMIRATION(String optionText, Object optionData, PersonAPI player, boolean willEngage, boolean hostile, LordEvent feast,OptionId option){
        DialogSet.addParaWithInserts("admiration_response",targetLord,textPanel,options);
        targetLord.setCourted(true);
        feast.setProfessedAdmiration(true);
        optionSelected(null, OptionId.ASK_QUESTION);
    }
    private void optionSelected_SUGGEST_DATE(String optionText, Object optionData,PersonAPI player,boolean willEngage,boolean hostile, LordEvent feast,OptionId option){
        int dateType = 1 + Utils.rand.nextInt(36);
        int loss;
        DialogSet.addParaWithInserts("spend_time_together"+dateType,targetLord,textPanel,options);
        DialogSet.addParaWithInserts("spend_time_together_after",targetLord,textPanel,options);
        feast.setHeldDate(true);
        options.clearOptions();
        DialogSet.addOptionWithInserts("option_give_gift","spend_time_together_hint",OptionId.OFFER_GIFT,targetLord,textPanel,options);
        DialogSet.addOptionWithInserts("option_dont_give_gift",null,OptionId.ASK_QUESTION,targetLord,textPanel,options);
    }
    private void optionSelected_OFFER_GIFT(String optionText, Object optionData,PersonAPI player,boolean willEngage,boolean hostile, LordEvent feast,OptionId option){
        nextState = OptionId.OFFER_GIFT_SELECTION;
        options.clearOptions();
        String clothing = targetLord.getLordAPI().getGender() == FullName.Gender.FEMALE ? "dress" : "suit";
        CargoAPI cargo = Global.getSector().getPlayerFleet().getCargo();
        options.addOption("An alpha core", "alpha_core");
        options.setTooltip("alpha_core", "Consumes 1 alpha core");
        if (cargo.getCommodityQuantity("alpha_core") < 1) {
            options.setEnabled("alpha_core", false);
        }
        options.addOption("A high-quality handgun", "hand_weapons");
        options.setTooltip("hand_weapons", "Consumes 100 Heavy Armaments");
        if (cargo.getCommodityQuantity("hand_weapons") < 100) {
            options.setEnabled("hand_weapons", false);
        }
        options.addOption("A butter sampler platter", "food");
        options.setTooltip("food", "Consumes 100 Food");
        if (cargo.getCommodityQuantity("food") < 100) {
            options.setEnabled("food", false);
        }
        options.addOption("The latest luxury " + clothing + " from Chicomoztoc", "luxury_goods");
        options.setTooltip("luxury_goods", "Consumes 100 Luxury Goods");
        if (cargo.getCommodityQuantity("luxury_goods") < 100) {
            options.setEnabled("luxury_goods", false);
        }
        options.addOption("Fresh Volturnian Lobster", "lobster");
        options.setTooltip("lobster", "Consumes 100 Volturnian Lobster");
        if (cargo.getCommodityQuantity("lobster") < 100) {
            options.setEnabled("lobster", false);
        }
        options.addOption("A pack of psychedelics", "drugs");
        options.setTooltip("drugs", "Consumes 100 Recreational Drugs");
        if (cargo.getCommodityQuantity("drugs") < 100) {
            options.setEnabled("drugs", false);
        }
        options.addOption("The latest Tri-pad DLC", "domestic_goods");
        options.setTooltip("domestic_goods", "Consumes 100 Domestic Goods");
        if (cargo.getCommodityQuantity("domestic_goods") < 100) {
            options.setEnabled("domestic_goods", false);
        }
        options.addOption("Never mind", OptionId.ASK_QUESTION);
    }
    private void optionSelected_OFFER_GIFT_SELECTION(String optionText, Object optionData,PersonAPI player,boolean willEngage,boolean hostile, LordEvent feast,OptionId option){
        String giftItem = (String) optionData;
        int quantityGiven = 100;
        if (giftItem.equals("alpha_core")) quantityGiven = 1;
        Global.getSector().getPlayerFleet().getCargo().removeCommodity(giftItem, quantityGiven);
        if (giftItem.equals(targetLord.getTemplate().preferredItemId)) {
            textPanel.addPara(StringUtil.getString(CATEGORY, "receive_gift_like"));
            textPanel.addPara(StringUtil.getString(
                    CATEGORY, "relation_increase",
                    targetLord.getLordAPI().getNameString(), "10"), Color.GREEN);
            targetLord.getLordAPI().getRelToPlayer().adjustRelationship(0.1f, null);
            targetLord.setRomanticActions(targetLord.getRomanticActions() + 1);
        } else {
            textPanel.addPara(StringUtil.getString(CATEGORY, "receive_gift_dislike"));
            textPanel.addPara(StringUtil.getString(
                    CATEGORY, "relation_decrease",
                    targetLord.getLordAPI().getNameString(), "10"), Color.RED);
            targetLord.getLordAPI().getRelToPlayer().adjustRelationship(-0.1f, null);
        }
        optionSelected(null, OptionId.ASK_QUESTION);
    }
    private void optionSelected_START_WEDDING(String optionText, Object optionData,PersonAPI player,boolean willEngage,boolean hostile, LordEvent feast,OptionId option){
        String hostName = feast.getOriginator().getLordAPI().getNameString();
        if (feast.getOriginator().equals(feast.getWeddingCeremonyTarget())
                && !feast.getParticipants().isEmpty()) {
            hostName = feast.getParticipants().get(0).getLordAPI().getNameString();
        }
        HashMap<String,String> inserts = new HashMap<>();
        inserts.put("%c0",feast.getWeddingCeremonyTarget().getLordAPI().getNameString());
        inserts.put("%c1",hostName);
        DialogSet.addParaWithInserts("marriage_ceremony",targetLord,textPanel,options,inserts);
        feast.getWeddingCeremonyTarget().setMarried(true);
        feast.getWeddingCeremonyTarget().setSpouse(Global.getSector().getPlayerPerson().getId());
        LordController.getPlayerLord().setMarried(true);
        LordController.getPlayerLord().setSpouse(feast.getWeddingCeremonyTarget().getLordAPI().getId());
        optionSelected(null, OptionId.INIT);
    }
    private void optionSelected_SUGGEST_MARRIAGE(String optionText, Object optionData,PersonAPI player,boolean willEngage,boolean hostile, LordEvent feast,OptionId option){
        DialogSet.addParaWithInserts("marriage_response",targetLord,textPanel,options);
        optionSelected(null, OptionId.ASK_QUESTION);
    }
    private void optionSelected_SUGGEST_JOIN_PARTY(String optionText, Object optionData,PersonAPI player,boolean willEngage,boolean hostile, LordEvent feast,OptionId option){
        DialogSet.addParaWithInserts("join_party_explanation",targetLord,textPanel,options);
        options.clearOptions();
        DialogSet.addOptionWithInserts("option_confirm_join_party",null,OptionId.CONFIRM_TOGGLE_PARTY,targetLord,textPanel,options);
        DialogSet.addOptionWithInserts("option_nevermind_join_party",null,OptionId.ASK_QUESTION,targetLord,textPanel,options);
        options.setShortcut(OptionId.ASK_QUESTION, 1, false, false, false, true);
    }
    private void optionSelected_SUGGEST_LEAVE_PARTY(String optionText, Object optionData,PersonAPI player,boolean willEngage,boolean hostile, LordEvent feast,OptionId option){
        DialogSet.addParaWithInserts("leave_party_explanation",targetLord,textPanel,options);
        options.clearOptions();
        DialogSet.addOptionWithInserts("option_confirm_leave_party",null,OptionId.CONFIRM_TOGGLE_PARTY,targetLord,textPanel,options);
        DialogSet.addOptionWithInserts("option_nevermind_leave_party",null,OptionId.ASK_QUESTION,targetLord,textPanel,options);
        options.setShortcut(OptionId.ASK_QUESTION, 1, false, false, false, true);
    }
    private void optionSelected_CONFIRM_TOGGLE_PARTY(String optionText, Object optionData,PersonAPI player,boolean willEngage,boolean hostile, LordEvent feast,OptionId option){
        if (targetLord.getCurrAction() == LordAction.COMPANION) {
            Misc.setMercenary(targetLord.getLordAPI(), false);
            Global.getSector().getPlayerFleet().getFleetData().removeOfficer(targetLord.getLordAPI());
            for (FleetMemberAPI ship : Global.getSector().getPlayerFleet().getFleetData().getMembersListCopy()) {
                if (targetLord.getLordAPI().equals(ship.getCaptain())) ship.setCaptain(null);
            }
            targetLord.getLordAPI().setFleet(targetLord.getOldFleet());
            targetLord.setOldFleet(null);
            targetLord.setCurrAction(null);
            targetLord.getFleet().fadeInIndicator();
            targetLord.getFleet().setHidden(false);
        } else {
            EventController.removeFromAllEvents(targetLord);
            targetLord.setCurrAction(LordAction.COMPANION);
            targetLord.getFleet().setVelocity(0, 0);
            targetLord.getFleet().fadeOutIndicator();
            targetLord.getFleet().setHidden(true);
            targetLord.getFleet().clearAssignments();
            targetLord.setOldFleet(targetLord.getFleet());
            MemoryAPI mem = targetLord.getFleet().getMemoryWithoutUpdate();
            Misc.setFlagWithReason(mem,
                    MemFlags.FLEET_BUSY, BUSY_REASON, true, 1e7f);
            Misc.setFlagWithReason(mem,
                    MemFlags.FLEET_IGNORES_OTHER_FLEETS, BUSY_REASON, true, 1e7f);
            Global.getSector().getPlayerFleet().getFleetData().addOfficer(targetLord.getLordAPI());
            Misc.setMercenary(targetLord.getLordAPI(), true);
            Misc.setMercHiredNow(targetLord.getLordAPI());
            targetLord.getLordAPI().setFaction(targetLord.getOldFleet().getFaction().getId());
        }
        optionSelected(null, OptionId.ASK_QUESTION);
    }
    private void optionSelected_SWAY_PROPOSAL_COUNCIL(String optionText, Object optionData,PersonAPI player,boolean willEngage,boolean hostile, LordEvent feast,OptionId option){
        FactionAPI faction = targetLord.getFaction();
        if (option == OptionId.SWAY_PROPOSAL_PLAYER) {
            proposal = PoliticsController.getProposal(LordController.getPlayerLord());
            swayFor = true;
            //swayFor
            support_SWAY_PROPOSAL_COUNCIL_FOR( optionText,  optionData, player, willEngage, hostile,  feast, option);
        } else {
            proposal = PoliticsController.getCurrProposal(faction);
            swayFor = false;
            //!swayFor
            support_SWAY_PROPOSAL_COUNCIL_AGAINST( optionText,  optionData, player, willEngage, hostile,  feast, option);
        }
    }
    private void support_SWAY_PROPOSAL_COUNCIL_AGAINST(String optionText, Object optionData, PersonAPI player, boolean willEngage, boolean hostile, LordEvent feast, OptionId option){
        FactionAPI faction = targetLord.getFaction();
        int opinion = PoliticsController.getApproval(targetLord, proposal, false).one;
        // lord can either agree/refuse outright, suggest a bribe, or suggest player support their proposal
        Random rand = new Random(targetLord.getLordAPI().getId().hashCode()
                + Global.getSector().getClock().getTimestamp());
        LawProposal lordProposal = PoliticsController.getProposal(targetLord);
        int relation = RelationController.getRelation(targetLord, LordController.getPlayerLord());
        int agreeChance = 0;
        int bribeChance = 0;
        int bargainChance = 0;
        opinion *= -1;
        if (!targetLord.isSwayed() && opinion > -20) {
            agreeChance = 10 * (relation / 10 + opinion + 12); // agree at -10 or above
            bribeChance = 25 + relation;
            switch (targetLord.getPersonality()) {
                case UPSTANDING:
                    bribeChance /= 4;
                    break;
                case MARTIAL:
                    bribeChance /= 2;
                    break;
                case CALCULATING:
                    bribeChance *= 2;
                    break;
            }
            // lord needs a proposal worth supporting to bargain
            if (lordProposal != null && lordProposal.getSupporters().size() > 1
                    && !lordProposal.isPlayerSupports()
                    && relation > Utils.getThreshold(RepLevel.SUSPICIOUS)) {
                bargainChance = 100;
            }
        }

        targetLord.setSwayed(true);
        if (rand.nextInt(100) < agreeChance) {
            if (opinion > 0) {
                //DialogSet.addParaWithInserts("against_accept_sway_redundant",targetLord,textPanel,options);
                textPanel.addPara(StringUtil.getString(CATEGORY, "option_accept_sway_redundant"));
            } else {
                //DialogSet.addParaWithInserts("against_accept_sway",targetLord,textPanel,options);
                textPanel.addPara(StringUtil.getString(CATEGORY, "option_accept_sway"));
            }
            proposal.getPledgedAgainst().add(targetLord.getLordAPI().getId());
            PoliticsController.updateProposal(proposal);
            optionSelected(null, OptionId.ASK_QUESTION);
        } else if (rand.nextInt(100) < bargainChance) {
            //DialogSet.addParaWithInserts("ERROR",targetLord,textPanel,options);
            options.clearOptions();
            //DialogSet.addOptionWithInserts("against_bargain_sway_support",null,null,targetLord,textPanel,options);
            //DialogSet.addOptionWithInserts("ERROR",null,null,targetLord,textPanel,options);
            textPanel.addPara(StringUtil.getString(CATEGORY, "option_bargain_sway_support"));
            options.clearOptions();
            bargainAmount = "proposal"; // TODO make some flags
            options.addOption("Pledge to support proposal: " + lordProposal.getTitle(), OptionId.SWAY_PROPOSAL_BARGAIN);
            options.addOption("Refuse", OptionId.ASK_QUESTION);


        } else if (rand.nextInt(100) < bribeChance) {
            //DialogSet.addParaWithInserts("against_bargain_sway_money",targetLord,textPanel,options);
            options.clearOptions();
            //DialogSet.addOptionWithInserts("ERROR",null,null,targetLord,textPanel,options);
            //DialogSet.addOptionWithInserts("ERROR",null,null,targetLord,textPanel,options);
            textPanel.addPara(StringUtil.getString(CATEGORY, "option_bargain_sway_money"));
            bargainAmount = "credits";
            options.clearOptions();
            options.addOption("Offer 100,000 credits", OptionId.SWAY_PROPOSAL_BARGAIN);
            float playerWealth = Global.getSector().getPlayerFleet().getCargo().getCredits().get();
            if (playerWealth < 100000) {
                options.setEnabled(OptionId.SWAY_PROPOSAL_BARGAIN, false);
                options.setTooltip(OptionId.SWAY_PROPOSAL_BARGAIN, "Insufficient funds.");
            }
            options.addOption("Refuse", OptionId.ASK_QUESTION);

        } else {
            //DialogSet.addParaWithInserts("ERROR",targetLord,textPanel,options);
            textPanel.addPara(StringUtil.getString(CATEGORY, "option_refuse_sway"));
            optionSelected(null, OptionId.ASK_QUESTION);
        }
    }
    private void support_SWAY_PROPOSAL_COUNCIL_FOR(String optionText, Object optionData, PersonAPI player, boolean willEngage, boolean hostile, LordEvent feast, OptionId option){
        FactionAPI faction = targetLord.getFaction();
        int opinion = PoliticsController.getApproval(targetLord, proposal, false).one;
        // lord can either agree/refuse outright, suggest a bribe, or suggest player support their proposal
        Random rand = new Random(targetLord.getLordAPI().getId().hashCode()
                + Global.getSector().getClock().getTimestamp());
        LawProposal lordProposal = PoliticsController.getProposal(targetLord);
        int relation = RelationController.getRelation(targetLord, LordController.getPlayerLord());
        int agreeChance = 0;
        int bribeChance = 0;
        int bargainChance = 0;
        if (!targetLord.isSwayed() && opinion > -20) {
            agreeChance = 10 * (relation / 10 + opinion + 12); // agree at -10 or above
            bribeChance = 25 + relation;
            switch (targetLord.getPersonality()) {
                case UPSTANDING:
                    bribeChance /= 4;
                    break;
                case MARTIAL:
                    bribeChance /= 2;
                    break;
                case CALCULATING:
                    bribeChance *= 2;
                    break;
            }
            // lord needs a proposal worth supporting to bargain
            if (lordProposal != null && lordProposal.getSupporters().size() > 1
                    && !lordProposal.isPlayerSupports()
                    && relation > Utils.getThreshold(RepLevel.SUSPICIOUS)) {
                bargainChance = 100;
            }
        }

        targetLord.setSwayed(true);
        if (rand.nextInt(100) < agreeChance) {
            if (opinion > 0) {
                //DialogSet.addParaWithInserts("ERROR",targetLord,textPanel,options);
                textPanel.addPara(StringUtil.getString(CATEGORY, "option_accept_sway_redundant"));
            } else {
                //DialogSet.addParaWithInserts("ERROR",targetLord,textPanel,options);
                textPanel.addPara(StringUtil.getString(CATEGORY, "option_accept_sway"));
            }
            proposal.getPledgedFor().add(targetLord.getLordAPI().getId());
            PoliticsController.updateProposal(proposal);
            optionSelected(null, OptionId.ASK_QUESTION);
        } else if (rand.nextInt(100) < bargainChance) {
            //DialogSet.addParaWithInserts("ERROR",targetLord,textPanel,options);
            options.clearOptions();
            //DialogSet.addOptionWithInserts("ERROR",null,null,targetLord,textPanel,options);
            //DialogSet.addOptionWithInserts("ERROR",null,null,targetLord,textPanel,options);
            textPanel.addPara(StringUtil.getString(CATEGORY, "option_bargain_sway_support"));
            options.clearOptions();
            bargainAmount = "proposal"; // TODO make some flags
            options.addOption("Pledge to support proposal: " + lordProposal.getTitle(), OptionId.SWAY_PROPOSAL_BARGAIN);
            options.addOption("Refuse", OptionId.ASK_QUESTION);


        } else if (rand.nextInt(100) < bribeChance) {
            //DialogSet.addParaWithInserts("ERROR",targetLord,textPanel,options);
            options.clearOptions();
            //DialogSet.addOptionWithInserts("ERROR",null,null,targetLord,textPanel,options);
            //DialogSet.addOptionWithInserts("ERROR",null,null,targetLord,textPanel,options);
            textPanel.addPara(StringUtil.getString(CATEGORY, "option_bargain_sway_money"));
            bargainAmount = "credits";
            options.clearOptions();
            options.addOption("Offer 100,000 credits", OptionId.SWAY_PROPOSAL_BARGAIN);
            float playerWealth = Global.getSector().getPlayerFleet().getCargo().getCredits().get();
            if (playerWealth < 100000) {
                options.setEnabled(OptionId.SWAY_PROPOSAL_BARGAIN, false);
                options.setTooltip(OptionId.SWAY_PROPOSAL_BARGAIN, "Insufficient funds.");
            }
            options.addOption("Refuse", OptionId.ASK_QUESTION);

        } else {
            //DialogSet.addParaWithInserts("ERROR",targetLord,textPanel,options);
            textPanel.addPara(StringUtil.getString(CATEGORY, "option_refuse_sway"));
            optionSelected(null, OptionId.ASK_QUESTION);
        }
    }
    private void optionSelected_SWAY_PROPOSAL_BARGAIN(String optionText, Object optionData,PersonAPI player,boolean willEngage,boolean hostile, LordEvent feast,OptionId option){
        textPanel.addPara(StringUtil.getString(CATEGORY, "option_accept_sway_bargain"));
        if (bargainAmount.equals("credits")) {
            Global.getSector().getPlayerFleet().getCargo().getCredits().subtract(100000);
            textPanel.addPara("Lost " + 100000 + " credits.", Color.RED);
        } else {
            LawProposal bargainProposal = PoliticsController.getProposal(targetLord);
            bargainProposal.getPledgedFor().add(LordController.getPlayerLord().getLordAPI().getId());
            bargainProposal.setPlayerSupports(true);
        }
        if (swayFor) {
            proposal.getPledgedFor().add(targetLord.getLordAPI().getId());
        } else {
            proposal.getPledgedAgainst().add(targetLord.getLordAPI().getId());
        }
        PoliticsController.updateProposal(proposal);
        optionSelected(null, OptionId.ASK_QUESTION);
    }
    private void optionSelected_ASK_LOCATION(String optionText, Object optionData,PersonAPI player,boolean willEngage,boolean hostile, LordEvent feast,OptionId option){
        textPanel.addParagraph(StringUtil.getString(CATEGORY, "accept_ask_location"));
        options.clearOptions();
        lordsReference.clear();
        nextState = OptionId.ASK_LOCATION_CHOICE;
        ArrayList<Lord> toAdd = new ArrayList<>();
        for (Lord lord : LordController.getLordsList()) {
            if (!lord.equals(targetLord)
                    && lord.getLordAPI().getFaction().equals(targetLord.getLordAPI().getFaction())) {
                toAdd.add(lord);
            }
        }
        Utils.canonicalLordSort(toAdd);
        for (Lord lord : toAdd) {
            String desc = lord.getTitle() + " " + lord.getLordAPI().getNameString();
            options.addOption(desc, new Object());
            lordsReference.put(desc, lord);
        }
        options.addOption(StringUtil.getString(CATEGORY, "option_nevermind"), OptionId.ASK_QUESTION);
    }
    private void optionSelected_ASK_LOCATION_CHOICE(String optionText, Object optionData,PersonAPI player,boolean willEngage,boolean hostile, LordEvent feast,OptionId option){
        Lord lord = lordsReference.get(optionText);
        if (lord != null && lord.getLordAPI().getFleet().isAlive()) {
            textPanel.addParagraph("I heard that " + optionText + " was last sighted around " + Utils.getNearbyDescription(
                    lord.getLordAPI().getFleet()));
        } else {
            textPanel.addParagraph("I dont know where " + optionText + " is.");
        }
        optionSelected(null, OptionId.INIT);
    }
    private void optionSelected_SPEAK_PRIVATELY(String optionText, Object optionData,PersonAPI player,boolean willEngage,boolean hostile, LordEvent feast,OptionId option){
        /*ok... ok, what is required here?
        * COMPLEAT
        *
        *   will speak privately:
        *       note: there is a offset by ID % 5. this requires a new condition. something like asdsadsadsasad...
        *           this would make no sense. instead, simply use the rep level base.
                case UPSTANDING:
                    rep min: RepLevel.WELCOMING) + 5;
                case MARTIAL:
                    rep min: RepLevel.WELCOMING);
                case CALCULATING:
                    rep min: RepLevel.FAVORABLE) + 5;
                case QUARRELSOME:
                    rep min: RepLevel.FAVORABLE);
        *
        * (done) conditions:
        *   lordHasLiege
        *   playerHasLiege
        *
        *   //ok, so: this would work by looking at all lords, and getting the number of valid lords. then, it would be true if that vale is between min / max.
        *   validLordNumbers{
        *       "min": int
        *       "max": int
        *       "rules" : {
        *
        *       }
        *   }
        * show:
        *   in DialogGroupOption: run 'show' on the target lord...? (does that even work...???????)
        *   ok... so... does it???????
        *   ... no. it could be used to hide the resalting options, HOWEVER: it cannot be used to hide the shown option.
        *   so.. I need the following:
        * (done)line data:
        *   TARGET_LEAGE_NAME
        *
        * lines:
        *   if(will speek privitly): "accept_speak_privately"
        *   (done) else: "refuse_speak_privately_PERSONALITY
        *       -returns to greetings options.
        *
        * options (only if "accept_speak_privately"):
        *   (done)!same faction
        *       lord has ledge
        *           "option_ask_liege_opinion": OptionId.ASK_LIEGE_OPINION
        *       lord no ledge
        *           "option_ask_liege_opinion_decentralized": OptionId.ASK_LIEGE_OPINION
        *   (done)---lord has firend that player corted---
        *       "option_ask_friend_preferences" : OptionId.ASK_FRIEND_FAVORITE_GIFT
        *   (done)nevermind: OptionId.INIT
        * will not speak privately:
        *   (done)copys OptionId.INIT
        *
        *
        * NOTE: (DONE) every lord has there own 'im not talking to you' dialog. I will need to add that to all starlords.
        * */

        if (targetLord.willSpeakPrivately()) {
            options.clearOptions();
            textPanel.addParagraph(StringUtil.getString(CATEGORY, "accept_speak_privately"));
            options.addOption(StringUtil.getString(CATEGORY, "option_ask_worldview"), OptionId.ASK_WORLDVIEW);
            if (!targetLord.getLordAPI().getFaction().equals(Global.getSector().getPlayerFaction())) {
                if (targetLord.getLiegeName() != null) {
                    options.addOption(StringUtil.getString(CATEGORY, "option_ask_liege_opinion", targetLord.getLiegeName()), OptionId.ASK_LIEGE_OPINION);
                } else {
                    options.addOption(StringUtil.getString(CATEGORY, "option_ask_liege_opinion_decentralized"), OptionId.ASK_LIEGE_OPINION);
                }
            }
            boolean hasCourtedFriend = false;
            for (Lord friend : LordController.getLordsList()) {
                if (RelationController.getRelation(targetLord, friend) > 30
                        && friend.isCourted() && targetLord != friend) {
                    hasCourtedFriend = true;
                }
            }
            if (hasCourtedFriend) {
                options.addOption(StringUtil.getString(CATEGORY, "option_ask_friend_preferences"),
                        OptionId.ASK_FRIEND_FAVORITE_GIFT);
            }
            options.addOption(StringUtil.getString(CATEGORY, "option_nevermind"), OptionId.INIT);
        } else {
            textPanel.addParagraph(StringUtil.getString(CATEGORY, "refuse_speak_privately_" +  targetLord.getPersonality().toString().toLowerCase()));
        }
    }
    private void optionSelected_ASK_WORLDVIEW(String optionText, Object optionData,PersonAPI player,boolean willEngage,boolean hostile, LordEvent feast,OptionId option){
        /*4 diffretn strings, just for basic data on lords.
        * have the additional line addon: one line link to updated lord info. if lord info not known, have the data not be null.
        * addons:
        *   (the 2 addons)
        *   set personality known: true / false
        *       includes a bit of data that add text for updated lord personality --provided
        * conditions:
        *   isPersonalityKnown*/
        textPanel.addParagraph(StringUtil.getString(
                CATEGORY, "worldview_" + targetLord.getPersonality().toString().toLowerCase()));
        if (!targetLord.isPersonalityKnown()) {
            targetLord.setPersonalityKnown(true);
            textPanel.addParagraph("Updated lord info!", Color.GREEN);
        }
    }
    private void optionSelected_ASK_LIEGE_OPINION(String optionText, Object optionData,PersonAPI player,boolean willEngage,boolean hostile, LordEvent feast,OptionId option){
        int loyalty = RelationController.getLoyalty(targetLord);
        if (targetLord.getLiegeName() == null) {
            textPanel.addParagraph(StringUtil.getString(CATEGORY, "liege_opinion_decentralized_" + relToString(loyalty)));
        } else {
            textPanel.addParagraph(StringUtil.getString(CATEGORY, "liege_opinion_" + relToString(loyalty), targetLord.getLiegeName()));
        }
        FactionAPI faction = Utils.getRecruitmentFaction();
        if ((targetLord.isMarried() || loyalty < Utils.getThreshold(RepLevel.FAVORABLE))
                && !faction.equals(targetLord.getLordAPI().getFaction())) {
            options.clearOptions();
            options.addOption(StringUtil.getString(CATEGORY, "suggest_defect", faction.getDisplayNameWithArticle()), OptionId.SUGGEST_DEFECT);
            options.addOption("Interesting.", OptionId.INIT);
        }
    }
    private void optionSelected_ASK_FRIEND_FAVORITE_GIFT(String optionText, Object optionData,PersonAPI player,boolean willEngage,boolean hostile, LordEvent feast,OptionId option){
        textPanel.addParagraph(StringUtil.getString(CATEGORY, "prepare_advise_friend_gift"));
        options.clearOptions();
        nextState = OptionId.ASK_FRIEND_FAVORITE_GIFT_LIST;
        for (Lord friend : LordController.getLordsList()) {
            if (RelationController.getRelation(targetLord, friend) > 30
                    && friend.isCourted() && targetLord != friend) {
                options.addOption(friend.getLordAPI().getNameString(), friend);
            }
        }
        options.addOption(StringUtil.getString(CATEGORY, "option_nevermind"), OptionId.INIT);
    }
    private void optionSelected_ASK_FRIEND_FAVORITE_GIFT_LIST(String optionText, Object optionData,PersonAPI player,boolean willEngage,boolean hostile, LordEvent feast,OptionId option){
        if (optionData instanceof Lord) {
            Lord friend = (Lord) optionData;
            String interest = "";
            switch (friend.getTemplate().preferredItemId) {
                case "alpha_core":
                    interest = "AI research";
                    break;
                case "hand_weapons":
                    interest = "rare weapons";
                    break;
                case "food":
                    interest = "exotic butters";
                    break;
                case "luxury_goods":
                    interest = "fashion";
                    break;
                case "lobster":
                    interest = "Sindrian delicacies";
                    break;
                case "drugs":
                    interest = "drugs";
                    break;
                case "domestic_goods":
                    interest = "technological gadgets";
                    break;
            }
            textPanel.addParagraph(StringUtil.getString(CATEGORY, "advise_friend_gift",
                    friend.getLordAPI().getName().getFirst(), interest));
        }
        optionSelected(null, OptionId.INIT);
    }
    private void optionSelected_SUGGEST_DEFECT(String optionText, Object optionData,PersonAPI player,boolean willEngage,boolean hostile, LordEvent feast,OptionId option){
        textPanel.addParagraph(StringUtil.getString(CATEGORY, "consider_defect"));
        nextState = OptionId.JUSTIFY_DEFECT;
        options.clearOptions();
        options.addOption(StringUtil.getString(CATEGORY, "suggest_defection_calculating"), OptionId.BARGAIN_DEFECT);
        options.addOption(StringUtil.getString(CATEGORY, "suggest_defection_upstanding"), new Object());
        options.addOption(StringUtil.getString(CATEGORY, "suggest_defection_martial"), new Object());
        options.addOption(StringUtil.getString(CATEGORY, "suggest_defection_quarrelsome"), new Object());
        options.addOption(StringUtil.getString(CATEGORY, "suggest_defection_abort"), OptionId.INIT);
    }
    private void optionSelected_JUSTIFY_DEFECT(String optionText, Object optionData,PersonAPI player,boolean willEngage,boolean hostile, LordEvent feast,OptionId option){
        // compute justification strength
        // upstanding checks player colony stability and count, marshal checks player level and fleet size,
        // calculating does bribes, quarrelsome auto-passes
        FactionAPI faction = Utils.getRecruitmentFaction();
        justification = CLAIM_CALCULATING; // calculating has an additional state so we don't know the option text for sure
        String justificationStr = "offer";
        if (optionText.equals(StringUtil.getString(CATEGORY, "suggest_defection_quarrelsome"))) {
            justification = CLAIM_QUARRELSOME;
            justificationStr = "history";
        } else if (optionText.equals(StringUtil.getString(CATEGORY, "suggest_defection_upstanding"))) {
            justification = CLAIM_UPSTANDING;
            justificationStr = "colonial stability and success";
        } else if (optionText.equals(StringUtil.getString(CATEGORY, "suggest_defection_martial"))) {
            justification = CLAIM_MARTIAL;
            justificationStr = "military and personal strength";
        }
        if (justification.equals(CLAIM_CALCULATING)) {
            bargainAmount = optionText;
            if (optionText.contains("500,000 Credits")
                    || optionText.contains(StringUtil.getString(CATEGORY_TITLES, "title_default_1"))) {
                claimStrength = SOMEWHAT_JUSTIFIED;
            } else if (optionText.contains("2,000,000 Credits")
                    || optionText.contains(StringUtil.getString(CATEGORY_TITLES, "title_default_2"))) {
                claimStrength = FULLY_JUSTIFIED;
            } else {
                claimStrength = COMPLETELY_UNJUSTIFIED;
            }
        } else {
            bargainAmount = null;
            claimStrength = DefectionUtils.computeClaimJustification(justification, faction);
        }
        String claimStr = "completely unjustified";
        Color claimColor = Color.RED;
        if (claimStrength == FULLY_JUSTIFIED) {
            claimStr = "fully justified";
            claimColor = Color.GREEN;
        } else if (claimStrength == SOMEWHAT_JUSTIFIED) {
            claimStr = "partially justified";
            claimColor = Color.YELLOW;
        }
        textPanel.addPara("Based on your " + justificationStr + ", your claim is seen as " + claimStr + ".", Color.ORANGE, claimColor, claimStr);
        // Breakdown defection factors - base personality, faction legitimacy, faction loyalty vs player relation, ties with subordinates, justification effect, and rng
        // use player loyalty here even if recruiting for other factions
        if (DefectionUtils.computeFactionLegitimacy(faction) > 7) {
            textPanel.addPara("Hmm, your faction is well-established and has a legitimate claim to unite the sector.");
        } else {
            textPanel.addPara("Hmm, your faction is still a minor player and would be a risk to join.");
        }
        if (DefectionUtils.computeRelativeFactionPreference(targetLord, Global.getSector().getPlayerFaction()) > 0) {
            textPanel.addPara("It's true that I consider your faction more respectable than my own.");
        } else {
            textPanel.addPara("Frankly, I prefer my faction over yours.");
        }
        if (DefectionUtils.computeRelativeLordPreference(targetLord, faction) > 0) {
            textPanel.addPara("I would prefer working with your lords over my present company.");
        } else {
            textPanel.addPara("I have good friends that I would not want to lose.");
        }
        if (justification.contains(targetLord.getPersonality().toString().toLowerCase()) && claimStrength > 0)  {
            textPanel.addParagraph("Additionally, I must admit that your justification is not without merit.");
        } else {
            textPanel.addParagraph("Additionally, I find your justification unconvincing.");
        }
        textPanel.addParagraph("Lastly, changing loyalties is no small decision. I must consider the consequences on my reputation carefully.");
        options.clearOptions();
        options.addOption(StringUtil.getString(CATEGORY, "confirm_suggest_defect"), OptionId.CONFIRM_SUGGEST_DEFECT);
        options.setTooltip(OptionId.CONFIRM_SUGGEST_DEFECT, "-10 relations if failed");
        options.addOption(StringUtil.getString(CATEGORY, "abort_suggest_defect"), OptionId.INIT);
    }
    private void optionSelected_BARGAIN_DEFECT(String optionText, Object optionData,PersonAPI player,boolean willEngage,boolean hostile, LordEvent feast,OptionId option){
        // 2 mil gold, 500k gold, or titles
        textPanel.addParagraph(StringUtil.getString(CATEGORY, "bargain_defect"));
        options.clearOptions();
        float playerWealth = Global.getSector().getPlayerFleet().getCargo().getCredits().get();
        if (playerWealth > SMALL_BRIBE) {
            options.addOption("500,000 Credits", new Object());
        }
        if (playerWealth > LARGE_BRIBE) {
            options.addOption("2,000,000 Credits", new Object());
        }
        if (Misc.getCommissionFaction() == null) {
            int numRankTwo = 0;
            int numRankOne = 0;
            for (Lord lord2 : LordController.getLordsList()) {
                if (lord2.getLordAPI().getFaction().equals(Global.getSector().getPlayerFaction())) {
                    if (lord2.getRanking() == 1) numRankOne++;
                    if (lord2.getRanking() == 2) numRankTwo++;
                }
            }
            if (numRankOne < 3) {
                options.addOption("The title of " + StringUtil.getString(CATEGORY_TITLES, "title_default_1"), new Object());
            }
            if (numRankTwo == 0) {
                options.addOption("The title of " + StringUtil.getString(CATEGORY_TITLES, "title_default_2"), new Object());
            }
        }
        options.addOption(StringUtil.getString(CATEGORY, "bargain_defect_nothing"), new Object());
        nextState = OptionId.JUSTIFY_DEFECT;
    }
    private void optionSelected_CONFIRM_SUGGEST_DEFECT(String optionText, Object optionData,PersonAPI player,boolean willEngage,boolean hostile, LordEvent feast,OptionId option){
        FactionAPI faction = Utils.getRecruitmentFaction();
        Random rand = new Random(targetLord.getLordAPI().getId().hashCode() + Global.getSector().getClock().getTimestamp());
        int claimConcern = 0;
        if (justification.contains(targetLord.getPersonality().toString().toLowerCase())) claimConcern = 100;
        int baseReluctance = DefectionUtils.getBaseReluctance(targetLord);
        int legitimacyFactor = DefectionUtils.computeFactionLegitimacy(faction);
        int loyaltyFactor = DefectionUtils.computeRelativeFactionPreference(targetLord, Global.getSector().getPlayerFaction());
        int companionFactor = DefectionUtils.computeRelativeLordPreference(targetLord, faction);
        int randFactor = rand.nextInt(10);
        int marriedFactor = targetLord.isMarried() ? 200 : 0;
        //log.info("DEBUG defection: " + baseReluctance + ", " + legitimacyFactor + ", " + loyaltyFactor + ", " + companionFactor + ", " + claimStrength + ", " + randFactor);
        int totalWeight = baseReluctance + legitimacyFactor + loyaltyFactor + companionFactor + marriedFactor + Math.min(claimConcern, claimStrength) + randFactor;
        if (totalWeight > 0) {
            String liegeName = Utils.getLiegeName(faction);
            textPanel.addPara(StringUtil.getString(CATEGORY, "accept_defect", player.getNameString(), liegeName), Color.GREEN);
            Global.getSoundPlayer().playUISound("ui_char_level_up", 1, 1);
            DefectionUtils.performDefection(targetLord, faction, false);
            // resolve bribe effects
            if (bargainAmount != null) {
                if (bargainAmount.contains("Credits")) {
                    if (claimStrength == FULLY_JUSTIFIED) {
                        Global.getSector().getPlayerFleet().getCargo().getCredits().subtract(LARGE_BRIBE);
                        textPanel.addPara("Lost " + LARGE_BRIBE + " credits.", Color.RED);
                    }
                    if (claimStrength == SOMEWHAT_JUSTIFIED) {
                        Global.getSector().getPlayerFleet().getCargo().getCredits().subtract(SMALL_BRIBE);
                        textPanel.addPara("Lost " + SMALL_BRIBE + " credits.", Color.RED);
                    }
                } else if (bargainAmount.contains("title")) {
                    if (claimStrength == FULLY_JUSTIFIED) targetLord.setRanking(2);
                    if (claimStrength == SOMEWHAT_JUSTIFIED) targetLord.setRanking(1);
                }
            }
        } else {
            textPanel.addPara(StringUtil.getString(CATEGORY, "refuse_defect"));
            Global.getSoundPlayer().playUISound("ui_rep_drop", 1, 1);
            targetLord.getLordAPI().getRelToPlayer().adjustRelationship(-0.1f, null);
            textPanel.addParagraph(StringUtil.getString(
                    CATEGORY, "relation_decrease", targetLord.getLordAPI().getNameString(), "10"), Color.RED);

        }
        optionSelected(null, OptionId.INIT);
    }
    private void optionSelected_ASK_QUEST(String optionText, Object optionData,PersonAPI player,boolean willEngage,boolean hostile, LordEvent feast,OptionId option){
        boolean questGiven = false;
        if (!QuestController.isQuestGiven(targetLord)) {
            MarketAPI tmp = lordFleet.getMarket();
            MarketAPI tmp2 = targetLord.getLordAPI().getMarket();
            SectorEntityToken marketEntity = targetLord.getClosestBase();
            MarketAPI newMarket;
            if (marketEntity != null) {
                newMarket = marketEntity.getMarket();
            } else {  // last resort, just make it some market for now
                newMarket = Global.getSector().getEconomy().getMarketsCopy().get(0);
            }
            lordFleet.setMarket(newMarket);
            targetLord.getLordAPI().setMarket(newMarket);
            ArrayList<Misc.Token> params = new ArrayList<>();
            params.add(new Misc.Token(QuestController.getQuestId(targetLord), Misc.TokenType.LITERAL));
            params.add(new Misc.Token("false", Misc.TokenType.LITERAL));
            new BeginMission().execute("", dialog, params, new HashMap<String, MemoryAPI>());
            //log.info("DEBUG: Creating quest of type " + params.get(0).toString());
            BaseHubMission mission = (BaseHubMission) Global.getSector().getMemoryWithoutUpdate().get(TEMP_MISSION_KEY);
            if (mission != null && !(mission instanceof BaseCustomBounty)) {  // TODO bounties dont seem to work
                MissionPreviewIntelPlugin intel = new MissionPreviewIntelPlugin(mission);
                Global.getSector().getIntelManager().addIntel(intel);
                questGiven = true;
                textPanel.addParagraph(StringUtil.getString(CATEGORY, "quest_available"));
                textPanel.addPara("New intel added!", Color.GREEN);
            }
            QuestController.setQuestGiven(targetLord, true);
            lordFleet.setMarket(tmp);
            targetLord.getLordAPI().setMarket(tmp2);
        }

        if (!questGiven) {
            textPanel.addParagraph(StringUtil.getString(CATEGORY, "no_quest_available"));
        }
    }
    private void optionSelected_SUGGEST_ACTION(String optionText, Object optionData,PersonAPI player,boolean willEngage,boolean hostile, LordEvent feast,OptionId option){
        boolean isBusy = false;
        // check if lord is leading an event or dead
        LordEvent campaign = EventController.getCurrentCampaign(targetLord.getLordAPI().getFaction());
        if ((feast != null && feast.getOriginator().equals(targetLord))
                || (campaign != null && campaign.getOriginator().equals(targetLord))
                || lordFleet.isEmpty() || targetLord.getCurrAction() == LordAction.COMPANION) {
            isBusy = true;
        }

        if (!targetLord.isMarried() && targetLord.getLordAPI().getRelToPlayer().isAtBest(RepLevel.NEUTRAL)) {
            textPanel.addParagraph(StringUtil.getString(CATEGORY, "refuse_suggest_action_relations"));
        } else if (isBusy) {
            textPanel.addParagraph(StringUtil.getString(CATEGORY, "refuse_suggest_action_busy"));
        } else {
            // TODO relations tooltips
            options.clearOptions();
            if (targetLord.isMarried()) {
                textPanel.addParagraph(StringUtil.getString(CATEGORY, "consider_suggest_action_spouse"));
            } else if (targetLord.getFaction().isPlayerFaction()) {
                textPanel.addParagraph(StringUtil.getString(CATEGORY, "consider_suggest_action_subject"));
            } else {
                textPanel.addParagraph(StringUtil.getString(CATEGORY, "consider_suggest_action"));
            }
            if (targetLord.getCurrAction() == LordAction.FOLLOW
                    && Global.getSector().getPlayerFleet().equals(targetLord.getTarget())) {
                options.addOption(StringUtil.getString(CATEGORY, "option_stop_follow_me"), OptionId.STOP_FOLLOW_ME);

            } else {
                options.addOption(StringUtil.getString(CATEGORY, "option_follow_me"), OptionId.FOLLOW_ME);
            }
            options.addOption(StringUtil.getString(CATEGORY, "option_suggest_raid"), OptionId.SUGGEST_RAID);
            options.addOption(StringUtil.getString(CATEGORY, "option_suggest_patrol"), OptionId.SUGGEST_PATROL);
            options.addOption(StringUtil.getString(CATEGORY, "option_suggest_upgrade"), OptionId.SUGGEST_UPGRADE);
            options.addOption(StringUtil.getString(CATEGORY, "option_nevermind"), OptionId.INIT);
        }
    }
    private void optionSelected_FOLLOW_ME(String optionText, Object optionData,PersonAPI player,boolean willEngage,boolean hostile, LordEvent feast,OptionId option){
        LordAI.playerOrder(targetLord, LordAction.FOLLOW, Global.getSector().getPlayerFleet());
        displayAcceptSuggestAction();
        if (!targetLord.getFaction().equals(Global.getSector().getPlayerFaction()) && !targetLord.isMarried()) {
            targetLord.getLordAPI().getRelToPlayer().adjustRelationship(-0.02f, null);
            textPanel.addParagraph(StringUtil.getString(
                    CATEGORY, "relation_decrease", targetLord.getLordAPI().getNameString(), "2"), Color.RED);
        }
        optionSelected(null, OptionId.INIT);
    }
    private void optionSelected_STOP_FOLLOW_ME(String optionText, Object optionData,PersonAPI player,boolean willEngage,boolean hostile, LordEvent feast,OptionId option){
        targetLord.setCurrAction(null);
        lordFleet.clearAssignments();
        displayAcceptSuggestAction();
        optionSelected(null, OptionId.INIT);
    }
    private void optionSelected_SUGGEST_PATROL(String optionText, Object optionData,PersonAPI player,boolean willEngage,boolean hostile, LordEvent feast,OptionId option){
        textPanel.addParagraph(StringUtil.getString(CATEGORY, "ask_patrol_location"));
        options.clearOptions();
        for (MarketAPI market : Global.getSector().getEconomy().getMarketsCopy()) {
            if (market.getFaction().equals(targetLord.getFaction()) || market.getFaction().isPlayerFaction()) {
                options.addOption(market.getName(), market.getId());
            }
        }
        options.addOption(StringUtil.getString(CATEGORY, "option_nevermind"), OptionId.INIT);
        nextState = OptionId.SUGGEST_PATROL_LOC;
    }
    private void optionSelected_SUGGEST_PATROL_LOC(String optionText, Object optionData,PersonAPI player,boolean willEngage,boolean hostile, LordEvent feast,OptionId option){
        if (optionData instanceof String) {
            SectorEntityToken patrolTarget = Global.getSector().getEconomy().getMarket((String) optionData).getPrimaryEntity();
            LordAI.playerOrder(targetLord, LordAction.PATROL_TRANSIT, patrolTarget);
            displayAcceptSuggestAction();
            if (!targetLord.getFaction().equals(Global.getSector().getPlayerFaction()) && !targetLord.isMarried()) {
                targetLord.getLordAPI().getRelToPlayer().adjustRelationship(-0.02f, null);
                textPanel.addParagraph(StringUtil.getString(
                        CATEGORY, "relation_decrease", targetLord.getLordAPI().getNameString(), "2"), Color.RED);
            }
        } else {
            // This should never happen
            textPanel.addParagraph(StringUtil.getString(CATEGORY, "refuse_suggest_action"));
        }
        optionSelected(null, OptionId.INIT);
    }
    private void optionSelected_SUGGEST_RAID(String optionText, Object optionData,PersonAPI player,boolean willEngage,boolean hostile, LordEvent feast,OptionId option){
        textPanel.addParagraph(StringUtil.getString(CATEGORY, "ask_raid_location"));
        options.clearOptions();
        for (MarketAPI market : Global.getSector().getEconomy().getMarketsCopy()) {
            if (Utils.canBeAttacked(market.getFaction())
                    && market.getFaction().isHostileTo(targetLord.getFaction())) {
                options.addOption(market.getName(), market.getId());
            }
        }
        options.addOption(StringUtil.getString(CATEGORY, "option_nevermind"), OptionId.INIT);
        nextState = OptionId.SUGGEST_RAID_LOC;
    }
    private void optionSelected_SUGGEST_RAID_LOC(String optionText, Object optionData,PersonAPI player,boolean willEngage,boolean hostile, LordEvent feast,OptionId option){
        if (optionData instanceof String) {
            SectorEntityToken raidTarget = Global.getSector().getEconomy().getMarket((String) optionData).getPrimaryEntity();
            LordAI.playerOrder(targetLord, LordAction.RAID_TRANSIT, raidTarget);
            displayAcceptSuggestAction();
            if (!targetLord.getFaction().equals(Global.getSector().getPlayerFaction()) && !targetLord.isMarried()) {
                targetLord.getLordAPI().getRelToPlayer().adjustRelationship(-0.02f, null);
                textPanel.addParagraph(StringUtil.getString(
                        CATEGORY, "relation_decrease", targetLord.getLordAPI().getNameString(), "2"), Color.RED);
            }
        } else {
            // This should never happen
            textPanel.addParagraph(StringUtil.getString(CATEGORY, "refuse_suggest_action"));
        }
        optionSelected(null, OptionId.INIT);
    }
    private void optionSelected_SUGGEST_UPGRADE(String optionText, Object optionData,PersonAPI player,boolean willEngage,boolean hostile, LordEvent feast,OptionId option){
        LordAI.playerOrder(targetLord, LordAction.UPGRADE_FLEET_TRANSIT, null);
        displayAcceptSuggestAction();
        if (!targetLord.getFaction().equals(Global.getSector().getPlayerFaction())) {
            targetLord.getLordAPI().getRelToPlayer().adjustRelationship(-0.01f, null);
            textPanel.addParagraph(StringUtil.getString(
                    CATEGORY, "relation_decrease", targetLord.getLordAPI().getNameString(), "1"), Color.RED);
        }
        optionSelected(null, OptionId.INIT);
    }
    private void optionSelected_SUGGEST_CEASEFIRE(String optionText, Object optionData,PersonAPI player,boolean willEngage,boolean hostile, LordEvent feast,OptionId option){
        options.removeOption(OptionId.SUGGEST_CEASEFIRE);
        if (targetLord.getLordAPI().getRelToPlayer().isAtWorst(RepLevel.WELCOMING)) {
            textPanel.addParagraph(StringUtil.getString(CATEGORY, "accept_mercy"));
            textPanel.addParagraph(StringUtil.getString(
                    CATEGORY, "relation_decrease", targetLord.getLordAPI().getNameString(), "10"), Color.RED);
            targetLord.getLordAPI().getRelToPlayer().adjustRelationship(-0.1f, null);
            lordFleet.getAI().doNotAttack(Global.getSector().getPlayerFleet(), 7);
            Misc.setFlagWithReason(lordFleet.getMemoryWithoutUpdate(),
                    MemFlags.MEMORY_KEY_MAKE_NON_HOSTILE, "starlords", true, 7);
        } else {
            textPanel.addParagraph(StringUtil.getString(CATEGORY, "refuse_mercy"));
        }
    }
    private void optionSelected_LEAVE(String optionText, Object optionData,PersonAPI player,boolean willEngage,boolean hostile, LordEvent feast,OptionId option){
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
