package starlords.controllers;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.comm.IntelInfoPlugin;
import com.fs.starfarer.api.impl.campaign.intel.BaseIntelPlugin;
import starlords.person.Lord;

import java.util.HashMap;
import java.util.List;

import static starlords.util.Constants.DEBUG_MODE;
import static starlords.util.Constants.STARTING_LOYALTY;

// tracks relations between all lords, lieges, and player
public class RelationController extends BaseIntelPlugin {

    private HashMap<String, Integer> lordMap;
    private int[][] lordRelations;
    private int[][] factionRelations;
    private HashMap<String, Integer> factionIdxMap = new HashMap<>(); // This belongs somewhere else eventually

    private static RelationController instance;

    private RelationController() {
        setHidden(true);
        int numLords = LordController.getLordsList().size();
        List<FactionAPI> factions = Global.getSector().getAllFactions();
        int numLieges = 24 + factions.size();  // save some space for adding new factions
        for (FactionAPI faction : factions) {
            factionIdxMap.put(faction.getId(), factionIdxMap.size());
        }
        lordRelations = new int[numLords][numLords];
        factionRelations = new int[numLieges][numLords];
        lordMap = new HashMap<>();
        /*// set initial relations
        for (int i = 0; i < lordRelations.length; i++) {
            lordRelations[i][i] = 100; // lords love themselves!
        }*/
        int a = 0;
        for (Lord lord : LordController.getLordsList()) {
            //set initial relations and create lord hashmap
            lordMap.put(lord.getLordAPI().getId(),a);
            lordRelations[a][a] = 100; // lords love themselves!
            //create default relations each faction has with there lords
            int factionIdx = factionIdxMap.get(lord.getLordAPI().getFaction().getId());
            factionRelations[factionIdx][a] = STARTING_LOYALTY;
            if (DEBUG_MODE) {
                factionRelations[factionIdx][a] = 0;
            }
            a++;
        }
    }
    private static int getIndexOfLord(Lord lord){
        return instance.lordMap.get(lord.getLordAPI().getId());
    }
    public static void addLord(Lord lord){
        //initalization data
        int numLords = LordController.getLordsList().size();
        List<FactionAPI> factions = Global.getSector().getAllFactions();
        int numLieges = 24 + factions.size();  // save some space for adding new factions

        //gets the new lord relationship default.
        int[][] tempa = instance.lordRelations;
        instance.lordRelations = new int[numLords][numLords];
        for (int a = 0; a < tempa.length; a++){
            for (int b = 0; b < tempa[a].length; b++){
                instance.lordRelations[a][b] = tempa[a][b];
            }
        }
        instance.lordMap.put(lord.getLordAPI().getId(),instance.lordRelations.length-1);
        instance.lordRelations[getIndexOfLord(lord)][getIndexOfLord(lord)] = 100;

        //gets the new lord factionRelationship default.
        tempa = instance.factionRelations;
        instance.factionRelations = new int[numLieges][numLords];
        for (int a = 0; a < tempa.length; a++){
            for (int b = 0; b < tempa[a].length; b++){
                instance.factionRelations[a][b] = tempa[a][b];
            }
        }

        int factionIdx = instance.factionIdxMap.get(lord.getLordAPI().getFaction().getId());
        instance.factionRelations[factionIdx][getIndexOfLord(lord)] = STARTING_LOYALTY;
        if (DEBUG_MODE) {
            instance.factionRelations[factionIdx][getIndexOfLord(lord)] = 0;
        }
    }
    public static void removeLord(Lord lord){
        int index = getIndexOfLord(lord);

        //recreates the relationship array without the inputted lord.
        int[][] tempa = instance.lordRelations;
        instance.lordRelations = new int[instance.lordRelations.length - 1][instance.lordRelations.length - 1];
        for (int a = 0; a < index; a++){
            /*for (int b = 0; b < index; b++){
                instance.lordRelations[a][b] = tempa[a][b];
            }*/
            for (int b = index+1; b < tempa[a].length; b++){
                instance.lordRelations[a][b-1] = tempa[a][b];
            }
        }
        for (int a = tempa.length+1; a < tempa.length; a++){
            for (int b = 0; b < index; b++){
                instance.lordRelations[a-1][b] = tempa[a][b];
            }
            for (int b = index+1; b < tempa[a].length; b++){
                instance.lordRelations[a-1][b-1] = tempa[a][b];
            }
        }

        //recreates the faction relationship array without the inputted lord.
        tempa = instance.factionRelations;
        instance.factionRelations = new int[instance.factionRelations.length][LordController.getLordsList().size() - 1];
        for (int a = 0; a < tempa.length; a++){
            /*for (int b = 0; b < index; b++){
                instance.lordRelations[a][b] = tempa[a][b];
            }*/
            for (int b = index+1; b < tempa[a].length; b++){
                instance.lordRelations[a][b-1] = tempa[a][b];
            }
        }
        //finally, removes the lord from the hashmap. never to be seen again...
        instance.lordMap.remove(lord.getLordAPI().getId());
    }
    public static void tryToAddlordMapMidGame(){
        if (instance.lordMap != null) return;
        instance.lordMap = new HashMap<>();
        int a = 0;
        for (Lord lord : LordController.getLordsList()) {
            instance.lordMap.put(lord.getLordAPI().getId(),a);
            a++;
        }
    }

