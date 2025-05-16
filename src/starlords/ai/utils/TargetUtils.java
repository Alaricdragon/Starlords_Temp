package starlords.ai.utils;

import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.RepLevel;
import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.ids.Commodities;
import com.fs.starfarer.api.impl.campaign.ids.Industries;
import com.fs.starfarer.api.impl.campaign.rulecmd.salvage.MarketCMD;
import com.fs.starfarer.api.util.Misc;
import exerelin.campaign.intel.groundbattle.GBUtils;
import exerelin.campaign.intel.groundbattle.GroundBattleIntel;
import exerelin.campaign.intel.groundbattle.GroundUnitDef;
import lombok.Setter;
import starlords.controllers.EventController;
import starlords.controllers.LordController;
import starlords.person.Lord;
import starlords.person.LordEvent;
import starlords.util.Utils;
import starlords.util.factionUtils.FactionTemplate;
import starlords.util.factionUtils.FactionTemplateController;

import java.util.ArrayList;
import java.util.HashMap;

import static starlords.ai.LordAI.*;

@Setter
public class TargetUtils {
    /*NOTE TO SELF:
    * YES THIS IS NOT EXPANDABLE. YES IT FEELS UNFINISHED. YA IT KINDA SUCKS.
    * THE ONLY REALY WAY TO IMPROVE THIS WITHOUT CREATING MORE ISSUES IS TO REPLACE THE INTIER LORD AI.
    * FOCUS ON FIXES FIST YOU SILLY PERSON. FIX THE THINGS, YOU CAN UPGRADE THE AI TO BE EXPANDABLE LATER.*/
    /*
    * ok, so something I might be forced to do, is at least move the viliance cost of each action into a single area. this would help a lot with settings.
    * ARGASGASGAGA
    * OK, SO HERES THE GOD DAM DEAL
    * IF I SIMPLY CREATE A CLASS TO HANDLE THE DIFFERENT OFFENCE TYPES, I COULD HAVE IT HOLD THE FOLLOWING DATA:
    *   -violence cost
    *   -what happens when it ends
    * NO. this will evolve into quite a big thing. I need to wait before upgrading the event controller. I have to.
    * so, heres whats going to happen:
    * 1) I am going to put the violence cost of each action into a single class, as static values (so I can change them with settings)
    * 2)
    *
    *
    *  */
    private static boolean canRaid_lord;
    private static boolean canTacticalBomb_lord;
    private static boolean canInvade_lord;
    private static boolean canSatbomb_lord;
    private static boolean canRaid_campaign;
    private static boolean canTacticalBomb_campaign;
    private static boolean canInvade_campaign;
    private static boolean canSatbomb_campaign;
    public static boolean isValidMarket(MarketAPI market){
        if (!market.isInEconomy()) return false;
        if (market.getSize() <= 2) return false;
        if (market.getIndustries().size() == 0) return false;
        return true;
    }
    public static boolean canBeFief(Lord lord,MarketAPI market){
        if (!isValidMarket(market)) return false;
        return true;
    }
    public static boolean canBeTradedWith(Lord lord,MarketAPI market){
        if (!isValidMarket(market)) return false;
        if (market.isHidden() && market.getFaction().isAtBest(lord.getFaction(),RepLevel.WELCOMING)) return false;
        if (market.getFaction().isAtBest(lord.getFaction(),RepLevel.SUSPICIOUS)) return false;
        if (market.getFaction().equals(lord.getFaction())) return false;
        return true;
    }
    public static boolean canBeAttackedByLord(Lord lord, MarketAPI market){
        if (!isValidMarket(market)) return false;
        boolean[] attacks = possibleAttacksMarket(lord,market,ATTACK_TYPE_RAID);
        if (attacks == null) return false;
        for (boolean a : attacks){
            if (a) return true;
        }
        return false;
    }
    public static boolean canBeAttackedByCampaign(Lord lord, MarketAPI market){
        if (!isAttackable(lord,market)) return false;
        boolean[] attacks = possibleAttacksFaction(lord,market.getFaction(),ATTACK_TYPE_RAID);
        if (attacks == null) return false;
        for (boolean a : attacks){
            if (a) return true;
        }
        return false;
    }
    public static boolean isAttackable(Lord lord, MarketAPI market){
        //note: this is needed at the following functions: EventController.getPreferredRaidLocation, EventController.getCampaignTarget
        if (!isValidMarket(market)) return false;
        if (!market.getFaction().isHostileTo(lord.getLordAPI().getFaction())) return false;
        if (!Utils.canBeAttacked(market.getFaction())) return false;
        if (Misc.getDaysSinceLastRaided(market) < RAID_COOLDOWN) return false;//todo: adjust this arg I need a break... this inter thing is hard...
        if (!(LordController.getFactionsWithLords().contains(market.getFaction()) || market.getFaction().isPlayerFaction())) return false;
        return true;
    }

