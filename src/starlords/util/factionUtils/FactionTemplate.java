package starlords.util.factionUtils;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.util.Misc;
import lombok.Getter;
import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.util.NexerlinUtilitys;
import starlords.util.StringUtil;
import starlords.util.Utils;

@Getter
public class FactionTemplate {
    /*what have I done?
    * 1) I have made it so lords in factions that don't allow defection cant defect, cant defect to factions that dont allow it (hopefully).
    * 2) I have made it so some markets cannot be attacked (hopefully)
    *   -note: I still need to make it so nexerlin can override invasion permissions.
    * what do I need to do?:
    * 3) make it so only valid markets can be fiefs.
    * 4) make it so only valid markets can be traded with.
    * 5) implement the faction.json*/
    /*
    * issue: my settings for what types of attacks are available make no sense. like...
    * well, I guess they do, kinda?
    * NOTE: this is only 50% of what I need to do. I ALSO require a way to determine if a market is invasion-able according to nexerlin.
    *
    * ok, so here is what I am going to do. take it slow and steady
    *   support:
    *       go into lord and create functions:
    *           can raid
    *           can saterate bomba
    *   1) check valid markets for raiding.
    *       -additional functions:
    *           isValidMarket (to determine if this is a real market, in a real location. and it exists. arg.)
    *       find all instances of markets being targeted for attacks. and change them to use this system.
    *           -note: we need to do this for invasions as well.
    *       find all instances of factions deciding if they can attack (raid), and make sure they can.
    *           -note: we need to do this for invasions as well.
    *   locations related to this:
    *       EventController.getPreferredRaidLocation();
    *       EventController.getCampaignTarget();
    *       -> if (!currCampaign.isDefensive()) {
    *       LordAI.chooseNewOffensiveType
    *       -> if (Utils.nexEnabled() && maxViolence >= LordEvent.OffensiveType.NEX_GROUND_BATTLE.violence)
    *       -> note: there are a few things here related to raids. do to the complexity, maybe change 'can be raided' to 'can be attacked'?
    *          additional data: that might not be required. looking at it, only campaigns can preform invasions. so its fine to not change 'can be raided'.
    *       LordAI.progressAssignment.RAID -> chooseNewOffensiveType(lord, campaign);
    *       LordAI.progressAssignment.CAMPAIGN -> chooseNewOffensiveType(lord, raid);
    *       -> note: I should go into chooseNewOffensiveType(lord, raid) and change this sugnificently.
    *           1: use the inputed raid type to determin what type of attack I am allowed to preform.
    *           2: change what attacks can be preformed from the 'violence level'. to 'attack type' this is basically the same, but like, I would also get the option settings, so we can allow lords to preform invasions on there own, provided certain actions exist.
    *           3: for invasions, I really need to fix nexerlins lack of ability to tell if I can invasions or not.
    *           4: (maybe) add a setting to starlords faction file to allow single lords to preform invasions?
    *       -> note: I need more settings:
    *           1: allow lords to attempt invasions
    *           2: allow lords to attempt sat-bombs
    *           3: allow lord to preform raids
    *           4: allow lords to preform tatical bombardments
    *           5: allow campains to preform invasions
    *           6: allows campains to preform sat-bombs
    *           7: allow campains to preform raids
    *           8: allow campains to preform tatical bombardments.
    *
    *           9: set campaign base max violence
    *           10: set lord base max violence
    *               -note: every combat action costs a 'violence' value. the max violence is the max amount of combat a attack can have before a given lord gives up.
    *               -note: I need to make it so every world can only be attacked so many times, or they will deciv. so that will need to be another setting.
    *           11: max vilance per world from lord.
    *               -note: I need a way for a lord to chose another attack target if they preform an invasion over a given world.
    *           12: max vilance per world from campain.
    *       -> note: I need more faction json things:
    *           1: campaign max violence multi
    *           2: lord attack max violence multi.
    *           3: max vilance per world from lord.
    *           4: max vilance per world from campain.
    *
    *
    *   location related to other things that I located.
    *       DefectionUtils.performDefection
    *       -> if (!Utils.canBeAttacked(faction) && includeFiefs) {
    *
    *
    *
    * required data:
    * factionID
    * leader name. (note: this requirs additional knowlage. what the hell are faction leaders?)
    * rank_0
    * rank_1
    * rank_2
    *
    * can invaide
    * can be raided
    * can raid
    * can starlords join
    *
    * can preform diplomacy
    * can preform policy's
    * can have tournaments
    *
    * (later)
    * starlords on start multiplyer
    * starlords life multiplyer
    *
    *
    *
    *
    * (later)
    * starlords multi for lord 'costs'.
    *   - ship buying cost.
    *   - garrison buying cost.
    *   - Smod buying cost.
    *
    * starlord gain multi:
    *   - trade wealth gain
    *   - kill wealth gain.
    *   - fief wealth gain
    *   - XP gain
    *
    * */
    private String factionID;

