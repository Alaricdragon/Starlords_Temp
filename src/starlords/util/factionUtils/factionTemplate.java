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
public class factionTemplate {
    /*
    * NOTE: this is only 50% of what I need to do. I ALSO require a way to determine if a market is invasion-able according to nexerlin.
    *
    * ok, so here is what I am going to do. take it slow and steady
    *   1) check valid markets for raiding.
    *       -additional functions:
    *           isValidMarket (to determine if this is a real market, in a real location. and it exists. arg.)
    *       find all instances of markets being targeted for attacks. and change them to use this system.
    *           -note: we need to do this for invasions as well.
    *       find all instances of factions deciding if they can attack (raid), and make sure they can.
    *           -note: we need to do this for invasions as well.
    *
    *
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

    private boolean canInvade;
    private boolean canBeRaided;
    private boolean canRaid;
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
    public factionTemplate(String factionID){
        this.factionID = factionID;
        factionTemplateControler.addTemplate(this);
    }
    public factionTemplate(String factionID,JSONObject json){
        this(factionID);
        setT0_title("rank_0",json);
        setT1_title("rank_1",json);
        setT2_title("rank_2",json);

        setCanInvade("canInvade",json);
        setCanBeRaided("canBeRaided",json);
        setCanRaid("canRaid",json);
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
}