    public static final String ATTACK_TYPE_RAID = "ATTACK_TYPE_RAID", ATTACK_TYPE_CAMPAIGN = "ATTACK_TYPE_CAMPAIGN";

    private static boolean[] possibleAttacksMarket(Lord lord, MarketAPI market, String type){
        LordEvent event = EventController.getCurrentRaid(lord);
        if (event != null) return possibleAttacksMarket(lord, market,event, type);

        event = EventController.getCurrentCampaign(lord.getFaction());
        if (event == null) return possibleAttacksMarket(lord, market, null,type);
        if (event.getOriginator().equals(lord)) return possibleAttacksMarket(lord, market, event,type);
        //if (event.getParticipants().contains(lord)) return possibleAttacksMarket(lord, market, event,type);

        return possibleAttacksMarket(lord, market,null, type);
    }
    private static boolean[] possibleAttacksMarket(Lord lord, MarketAPI market,LordEvent event, String type){
        boolean[] out = possibleAttacksFaction(lord,market.getFaction(),type);
        if (out == null) return null;
        //vilance left is not required. that just seems to be used, (in the contect of chosing attacks) to prevent lords from invading or raiding at random.
        if (out[0]){
            //canRaid. (basic). this is always true for now.
        }
        if (out[1]){
            //canRaid. (industry)
            //make it so this can only raid industry's not yet raided this attack.
            if (!canRaidIndustry(market) ) out[1] = false;
            out[1] = canRaidIndustry(market);
        }
        if (out[2]){
            //canTacticalBomb
            //limit the number of Tactical bombs allowed to be dropped. whats the base game max?
            int fuelCost = MarketCMD.getBombardmentCost(market, lord.getFleet());
            int fuelAmt=0;
            if (event != null){
                fuelAmt = (int) event.getTotalFuel();
            }else{
                fuelAmt = (int) lord.getFleet().getCargo().getFuel();
            }
            if (fuelAmt < fuelCost) {
                out[2] = false;
            }
        }
        if (out[3]){
            //canInvade
            GroundBattleIntel tmp = new GroundBattleIntel(market, lord.getFaction(), market.getFaction());
            tmp.init();
            float defenderStr = GBUtils.estimateTotalDefenderStrength(tmp, true);
            tmp.endImmediately();
            float attackerStr = 0;
            if (event != null) {
                float marines = event.getTotalMarines();
                float heavies = event.getTotalArms();
                marines = Math.max(0, marines - heavies * GroundUnitDef.getUnitDef(GroundUnitDef.HEAVY).personnel.mult);
                attackerStr = marines * GroundUnitDef.getUnitDef(GroundUnitDef.MARINE).strength
                        + heavies * GroundUnitDef.getUnitDef(GroundUnitDef.HEAVY).strength;
            }else{
                float marines = lord.getFleet().getCargo().getMarines();
                float heavies = lord.getFleet().getCargo().getCommodityQuantity(Commodities.HAND_WEAPONS);
                marines = Math.max(0, marines - heavies * GroundUnitDef.getUnitDef(GroundUnitDef.HEAVY).personnel.mult);
                attackerStr = marines * GroundUnitDef.getUnitDef(GroundUnitDef.MARINE).strength
                        + heavies * GroundUnitDef.getUnitDef(GroundUnitDef.HEAVY).strength;
            }
            if (attackerStr <= 0.8 * defenderStr) {
                out[3] = false;
            }

        }
        if (out[4]){
            //canSatBomb.
            //limit the amount of sat bombs allowed.
            int fuelCost = MarketCMD.getBombardmentCost(market, lord.getFleet());
            int fuelAmt=0;
            if (event != null){
                fuelAmt = (int) event.getTotalFuel();
            }else{
                fuelAmt = (int) lord.getFleet().getCargo().getFuel();
            }
            if (fuelAmt < fuelCost) {
                out[4] = false;
            }
        }
        return out;
    }
    public static boolean canRaidIndustry(MarketAPI market) {
        for (Industry industry : market.getIndustries()) {
            if (!industry.canBeDisrupted()) continue;
            if (industry.getSpec().hasTag(Industries.TAG_UNRAIDABLE)) continue;
            return true;
        }
        return false;
    }