    public static void modifyRelation(Lord lord1, Lord lord2, int amount) {
        if (lord1.isPlayer())  {
            lord2.getLordAPI().getRelToPlayer().adjustRelationship(amount / 100f, null);
            return;
        }
        if (lord2.isPlayer()) {
            lord1.getLordAPI().getRelToPlayer().adjustRelationship(amount / 100f, null);
            return;
        }
        int idx1 = getIndexOfLord(lord1);
        int idx2 = getIndexOfLord(lord2);
        int newRel = Math.max(-100, Math.min(100, getInstance().lordRelations[Math.min(idx1, idx2)][Math.max(idx1, idx2)] + amount));
        getInstance().lordRelations[Math.min(idx1, idx2)][Math.max(idx1, idx2)] = newRel;
    }

    public static int getRelation(Lord lord1, Lord lord2) {
        if (lord1.isPlayer())  {
            return lord2.getLordAPI().getRelToPlayer().getRepInt();
        }
        if (lord2.isPlayer()) {
            return lord1.getLordAPI().getRelToPlayer().getRepInt();
        }
        int idx1 = getIndexOfLord(lord1);
        int idx2 = getIndexOfLord(lord2);
        return getInstance().lordRelations[Math.min(idx1, idx2)][Math.max(idx1, idx2)];
    }

    public static void modifyLoyalty(Lord lord, int amount) {
        modifyLoyalty(lord, lord.getFaction().getId(), amount);
    }

    public static void modifyLoyalty(Lord lord, String factionId, int amount) {
        if (Global.getSector().getPlayerFaction().getId().equals(factionId)) {
            modifyRelation(lord, LordController.getPlayerLord(), amount);
        } else {
            int newLoyalty = Math.min(100, Math.max(-100, amount +
                    getInstance().factionRelations[getFactionIdx(factionId)][getIndexOfLord(lord)]));
            getInstance().factionRelations[getFactionIdx(factionId)][getIndexOfLord(lord)] = newLoyalty;
        }
    }

    public static int getLoyalty(Lord lord) {
        return getLoyalty(lord, lord.getLordAPI().getFaction().getId());
    }

    public static int getLoyalty(Lord lord, String factionId) {
        if (Global.getSector().getPlayerFaction().getId().equals(factionId)) return lord.getPlayerRel();
        return getInstance().factionRelations[getFactionIdx(factionId)][getIndexOfLord(lord)];
    }

    private static int getFactionIdx(String factionId) {
        HashMap<String, Integer> tmp = getInstance().factionIdxMap;
        if (!tmp.containsKey(factionId)) {
            // new faction must've been added after gamestart
            tmp.put(factionId, tmp.size());
        }
        return tmp.get(factionId);
    }

    public static RelationController getInstance(boolean forceReset) {
        if (instance == null || forceReset) {
            List<IntelInfoPlugin> intel = Global.getSector().getIntelManager().getIntel(RelationController.class);
            if (intel.isEmpty()) {
                instance = new RelationController();
                Global.getSector().getIntelManager().addIntel(instance, true);
            } else {
                if (intel.size() > 1) {
                    throw new IllegalStateException("Should only be one RelationController intel registered");
                }
                instance = (RelationController) intel.get(0);
            }
        }
        return instance;
    }

    public static RelationController getInstance() {
        return getInstance(false);
    }
}