    private String T0_title;
    private String T1_title;
    private String T2_title;

    private boolean canAttack;
    private boolean canBeAttacked;
    private boolean canInvade;
    private boolean canBeRaided;
    private boolean canRaid;
    private boolean canBeSatBomb;
    private boolean canSatBomb;
    private boolean canTacticalBomb;
    private boolean canBeTacticalBomb;
    private boolean canHaveCampaigns;

    private boolean canStarlordsJoin;

    private boolean canPreformDiplomacy;
    private boolean canPreformPolicy;
    private boolean canPreformFeasts;

    private boolean canGiveFiefs;
    private boolean canLordsTakeFiefsWithDefection;
    private int maxNumberFiefsPerLord;

    private double lordFiefIncomeMulti;
    private double lordCombatIncomeMulti;
    //private double starlordsOnStartMulti = 1;
    //private double starlordsLifeMulti = 1;
    public FactionTemplate(String factionID){
        this.factionID = factionID;
        FactionTemplateController.addTemplate(this);
        init(factionID,new JSONObject());
    }
    public FactionTemplate(String factionID, JSONObject json){
        this(factionID);
        init(factionID,json);
    }
    private void init(String factionID,JSONObject json){
        setT0_title("rank_0",json);
        setT1_title("rank_1",json);
        setT2_title("rank_2",json);

        setCanAttack("canBeAttacked",json);
        setCanBeAttacked("canAttack",json);
        setCanInvade("canInvade",json);
        setCanSatBomb("canSatBomb",json);
        setCanBeSatBomb("canBeSatBomb",json);
        setCanBeRaided("canBeRaided",json);
        setCanRaid("canRaid",json);
        setCanTacticalBomb("canTacticalBombed",json);
        setCanBeTacticalBomb("canBeTacticalBomb",json);
        setCanHaveCampaigns("canHaveCampaigns",json);

        setCanStarlordsJoin("canStarlordsJoin",json);

        setCanPreformDiplomacy("canPreformDiplomacy",json);
        setCanPreformPolicy("canPreformPolicy",json);
        setCanPreformFeasts("canHoldFeasts",json);

        setCanGiveFiefs("canGivesFiefs",json);
        setCanLordsTakeFiefsWithDefection("canLordsTakeFiefsWithDefection",json);
        setMaxNumberFiefsPerLord("maxNumberOfFiefs",json);

        setLordFiefIncomeMulti("fiefIncomeMulti",json);
        setLordCombatIncomeMulti("combatIncomeMulti",json);

    }
    @SneakyThrows
    private void setT0_title(String key, JSONObject json){
        if (json != null && json.has(key)){
            T0_title = json.getString(key);
            return;
        }
        T0_title = StringUtil.getString("starlords_title", "title_default_" + 0);
    }
    @SneakyThrows
    private void setT1_title(String key, JSONObject json){
        if (json != null && json.has(key)){
            T1_title = json.getString(key);
            return;
        }
        T1_title = StringUtil.getString("starlords_title", "title_default_" + 1);
    }
    @SneakyThrows
    private void setT2_title(String key, JSONObject json){
        if (json != null && json.has(key)){
            T2_title = json.getString(key);
            return;
        }
        T2_title = StringUtil.getString("starlords_title", "title_default_" + 2);
    }
    @SneakyThrows
    private void setCanAttack(String key,JSONObject json){
        if (json != null && json.has(key)){
            canAttack = json.getBoolean(key);
            return;
        }
        canAttack = true;

    }
    @SneakyThrows
    private void setCanBeAttacked(String key,JSONObject json){
        if (json != null && json.has(key)){
            canBeAttacked = json.getBoolean(key);
            return;
        }
        if (Utils.nexEnabled()){
            canBeAttacked = NexerlinUtilitys.canBeAttacked(Global.getSector().getFaction(factionID));
            return;
        }
        boolean isPirate = Misc.isPirateFaction(Global.getSector().getFaction(factionID));
        canBeAttacked = !isPirate;
    }
    @SneakyThrows
    private void setCanInvade(String key,JSONObject json){
        if (json != null && json.has(key)){
            canInvade = json.getBoolean(key);
            return;
        }
        if (Utils.nexEnabled()){
            canInvade = NexerlinUtilitys.canBeAttacked(Global.getSector().getFaction(factionID));
            return;
        }
        boolean isPirate = Misc.isPirateFaction(Global.getSector().getFaction(factionID));
        canInvade = !isPirate;

    }
    @SneakyThrows
    private void setCanSatBomb(String key,JSONObject json){
        if (json != null && json.has(key)){
            canSatBomb = json.getBoolean(key);
            return;
        }
        canBeRaided = canInvade;

    }
    @SneakyThrows
    private void setCanBeSatBomb(String key,JSONObject json){
        if (json != null && json.has(key)){
            canBeSatBomb = json.getBoolean(key);
            return;
        }
        canBeSatBomb = canInvade;
    }
    @SneakyThrows
    private void setCanRaid(String key,JSONObject json){
        if (json != null && json.has(key)){
            canRaid = json.getBoolean(key);
            return;
        }
        canRaid = true;
        /*if (Utils.nexEnabled()){
            canInvade = NexerlinUtilitys.canBeAttacked(Global.getSector().getFaction(factionID));
            return;
        }
        boolean isPirate = Misc.isPirateFaction(Global.getSector().getFaction(factionID));
        canInvade = !isPirate;*/

    }
    @SneakyThrows
    private void setCanTacticalBomb(String key,JSONObject json){
        if (json != null && json.has(key)){
            canTacticalBomb = json.getBoolean(key);
            return;
        }
        canTacticalBomb = canBeRaided;

    }
    @SneakyThrows
    private void setCanBeTacticalBomb(String key,JSONObject json){
        if (json != null && json.has(key)){
            canBeTacticalBomb = json.getBoolean(key);
            return;
        }
        canBeTacticalBomb = canInvade;
    }
    @SneakyThrows
    private void setCanBeRaided(String key,JSONObject json){
        if (json != null && json.has(key)){
            canBeRaided = json.getBoolean(key);
            return;
        }
        if (Utils.nexEnabled()){
            canBeRaided = NexerlinUtilitys.canBeAttacked(Global.getSector().getFaction(factionID));
            return;
        }
        boolean isPirate = Misc.isPirateFaction(Global.getSector().getFaction(factionID));
        canBeRaided = !isPirate;

    }
    @SneakyThrows
    private void setCanHaveCampaigns(String key,JSONObject json){
        if (json != null && json.has(key)){
            canBeRaided = json.getBoolean(key);
            return;
        }
        if (Utils.nexEnabled()){
            canHaveCampaigns = NexerlinUtilitys.canBeAttacked(Global.getSector().getFaction(factionID));
            return;
        }
        boolean isPirate = Misc.isPirateFaction(Global.getSector().getFaction(factionID));
        canHaveCampaigns = !isPirate;

    }

