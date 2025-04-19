package starlords.util;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.econ.Industry;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.combat.StatBonus;
import com.fs.starfarer.api.impl.campaign.CoreReputationPlugin;
import com.fs.starfarer.api.impl.campaign.ids.*;
import com.fs.starfarer.api.util.Misc;
import lombok.Setter;
import org.apache.log4j.Logger;
import org.lwjgl.util.vector.Vector2f;
import starlords.controllers.LordController;
import starlords.person.Lord;

import java.util.*;


public class Utils {

    private static final float SOMEWHAT_CLOSE_DIST = 1000;
    private static final float CLOSE_DIST = 500;
    private static final int FAST_PATROL_FP = 20;
    private static final int COMBAT_PATROL_FP = 40;
    private static final int HEAVY_PATROL_FP = 65;
    public static final Random rand = new Random(); // for low-priority rng that doesn't need to be savescum-proof
    public static String getFactionTitle(String faction,int ranking){
        String titleStr = "title_" + faction + "_" + ranking;
        String ret = StringUtil.getString("starlords_title", titleStr);
        if (ret != null && ret.startsWith("Missing string")) {
            ret = StringUtil.getString("starlords_title", "title_default_" + ranking);
        }
        return ret;
    }
    public static int nextInt(int bound) {
        return rand.nextInt(bound);
    }

    public static boolean nextBoolean() {
        return rand.nextBoolean();
    }

    public static boolean nexEnabled() {
        return Global.getSettings().getModManager().isModEnabled("nexerelin");
    }

    public static boolean secondInCommandEnabled() {
        return Global.getSettings().getModManager().isModEnabled("second_in_command");
    }

    public static void adjustPlayerReputation(PersonAPI target, int delta) {
        CoreReputationPlugin.CustomRepImpact param = new CoreReputationPlugin.CustomRepImpact();
        param.delta = delta / 100f;
        Global.getSector().adjustPlayerReputation(
                new CoreReputationPlugin.RepActionEnvelope(CoreReputationPlugin.RepActions.CUSTOM,
                        param, null, null, false, true),
                target);
    }

    public static List<MarketAPI> getFactionMarkets(String factionId)
    {
        List<MarketAPI> allMarkets = Global.getSector().getEconomy().getMarketsCopy();
        List<MarketAPI> ret = new ArrayList<>();
        for (MarketAPI market : allMarkets)
        {
            if (market.getFactionId().equals(factionId))
                ret.add(market);
        }
        return ret;
    }

    public static float getHyperspaceDistance(SectorEntityToken entity1, SectorEntityToken entity2)
    {
        if (entity1.getContainingLocation() == entity2.getContainingLocation()) return 0;
        return Misc.getDistance(entity1.getLocationInHyperspace(), entity2.getLocationInHyperspace());
    }

    public static float estimateMarketDefenses(MarketAPI market) {
        CampaignFleetAPI marketFleet = Misc.getStationFleet(market);
        float stationStrength = 0;
        if (marketFleet != null) {
            stationStrength += marketFleet.getFleetPoints();
        }
        // estimates patrol strength, from nex
        float patrolStrength = 0;

        int maxLight = (int) market.getStats().getDynamic().getMod(Stats.PATROL_NUM_LIGHT_MOD).computeEffective(0);
        int maxMedium = (int) market.getStats().getDynamic().getMod(Stats.PATROL_NUM_MEDIUM_MOD).computeEffective(0);
        int maxHeavy = (int) market.getStats().getDynamic().getMod(Stats.PATROL_NUM_HEAVY_MOD).computeEffective(0);

        patrolStrength += maxLight * FAST_PATROL_FP;
        patrolStrength += maxMedium * COMBAT_PATROL_FP;
        patrolStrength += maxHeavy * HEAVY_PATROL_FP;

        float fleetSizeMult = market.getStats().getDynamic().getMod(Stats.COMBAT_FLEET_SIZE_MULT).computeEffective(0f);
        fleetSizeMult = 1 + (fleetSizeMult - 1) * 0.75f;
        patrolStrength *= fleetSizeMult;

        return patrolStrength + stationStrength;
    }