    public static int getViolenceLeft(LordEvent event){
        int maxViolenceAgainstTarget=0;
        int currentViolenceAgainstTarget=0;
        if (event != null) {
            currentViolenceAgainstTarget = event.getTotalViolence();
            maxViolenceAgainstTarget = event.getMaxViolence();
        }
        return Math.max(0,maxViolenceAgainstTarget - currentViolenceAgainstTarget);
    }
    public static int getCampaignViolenceLeft(LordEvent event){
        int maxViolenceAgainstTarget=0;
        int currentViolenceAgainstTarget=0;
        if (event != null) {
            currentViolenceAgainstTarget = event.getTotalCampaignViolence();
            maxViolenceAgainstTarget = event.getMaxCampaignViolence();
        }
        return Math.max(0,maxViolenceAgainstTarget - currentViolenceAgainstTarget);
    }

    private static boolean[] possibleAttacksFaction(Lord lord, FactionAPI factionAPI, String type){
        int typesOfAttacks = 5;
        boolean[] settings = new boolean[typesOfAttacks];
        switch (type){
            case ATTACK_TYPE_RAID:
                settings = new boolean[]{
                        canRaid_lord,//there are two types of raids.
                        canRaid_lord,
                        canTacticalBomb_lord,
                        canInvade_lord && Utils.nexEnabled(),
                        canSatbomb_lord
                };
                break;
            case ATTACK_TYPE_CAMPAIGN:
                settings = new boolean[]{
                        canRaid_campaign,
                        canRaid_campaign,
                        canTacticalBomb_campaign,
                        canInvade_campaign && Utils.nexEnabled(),
                        canSatbomb_campaign
                };
                break;
        }
        FactionTemplate a = FactionTemplateController.getTemplate(lord.getFaction());
        FactionTemplate b = FactionTemplateController.getTemplate(factionAPI);
        if (!a.isCanAttack()) return null;
        if (!b.isCanBeAttacked()) return null;
        boolean[] lordFactionAttacks = {
                a.isCanRaid(),
                a.isCanRaid(),
                a.isCanTacticalBomb(),
                a.isCanInvade(),
                a.isCanSatBomb()
        };
        boolean[] targetFactionAttacks = {
                b.isCanBeRaided(),
                b.isCanBeRaided(),
                b.isCanBeTacticalBomb(),
                b.isCanInvade(),
                b.isCanBeSatBomb()
        };
        boolean[] lordAttacks = {
                lord.canRaid(),
                lord.canRaid(),
                lord.canTacticallyBomb(),
                lord.canPreformInvasion(),
                lord.canSatBomb()
        };
        boolean[] output = new boolean[typesOfAttacks];
        for (int c = 0; c < 4; c++){
            output[c] = lordFactionAttacks[c] && targetFactionAttacks[c] && lordAttacks[c] && settings[c];
        }
        return output;
    }
    public static LordEvent.OffensiveType getOffencive(Lord lord,MarketAPI market,LordEvent event,String type){
        boolean[] out = possibleAttacksMarket(lord,market,event,type);
        ArrayList<LordEvent.OffensiveType> options = new ArrayList<>();
        ArrayList<Integer> weights = new ArrayList<>();
        if (out == null) return null;
        if (out[0]){
            //canRaid. (basic). this is always true for now.
            options.add(LordEvent.OffensiveType.RAID_GENERIC);
            weights.add(3);
        }
        if (out[1]){
            //canRaid. (industry)
            options.add(LordEvent.OffensiveType.RAID_INDUSTRY);
            weights.add(3);
        }
        if (out[2]){
            //canTacticalBomb
            options.add(LordEvent.OffensiveType.BOMBARD_TACTICAL);
            weights.add(10);
        }
        if (out[3]){
            //canInvade
            options.add(LordEvent.OffensiveType.NEX_GROUND_BATTLE);
            weights.add(20);
        }
        if (out[4]) {
            //canSatBomb.
            options.add(LordEvent.OffensiveType.BOMBARD_SATURATION);
            weights.add(20);
        }
        return Utils.weightedSample(options, weights, null);
    }
}