    @SneakyThrows
    private void setCanStarlordsJoin(String key, JSONObject json){
        if (json != null && json.has(key)){
            canStarlordsJoin = json.getBoolean(key);
            return;
        }
        canStarlordsJoin = true;
    }
    @SneakyThrows
    private void setCanPreformDiplomacy(String key, JSONObject json){
        if (json != null && json.has(key)){
            canPreformDiplomacy = json.getBoolean(key);
            return;
        }
        if (Utils.nexEnabled()){
            canPreformDiplomacy = NexerlinUtilitys.canChangeRelations(Global.getSector().getFaction(factionID));
            return;
        }
        boolean isPirate = Misc.isPirateFaction(Global.getSector().getFaction(factionID));
        canPreformDiplomacy = !isPirate;

    }
    @SneakyThrows
    private void setCanPreformPolicy(String key, JSONObject json){
        if (json != null && json.has(key)){
            canPreformPolicy = json.getBoolean(key);
            return;
        }
        boolean isPirate = Misc.isPirateFaction(Global.getSector().getFaction(factionID));
        canPreformPolicy = !isPirate;

    }
    @SneakyThrows
    private void setCanPreformFeasts(String key, JSONObject json){
        if (json != null && json.has(key)){
            canPreformFeasts = json.getBoolean(key);
            return;
        }
        canPreformFeasts = true;
    }