    public static boolean canRaidIndustry(MarketAPI market) {
        for (Industry industry : market.getIndustries()) {
            if (!industry.canBeDisrupted()) continue;
            if (industry.getSpec().hasTag(Industries.TAG_UNRAIDABLE)) continue;
            return true;
        }
        return false;
    }

    public static Industry getIndustryToRaid(MarketAPI market) {
        ArrayList<Industry> options = new ArrayList<>();
        for (Industry industry : market.getIndustries()) {
            if (!industry.canBeDisrupted()) continue;
            if (industry.getSpec().hasTag(Industries.TAG_UNRAIDABLE)) continue;
            options.add(industry);
        }
        if (options.isEmpty()) return null;
        return options.get(new Random().nextInt(options.size()));
    }

    // sorts lords so player is first, then marshal, then by rank, then alphabetically within the same rank
    // used for a bunch of uis
    public static void canonicalLordSort(ArrayList<Lord> lords) {
        Collections.sort(lords, new Comparator<Lord>() {
            @Override
            public int compare(Lord o1, Lord o2) {
                if (o1.isPlayer()) return -1;
                if (o2.isPlayer()) return 1;
                if (o1.isMarshal()) return -1;
                if (o2.isMarshal()) return 1;
                if (o1.getRanking() != o2.getRanking()) return o2.getRanking() - o1.getRanking();
                return o1.getLordAPI().getNameString().compareTo(o2.getLordAPI().getNameString());
            }
        });
    }

    // convenience wrapper
    public static float getDaysSince(long timestamp) {
        return Global.getSector().getClock().getElapsedDaysSince(timestamp);
    }

    public static boolean isSomewhatClose(SectorEntityToken entity1, SectorEntityToken entity2) {
        return isClose(entity1, entity2, SOMEWHAT_CLOSE_DIST);
    }

    public static boolean isClose(SectorEntityToken entity1, SectorEntityToken entity2) {
        return isClose(entity1, entity2, CLOSE_DIST);
    }

    public static boolean isClose(SectorEntityToken entity1, SectorEntityToken entity2, float thres) {
        if (entity1 == null || entity2 == null) return false;
        LocationAPI loc1 = entity1.getContainingLocation();
        LocationAPI loc2 = entity2.getContainingLocation();
        if (loc1 == null || loc2 == null) return false;
        if (loc1.isHyperspace() && loc2.isHyperspace()) {
            return Misc.getDistance(entity1.getLocationInHyperspace(), entity2.getLocationInHyperspace()) < thres;
        }
        if (!loc1.isHyperspace() && !loc2.isHyperspace()) {
            return Misc.getDistance(entity1.getLocation(), entity2.getLocation()) < thres;
        }
        return false;
    }

    public static int getThreshold(RepLevel level) {
        int sign = 1;
        if (level.isAtBest(RepLevel.NEUTRAL)) sign = -1;
        return Math.round(sign * 100 * level.getMin());
    }

    public static String getNearbyDescription(CampaignFleetAPI fleet) {
        if (fleet.isInHyperspace()) {
            return "Hyperspace near " + Misc.getNearestStarSystem(fleet);
        } else {
            SectorEntityToken out = findNearestLocation(fleet);
            if (out != null && out.getName() != null) return out.getName() + " in " + fleet.getContainingLocation().getName();
            return "in "+fleet.getContainingLocation().getName();
        }
    }

    public static FactionAPI getRecruitmentFaction() {
        FactionAPI faction = Misc.getCommissionFaction();
        if (faction == null) faction = Global.getSector().getPlayerFaction();
        return faction;
    }

    public static float getRaidStr(CampaignFleetAPI fleet, float marines) {
        float attackerStr = marines;
        StatBonus stat = fleet.getStats().getDynamic().getMod(Stats.PLANETARY_OPERATIONS_MOD);
        attackerStr = stat.computeEffective(attackerStr);
        return attackerStr;
    }

    public static String getTitle(FactionAPI faction, int rank) {
        String titleStr = "title_" + faction.getId() + "_" + rank;
        String ret = StringUtil.getString("starlords_title", titleStr);
        if (ret != null && ret.startsWith("Missing string")) {
            ret = StringUtil.getString("starlords_title", "title_default_" + rank);
        }
        return ret;
    }

