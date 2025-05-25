package starlords.util.factionUtils;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.impl.campaign.ids.People;
import com.fs.starfarer.api.util.Misc;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import starlords.ai.utils.TargetUtils;
import starlords.lunaSettings.StoredSettings;
import starlords.plugins.LordInteractionDialogPluginImpl;
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
    * 3)(maybe done?) make it so only valid markets can be fiefs.
    * 4)(done) make it so only valid markets can be traded with.
    * 5)(done) make it so only factions that are warable can have war declared.
    * 6) (done)make it so only factions that are diplomatic can have peace / war actions.
    * 7) (done, needs testing)make it so only factions that can have policys can have policys
    * 8) (done, but only basic data is complete) implement the faction.json
    *
    * note:
    *   things I need to test (both with pirates and not):
    *   1) can I press the policy buttons?
    *       -heg: yes.
    *       -player: yes.
    *       -player before starlords: no
    *   2) who can I declare war / peace with?
    *       -(in heg) player can declare war / peace with the right people.
    *       -(in player) player can declare war / peace with the right people.
    *       -starlords seem to work in this? (he just tatical bombarded though. did I mess up somewere? need to test without this modification quickly)
    *   3) can I have a fief?
    *       -(in own faction) yes? buttons were fighting me, but thats just the 'once a month' update thing.
    *   4) can a marshal exist?
    *       -the heg can have one
    *       -player can have one.
    *   5) can a campain be lead?
    *       -heg lanched a campain. only defensive so far. possable issues with targeting prevent counter offencive?
    *       -(player) I need to check this.
    *           -UNKNWON if this fucking aafrjnaskmcfasdas dm HURRY UP DAM IT. (it should work, by my calculations.)
    *   6) what does this faction target (for trade, and for raids, and for campains)?
    *       -raids (target with player command) works?
    *           -note: the 'raid' consided of the lord going aroud the system attacking random fleets. never attempted to attack the planet after the staion was destroyed. unknown reason.
    *           -note: this is likely a issue in the base starlords. I barely understand the AI.
    *       -campain:
    *           -the heg compain happend. only defensivly so far. unknown reason. possable issues with can attack...
    *   7) AFTER I AM DONE, TURN OFF DEBUG MODE! ARG....
    *
    * issues:
    *
    *   other alturations:
    *       having a polics / deplomancy true, and a policys / deplomancy false
    *       installing nexerlin
    *       turning on debug mode to see what policys are formed.
    *
    *   other improvements:
    *       make a system that 'gives' pirate type factions a fief? so pirates can have there own fief.
    *
    * */
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
    public static Logger log = Global.getLogger(TargetUtils.class);

    protected String factionID;

    //protected PersonAPI leader;
    protected String leaderID;
    protected String T0_title;
    protected String T1_title;
    protected String T2_title;

    protected boolean canAttack;
    protected boolean canBeAttacked;
    protected boolean canInvade;
    protected boolean canBeRaided;
    protected boolean canRaid;
    protected boolean canBeSatBomb;
    protected boolean canSatBomb;
    protected boolean canTacticalBomb;
    protected boolean canBeTacticalBomb;
    protected boolean canHaveCampaigns;

    protected boolean canStarlordsJoin;

    protected boolean canPreformDiplomacy;
    protected boolean canPreformPolicy;
    protected boolean canPreformFeasts;

    protected boolean canTrade;
    protected boolean canBeTradedWith;

    protected boolean canGiveFiefs;
    protected boolean canLordsTakeFiefsWithDefection;
    protected int maxNumberFiefsPerLord;

    protected double lordFiefIncomeMulti;
    protected double lordCombatIncomeMulti;
    protected double lordTradeIncomeMulti;
    protected double lordCommissionedIncomeMulti;

    protected double lordRepChangeFromKillsMulti;
    protected double lordFleetUpkeepCostMulti;
    //private double starlordsOnStartMulti = 1;
    //private double starlordsLifeMulti = 1;
    public FactionTemplate(String factionID){
        this.factionID = factionID;
        FactionTemplateController.addTemplate(this);
        init(factionID,new JSONObject());
    }
    public FactionTemplate(String factionID, JSONObject json){
        this.factionID = factionID;
        FactionTemplateController.addTemplate(this);
        init(factionID,json);
    }
    private void init(String factionID,JSONObject json){
        log.info("setting up faction template for: "+factionID);

        setLeaderID("leaderID",json);
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

        setCanTrade("canTrade",json);
        setCanBeTradedWith("canBeTradedWith",json);

        setCanGiveFiefs("canGivesFiefs",json);
        setCanLordsTakeFiefsWithDefection("canLordsTakeFiefsWithDefection",json);
        setMaxNumberFiefsPerLord("maxNumberOfFiefs",json);

        setLordFiefIncomeMulti("fiefIncomeMulti",json);
        setLordCombatIncomeMulti("combatIncomeMulti",json);
        setLordTradeIncomeMulti("tradeIncomeMulti",json);
        setLordCommissionedIncomeMulti("commissionIncomeMulti",json);

        setLordFleetUpkeepCostMulti("lordFleetUpkeepCostMulti",json);

        setLordRepChangeFromKillsMulti("lordRepGainPerWin",json);

        insureAttackStatusCorrect();

        log.info("  leaderID "+this.leaderID);
        log.info("  rank0 "+this.T0_title);
        log.info("  rank1 "+this.T1_title);
        log.info("  rank2 "+this.T2_title);
        log.info("");
        log.info("  canBeAttacked: "+this.canBeAttacked);
        log.info("  canAttack: "+this.canAttack);
        log.info("  canInvade: "+this.canInvade);
        log.info("  canSatBomb: "+this.canSatBomb);
        log.info("  canBeSatBomb "+this.canBeSatBomb);
        log.info("  canBeRaided "+this.canBeRaided);
        log.info("  canRaid "+this.canRaid);
        log.info("  canTacticalBombed "+this.canTacticalBomb);
        log.info("  canHaveCompaings: "+this.canHaveCampaigns);
        log.info("");
        log.info("  canStarlordsJoin :"+this.canStarlordsJoin);
        log.info("  canPreformDeplomancy: "+this.canPreformDiplomacy);
        log.info("  canHoldFeasts: "+this.canPreformFeasts);
        log.info("  canTrade: "+this.canTrade);
        log.info("  canBeTradedWith: "+this.canBeTradedWith);
        log.info("  canGiveFiefs: "+this.canGiveFiefs);
        log.info("  canTakeFiefsWithWhenDefecting: "+this.canLordsTakeFiefsWithDefection);
        log.info("  (not yet working) max number of fiefs per lord: "+this.maxNumberFiefsPerLord);
        log.info("");
        log.info("  fiefIncomeMulti: "+this.lordFiefIncomeMulti);
        log.info("  combatIncomeMulti: "+this.lordCombatIncomeMulti);
        log.info("  tradeIncomeMulti: "+this.lordTradeIncomeMulti);
        log.info("  commissionIncomeMulti: "+this.lordCommissionedIncomeMulti);
        log.info("  fleetUpkeepCostMulti: "+this.lordFleetUpkeepCostMulti);
        log.info("  lordRepGainPerWinMulti: "+this.lordRepChangeFromKillsMulti);

    }
    /*@SneakyThrows
    protected void setLeader(String key, JSONObject json){
        if (json != null && json.has(key)){
            String temp = json.getString(key);
            leader = Global.getSector().getImportantPeople().getPerson(temp);
            LordInteractionDialogPluginImpl.log.info("got leader as a person of: "+leader);
            return;
        }
        LordInteractionDialogPluginImpl.log.info("got leader as null");
        leader = null;
    }*/
    public PersonAPI getLeader(){
        if (leaderID == null) return null;
        PersonAPI person = Global.getSector().getImportantPeople().getPerson(leaderID);
        return person;
    }
    @SneakyThrows
    protected void setLeaderID(String key, JSONObject json){
        if (json != null && json.has(key)){
            leaderID = json.getString(key);
            //leader = Global.getSector().getImportantPeople().getPerson(leaderID);
            return;
        }
        leaderID = null;
        //leader = null;
    }
    @SneakyThrows
    protected void setT0_title(String key, JSONObject json){
        if (json != null && json.has(key)){
            T0_title = json.getString(key);
            return;
        }
        T0_title = StringUtil.getString("starlords_title", "title_default_" + 0);
    }
    @SneakyThrows
    protected void setT1_title(String key, JSONObject json){
        if (json != null && json.has(key)){
            T1_title = json.getString(key);
            return;
        }
        T1_title = StringUtil.getString("starlords_title", "title_default_" + 1);
    }
    @SneakyThrows
    protected void setT2_title(String key, JSONObject json){
        if (json != null && json.has(key)){
            T2_title = json.getString(key);
            return;
        }
        T2_title = StringUtil.getString("starlords_title", "title_default_" + 2);
    }
    @SneakyThrows
    protected void setCanAttack(String key,JSONObject json){
        if (json != null && json.has(key)){
            canAttack = json.getBoolean(key);
            return;
        }
        canAttack = true;

    }
    @SneakyThrows
    protected void setCanBeAttacked(String key,JSONObject json){
        if (json != null && json.has(key)){
            canBeAttacked = json.getBoolean(key);
            return;
        }
        if (Utils.nexEnabled()){
            canBeAttacked = NexerlinUtilitys.canInvade(Global.getSector().getFaction(factionID));
            return;
        }
        boolean isPirate = Misc.isPirateFaction(Global.getSector().getFaction(factionID));
        canBeAttacked = !isPirate;
    }
    @SneakyThrows
    protected void setCanInvade(String key,JSONObject json){
        if (json != null && json.has(key)){
            canInvade = json.getBoolean(key);
            return;
        }
        if (Utils.nexEnabled()){
            canInvade = NexerlinUtilitys.canInvade(Global.getSector().getFaction(factionID));
            return;
        }
        boolean isPirate = Misc.isPirateFaction(Global.getSector().getFaction(factionID));
        canInvade = !isPirate;

    }
    @SneakyThrows
    protected void setCanSatBomb(String key,JSONObject json){
        if (json != null && json.has(key)){
            canSatBomb = json.getBoolean(key);
            return;
        }
        canSatBomb = true;

    }
    @SneakyThrows
    protected void setCanBeSatBomb(String key,JSONObject json){
        if (json != null && json.has(key)){
            canBeSatBomb = json.getBoolean(key);
            return;
        }
        canBeSatBomb = true;
    }
    @SneakyThrows
    protected void setCanRaid(String key,JSONObject json){
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
    protected void setCanTacticalBomb(String key,JSONObject json){
        if (json != null && json.has(key)){
            canTacticalBomb = json.getBoolean(key);
            return;
        }
        canTacticalBomb = true;

    }
    @SneakyThrows
    protected void setCanBeTacticalBomb(String key,JSONObject json){
        if (json != null && json.has(key)){
            canBeTacticalBomb = json.getBoolean(key);
            return;
        }
        canBeTacticalBomb = true;
    }
    @SneakyThrows
    protected void setCanBeRaided(String key,JSONObject json){
        if (json != null && json.has(key)){
            canBeRaided = json.getBoolean(key);
            return;
        }
        /*if (Utils.nexEnabled()){
            canBeRaided = NexerlinUtilitys.canBeAttacked(Global.getSector().getFaction(factionID));
            return;
        }*/
        canBeRaided = true;

    }
    @SneakyThrows
    protected void setCanHaveCampaigns(String key,JSONObject json){
        if (json != null && json.has(key)){
            canHaveCampaigns = json.getBoolean(key);
            return;
        }
        if (Utils.nexEnabled()){
            canHaveCampaigns = NexerlinUtilitys.canInvade(Global.getSector().getFaction(factionID));
            return;
        }
        boolean isPirate = Misc.isPirateFaction(Global.getSector().getFaction(factionID));
        canHaveCampaigns = !isPirate;

    }

    @SneakyThrows
    protected void setCanStarlordsJoin(String key, JSONObject json){
        if (json != null && json.has(key)){
            canStarlordsJoin = json.getBoolean(key);
            return;
        }
        canStarlordsJoin = true;
    }
    @SneakyThrows
    protected void setCanPreformDiplomacy(String key, JSONObject json){
        if (json != null && json.has(key)){
            canPreformDiplomacy = json.getBoolean(key);
            return;
        }
        if (Utils.nexEnabled()){
            canPreformDiplomacy = NexerlinUtilitys.canPreformDiplomacy(Global.getSector().getFaction(factionID));
            return;
        }
        boolean isPirate = Misc.isPirateFaction(Global.getSector().getFaction(factionID));
        canPreformDiplomacy = !isPirate;

    }
    @SneakyThrows
    protected void setCanPreformPolicy(String key, JSONObject json){
        if (json != null && json.has(key)){
            canPreformPolicy = json.getBoolean(key);
            return;
        }
        if (Utils.nexEnabled()){
            canPreformPolicy = NexerlinUtilitys.canPreformDiplomacy(Global.getSector().getFaction(factionID));
            return;
        }
        boolean isPirate = Misc.isPirateFaction(Global.getSector().getFaction(factionID));
        canPreformPolicy = !isPirate;

    }
    @SneakyThrows
    protected void setCanPreformFeasts(String key, JSONObject json){
        if (json != null && json.has(key)){
            canPreformFeasts = json.getBoolean(key);
            return;
        }
        canPreformFeasts = true;
    }


    @SneakyThrows
    protected void setCanTrade(String key, JSONObject json){
        if (json != null && json.has(key)){
            canTrade = json.getBoolean(key);
            return;
        }
        canTrade = true;
    }
    @SneakyThrows
    protected void setCanBeTradedWith(String key, JSONObject json){
        if (json != null && json.has(key)){
            canBeTradedWith = json.getBoolean(key);
            return;
        }
        canBeTradedWith = true;
    }

    @SneakyThrows
    protected void setCanGiveFiefs(String key, JSONObject json){
        if (json != null && json.has(key)){
            canGiveFiefs = json.getBoolean(key);
            return;
        }
        canGiveFiefs = true;

    }
    @SneakyThrows
    protected void setCanLordsTakeFiefsWithDefection(String key, JSONObject json){
        if (json != null && json.has(key)){
            canLordsTakeFiefsWithDefection = json.getBoolean(key);
            return;
        }
        canLordsTakeFiefsWithDefection = canInvade;
    }
    @SneakyThrows
    protected void setMaxNumberFiefsPerLord(String key, JSONObject json){
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
    protected void setLordFiefIncomeMulti(String key, JSONObject json){
        if (json != null && json.has(key)){
            lordFiefIncomeMulti = json.getDouble(key);
            return;
        }
        boolean isPirate = Misc.isPirateFaction(Global.getSector().getFaction(factionID));
        if (isPirate){
            lordFiefIncomeMulti = 0.5;
            return;
        }
        lordFiefIncomeMulti = 1;

    }
    @SneakyThrows
    protected void setLordCombatIncomeMulti(String key, JSONObject json){
        if (json != null && json.has(key)){
            lordCombatIncomeMulti = json.getDouble(key);
            return;
        }
        boolean isPirate = Misc.isPirateFaction(Global.getSector().getFaction(factionID));
        if (isPirate){
            lordCombatIncomeMulti = 2;
            return;
        }
        lordCombatIncomeMulti = 1;

    }
    @SneakyThrows
    protected void setLordTradeIncomeMulti(String key, JSONObject json){
        if (json != null && json.has(key)){
            lordTradeIncomeMulti = json.getDouble(key);
            return;
        }
        boolean isPirate = Misc.isPirateFaction(Global.getSector().getFaction(factionID));
        if (isPirate){
            lordTradeIncomeMulti = 1;
            return;
        }
        lordTradeIncomeMulti = 1;

    }
    @SneakyThrows
    protected void setLordCommissionedIncomeMulti(String key, JSONObject json){
        if (json != null && json.has(key)){
            lordCommissionedIncomeMulti = json.getDouble(key);
            return;
        }
        boolean isPirate = Misc.isPirateFaction(Global.getSector().getFaction(factionID));
        if (isPirate){
            lordCommissionedIncomeMulti = 2;
            return;
        }
        lordCommissionedIncomeMulti = 1;
    }

    @SneakyThrows
    protected void setLordFleetUpkeepCostMulti(String key, JSONObject json){
        if (json != null && json.has(key)){
            lordFleetUpkeepCostMulti = json.getDouble(key);
            return;
        }
        lordFleetUpkeepCostMulti = 1;
    }

    @SneakyThrows
    protected void setLordRepChangeFromKillsMulti(String key, JSONObject json){
        if (json != null && json.has(key)){
            lordRepChangeFromKillsMulti = json.getDouble(key);
            return;
        }
        boolean isPirate = Misc.isPirateFaction(Global.getSector().getFaction(factionID));
        if (isPirate){
            lordRepChangeFromKillsMulti = 1;
            return;
        }
        lordRepChangeFromKillsMulti = 0;
    }

    public boolean isCanBeAttacked(){
        return canBeAttacked;
        //if (!canBeAttacked) return false;
        //if (!canBeTacticalBomb && !canBeSatBomb && !canBeRaided && !canInvade) return false;
        //return true;
    }
    public boolean isCanAttack(){
        return canAttack;
        //if (!canAttack) return false;
        //if (!canTacticalBomb && !canSatBomb && !canRaid && !canInvade) return false;
        //return true;

    }
    private void insureAttackStatusCorrect(){
        if (canAttack){
            canAttack = canTacticalBomb || canSatBomb || canRaid || canInvade;
        }
        if (canBeAttacked){
            canBeAttacked = canTacticalBomb || canSatBomb || canRaid || canInvade;
        }
    }
}