    @SneakyThrows
    private void setCanGiveFiefs(String key, JSONObject json){
        if (json != null && json.has(key)){
            canGiveFiefs = json.getBoolean(key);
            return;
        }
        canGiveFiefs = true;

    }
    @SneakyThrows
    private void setCanLordsTakeFiefsWithDefection(String key, JSONObject json){
        if (json != null && json.has(key)){
            canLordsTakeFiefsWithDefection = json.getBoolean(key);
            return;
        }
        canLordsTakeFiefsWithDefection = canInvade;
    }
    @SneakyThrows
    private void setMaxNumberFiefsPerLord(String key, JSONObject json){
        if (json != null && json.has(key)){
            maxNumberFiefsPerLord = json.getInt(key);
            return;
        }
        boolean isPirate = Misc.isPirateFaction(Global.getSector().getFaction(factionID));
        if (isPirate){
            maxNumberFiefsPerLord = 1;
            return;
        }
        maxNumberFiefsPerLord = Integer.MAX_VALUE;
    }

    @SneakyThrows
    private void setLordFiefIncomeMulti(String key, JSONObject json){
        if (json != null && json.has(key)){
            lordFiefIncomeMulti = json.getDouble(key);
            return;
        }
        boolean isPirate = Misc.isPirateFaction(Global.getSector().getFaction(factionID));
        if (isPirate){
            lordFiefIncomeMulti = 1;
            return;
        }
        lordFiefIncomeMulti = 1;

    }
    @SneakyThrows
    private void setLordCombatIncomeMulti(String key, JSONObject json){
        if (json != null && json.has(key)){
            lordCombatIncomeMulti = json.getDouble(key);
            return;
        }
        boolean isPirate = Misc.isPirateFaction(Global.getSector().getFaction(factionID));
        if (isPirate){
            lordCombatIncomeMulti = 1;
            return;
        }
        lordCombatIncomeMulti = 1;

    }


    public boolean isCanBeAttacked(){
        if (!canBeAttacked) return false;
        if (!canBeTacticalBomb && !canBeSatBomb && !canBeRaided && !canInvade) return false;
        return true;
    }
    public boolean isCanAttack(){
        if (!canAttack) return false;
        if (!canTacticalBomb && !canSatBomb && !canRaid && !canInvade) return false;
        return true;

    }
}