    public static <T> T weightedSample(List<T> data, List<Integer> weights, Random rand) {
        if (data.isEmpty()) return null;
        if (rand == null) rand = new Random();
        int totalWeight = 0;
        for (int w : weights) {
            totalWeight += w;
        }
        int choice = rand.nextInt(totalWeight);
        T curr = null;
        for (int i = 0; i < weights.size(); i++) {
            if (weights.get(i) > choice) {
                curr = data.get(i);
                break;
            } else {
                choice -= weights.get(i);
            }
        }
        return curr;
    }

    // gets number of major faction enemies of specified faction
    // ignores pirate and lordless factions

    /*public static int getNumMajorEnemies(FactionAPI faction) {
        int numEnemies = 0;
        for (FactionAPI faction2 : LordController.getFactionsWithLords()) {
            if (!Misc.isPirateFaction(faction2) && faction.isHostileTo(faction2)) numEnemies += 1;
        }
        return numEnemies;
    }*/
    public static int getNumMajorEnemiesForAttacks(FactionAPI faction){
        int numEnemies = 0;
        for (FactionAPI faction2 : LordController.getFactionsWithLords()) {
            if (Utils.canBeAttacked(faction2) && faction.isHostileTo(faction2)) numEnemies += 1;
        }
        return numEnemies;
    }
    public static int getNumMajorEnemiesForDiplomacy(FactionAPI faction){
        int numEnemies = 0;
        for (FactionAPI faction2 : LordController.getFactionsWithLords()) {
            if (Utils.canHaveRelations(faction2) && faction.isHostileTo(faction2)) numEnemies += 1;
        }
        return numEnemies;
    }
    public static PersonAPI getLeader(FactionAPI faction) {
        switch (faction.getId()) {
            case Factions.PIRATES:
            case Factions.LUDDIC_CHURCH:
                return null;
            case Factions.HEGEMONY:
                return Global.getSector().getImportantPeople().getPerson(People.DAUD);
            case Factions.PERSEAN:
                return Global.getSector().getImportantPeople().getPerson(People.REYNARD_HANNAN);
            case Factions.DIKTAT:
                return Global.getSector().getImportantPeople().getPerson(People.ANDRADA);
            case Factions.LUDDIC_PATH:
                return Global.getSector().getImportantPeople().getPerson(People.COTTON);
            case Factions.TRITACHYON:
                return Global.getSector().getImportantPeople().getPerson(People.SUN);
            case Factions.PLAYER:
                return Global.getSector().getPlayerPerson();
        }
        return null;
    }

    public static String getLiegeName(FactionAPI faction) {
        switch (faction.getId()) {
            case Factions.PIRATES:
                return null;
            case Factions.HEGEMONY:
                return "Baikal Daud";
            case Factions.PERSEAN:
                return "Reynard Hannan";
            case Factions.DIKTAT:
                return "Phillip Andrada";
            case Factions.LUDDIC_CHURCH:
                return "The Pope";
            case Factions.LUDDIC_PATH:
                return "Livewell Cotton";
            case Factions.TRITACHYON:
                return "Artemisia Sun";
            case Factions.PLAYER:
                return Global.getSector().getPlayerPerson().getNameString();
        }
        return null;
    }

    public static PersonAPI clonePerson(PersonAPI person) {
        PersonAPI clone = person.getFaction().createRandomPerson(person.getGender());
        clone.setPortraitSprite(person.getPortraitSprite());
        clone.setPersonality(person.getPersonalityAPI().getId());
        clone.setName(person.getName());
        clone.setVoice(person.getVoice());
        clone.setImportance(person.getImportance());
        clone.setMarket(person.getMarket());
        clone.setPostId(person.getPostId());
        clone.setRankId(person.getRankId());
        clone.getStats().setLevel(person.getStats().getLevel());
        return clone;
    }

    public static SectorEntityToken findNearestMarket(SectorEntityToken target){
        SectorEntityToken output = null;
        MarketAPI marketTemp = Misc.findNearestLocalMarket(target, 1e10f, null);
        if (marketTemp != null && marketTemp.getPrimaryEntity() != null) output = marketTemp.getPrimaryEntity();
        if (output != null) return output;
        List<MarketAPI> a = Global.getSector().getEconomy().getMarketsCopy();

        //give up on finding a market in system. just get the closest one.
        MarketAPI sameFaction = null;
        MarketAPI friendly = null;
        MarketAPI notHostile = null;
        MarketAPI any = null;
        float sameFactionD = 99999999;
        float friendlyD = 99999999;
        float notHostileD = 99999999;
        float anyD = 99999999;
        for (MarketAPI b : a){
            if (b.getPrimaryEntity().getFaction().equals(target.getFaction())){
                float x = target.getLocationInHyperspace().x - b.getPrimaryEntity().getLocationInHyperspace().x;
                float y = target.getLocationInHyperspace().y - b.getPrimaryEntity().getLocationInHyperspace().y;
                x = Math.max(-1*x,x);
                y = Math.max(-1*y,y);
                float d = x+y;
                if (d < sameFactionD){
                    sameFactionD = d;
                    sameFaction = b;
                }
            }
            if (b.getPrimaryEntity().getFaction().getRelationshipLevel(target.getFaction()).isAtWorst(RepLevel.FAVORABLE)){
                float x = target.getLocationInHyperspace().x - b.getPrimaryEntity().getLocationInHyperspace().x;
                float y = target.getLocationInHyperspace().y - b.getPrimaryEntity().getLocationInHyperspace().y;
                x = Math.max(-1*x,x);
                y = Math.max(-1*y,y);
                float d = x+y;
                if (d < friendlyD){
                    friendlyD = d;
                    friendly = b;
                }
            }
            if (b.getPrimaryEntity().getFaction().getRelationshipLevel(target.getFaction()).isAtWorst(RepLevel.NEUTRAL)){
                float x = target.getLocationInHyperspace().x - b.getPrimaryEntity().getLocationInHyperspace().x;
                float y = target.getLocationInHyperspace().y - b.getPrimaryEntity().getLocationInHyperspace().y;
                x = Math.max(-1*x,x);
                y = Math.max(-1*y,y);
                float d = x+y;
                if (d < notHostileD){
                    notHostileD = d;
                    notHostile = b;
                }
            }
            float x = target.getLocationInHyperspace().x - b.getPrimaryEntity().getLocationInHyperspace().x;
            float y = target.getLocationInHyperspace().y - b.getPrimaryEntity().getLocationInHyperspace().y;
            x = Math.max(-1*x,x);
            y = Math.max(-1*y,y);
            float d = x+y;
            if (d < anyD){
                anyD = d;
                any = b;
            }
        }
        if (sameFaction != null) return sameFaction.getPlanetEntity();
        if (friendly != null) return friendly.getPlanetEntity();
        if (notHostile != null) return notHostile.getPlanetEntity();
        if (any != null) return any.getPlanetEntity();

        //if none exist, give up. give me a planet at random. who even cares anymore????
        //also of note: this is a type of emergency backup to prevent crashes. it should never run for any reason ever. but it can, if it must.
        List<StarSystemAPI> d = Global.getSector().getStarSystems();
        for (StarSystemAPI b : d){
            if(b.getPlanets().size() != 0){
                output = b.getPlanets().get(0);
                break;
            }
        }
        if (output != null) return output;

        Logger log = Global.getLogger(Utils.class);
        log.info("there is only the void, empty and terrible. nowhere to respawn. nowhere to stand. it is all gone. what have you done? why?");
        log.info("was it worth it?");
        return null;
    }

    public static SectorEntityToken findNearestLocation(SectorEntityToken target){
        SectorEntityToken output = findAnyStaticEntity(target);
        if (output != null) return output;
        output = Misc.getNearestStarSystem(target).getCenter();
        if (output != null){
            output = findAnyStaticEntity(output);
            if (output != null) return output;
        }
        Logger log = Global.getLogger(Utils.class);
        log.info("ERROR: failed to acquire anything in a targeted system. and I am forced to ask: what on earth was going on? if you get this error, something went wrong in starlords. please report this to the modders responsible for that wonderful mod.");
        //try one final time to get any starsystem, please please, get something to go to
        StarSystemAPI starSystem = Misc.getNearbyStarSystem(target);
        output = findAnyStaticEntity(starSystem.getCenter());
        if (output != null) return output;
        //at this point, why even bother? like, error handling is going to be required. the system does not even exist! wtf..
        return null;
    }
    public static SectorEntityToken findAnyStaticEntity(SectorEntityToken target){
        SectorEntityToken output = Misc.findNearestPlanetTo(target,false,true);
        if (output != null) return output;
        if (!target.isInHyperspace() && target.getStarSystem() != null){
            if (target.getStarSystem().getJumpPoints() != null && target.getStarSystem().getJumpPoints().size() != 0){
                return getClosest(target.getLocation(),target.getStarSystem().getJumpPoints());
            }
            if (target.getStarSystem().getStar() != null){
                return target.getStarSystem().getStar();
            }
            if (target.getStarSystem().getCenter() != null){
                return target.getStarSystem().getCenter();
            }
            if (target.getStarSystem().getAsteroids() != null && target.getStarSystem().getAsteroids().size() != 0){
                return getClosest(target.getLocation(),target.getStarSystem().getAsteroids());
            }
            if(target.getStarSystem().getAllEntities() != null && target.getStarSystem().getAllEntities().size() != 0){
                return getClosest(target.getLocation(),target.getStarSystem().getAllEntities());
            }
            return null;
            //at this point, I need to start to look into nearby starsystems.
        }
        return null;
    }
    private static SectorEntityToken getClosest(Vector2f a, List<SectorEntityToken> b){
        //i didn't test this. like, at all. I really really hope this does not have issues.
        SectorEntityToken output = null;
        float distance = 999999999;
        for (SectorEntityToken c : b){
            float x = a.x - c.getLocation().x;
            float y = a.y - c.getLocation().y;
            x = Math.max(-1*x,x);
            y = Math.max(-1*y,y);
            float d = x+y;
            if (d < distance){
                distance = d;
                output = c;
            }
        }
        return output;
    }
    //determines if a given market can be attacked (some markets should be be attacked. like, hidden dustkeeper markets darn it.)
    @Setter
    private static HashSet<String> forcedAttack;
    @Setter
    private static HashSet<String> forcedNoAttack;
    public static boolean canBeAttacked(MarketAPI market) {
        if (!canBeAttacked(market.getFaction())) return false;
        if (!Global.getSettings().getModManager().isModEnabled("nexerelin")) return true;
        return NexerlinUtilitys.canBeAttacked(market);
    }
    //determines if a faction can be attacked at all. (pirates cant be raided for example. (or at least I think so))
    public static boolean canBeAttacked(FactionAPI faction){
        HashSet<String> forced = forcedAttack;
        HashSet<String> prevented = forcedNoAttack;
        if (forced.contains(faction.getId())) return true;
        if (prevented.contains(faction.getId())) return false;
        //if (isMinorFaction(faction)) return false;
        if (!Global.getSettings().getModManager().isModEnabled("nexerelin")) return !isMinorFaction(faction);
        return NexerlinUtilitys.canBeAttacked(faction);
    }
    //determines if a faction can have there relations change. (aka, pirates don't have relationship changes. nore do some modded content.)
    @Setter
    private static HashSet<String> forcedRelations;
    @Setter
    private static HashSet<String> forcedNoRelations;
    public static boolean canHaveRelations(FactionAPI faction){
        HashSet<String> forced = forcedRelations;
        HashSet<String> prevented = forcedNoRelations;
        if (forced.contains(faction.getId())) return true;
        if (prevented.contains(faction.getId())) return false;
        //if (isMinorFaction(faction)) return false;
        if (!Global.getSettings().getModManager().isModEnabled("nexerelin")) return !isMinorFaction(faction);//return true;
        return NexerlinUtilitys.canChangeRelations(faction);
    }
    //prevents some factions from having war / peace declared, engaging/getting in invasions, and being raided (like pirates, for not pirates)

    @Setter
    private static HashSet<String> forcedMinorFaction;
    @Setter
    private static HashSet<String> forcedNotMinorFaction;
    public static boolean isMinorFaction(FactionAPI faction){
        HashSet<String> forced = forcedMinorFaction;
        HashSet<String> prevented = forcedNotMinorFaction;
        if (forced.contains(faction.getId())) return true;
        if (prevented.contains(faction.getId())) return false;
        if (!Global.getSettings().getModManager().isModEnabled("nexerelin")) return Misc.isPirateFaction(faction);
        //to do: use nexerlin to determine if a faction is a minor faction.
        return NexerlinUtilitys.isMinorFaction(faction);
    }
}
