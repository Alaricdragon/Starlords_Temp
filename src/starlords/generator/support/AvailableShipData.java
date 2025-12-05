package starlords.generator.support;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.impl.campaign.ids.ShipRoles;
import com.fs.starfarer.api.loading.RoleEntryAPI;
import lombok.Getter;
import org.apache.log4j.Logger;
import starlords.generator.LordGenerator;
import starlords.lunaSettings.StoredSettings;
import starlords.util.Utils;

import java.util.*;

public class AvailableShipData {
    public static final String HULLTYPE_CARRIER = "CARRIER", HULLTYPE_WARSHIP = "WARSHIP", HULLTYPE_PHASE = "PHASE", HULLTYPE_COMBATCIV = "COMBATCIV", HULLTYPE_TANKER = "TANKER", HULLTYPE_CARGO = "CARGO", HULLTYPE_PERSONNEL = "PERSONNEL", HULLTYPE_LINER = "LINER", HULLTYPE_TUG = "TUG", HULLTYPE_UTILITY = "UTILITY";
    public static final String HULLSIZE_FIGHTER = ShipAPI.HullSize.FIGHTER.name(), HULLSIZE_FRIGATE = ShipAPI.HullSize.FRIGATE.name(), HULLSIZE_DESTROYER = ShipAPI.HullSize.DESTROYER.name(), HULLSIZE_CRUISER = ShipAPI.HullSize.CRUISER.name(), HULLSIZE_CAPITALSHIP = ShipAPI.HullSize.CAPITAL_SHIP.name();
    //this is going to hold 5 hashsets, each linking to a diffrent type of ship. thats warship, carrier, phase, combat civ, and civ.
    //NOTE: this will contain things on a vareant by vareant bases because WHAT THE FUCK how doe this even fucking work!?!?!!!!?
    @Getter
    private static AvailableShipData defaultShips;
    private static HashMap<String, AvailableShipData> factionShips;
    @Getter
    @Deprecated
    private HashMap<String, Double> unorganizedShips = new HashMap<>(); //this is disorganized ships for the reason of
    //how this works, the first inputted string is the ship type (phase, carrier, extra), the second inputed string is the hullsize.
    @Getter
    private HashMap<String, HashMap<String,HashMap<String, Double>>> organizedShips = new HashMap<>();//hullsize, <hulltype, <varientID, weight>>
    public AvailableShipData(){
        startupType(HULLTYPE_CARRIER);
        startupType(HULLTYPE_WARSHIP);
        startupType(HULLTYPE_PHASE);
        startupType(HULLTYPE_COMBATCIV);
        startupType(HULLTYPE_TANKER);
        startupType(HULLTYPE_CARGO);
        startupType(HULLTYPE_LINER);
        startupType(HULLTYPE_PERSONNEL);
        startupType(HULLTYPE_TUG);
        startupType(HULLTYPE_UTILITY);
    }

    private void startupType(String type){
        HashMap<String,HashMap<String,Double>> a = new HashMap<>();
        a.put(HULLSIZE_FRIGATE,new HashMap<String,Double>());
        a.put(HULLSIZE_DESTROYER,new HashMap<String,Double>());
        a.put(HULLSIZE_CRUISER,new HashMap<String,Double>());
        a.put(HULLSIZE_CAPITALSHIP,new HashMap<String,Double>());
        organizedShips.put(type,a);
    }
    public static void startup(){
        //this reads and saves the default ship role of every ship in starsector (for ease of access, because by god)
        Logger log = Global.getLogger(StoredSettings.class);
        log.info("DEBUG: attempting to get AvailableShipData_OUTDATED startup");
        List[] carriers =  {
                Global.getSettings().getDefaultEntriesForRole(ShipRoles.CARRIER_LARGE),
                Global.getSettings().getDefaultEntriesForRole(ShipRoles.CARRIER_MEDIUM),
                Global.getSettings().getDefaultEntriesForRole(ShipRoles.CARRIER_SMALL)
        };
        List[] warships =  {
                Global.getSettings().getDefaultEntriesForRole(ShipRoles.COMBAT_CAPITAL),
                Global.getSettings().getDefaultEntriesForRole(ShipRoles.COMBAT_LARGE),
                Global.getSettings().getDefaultEntriesForRole(ShipRoles.COMBAT_MEDIUM),
                Global.getSettings().getDefaultEntriesForRole(ShipRoles.COMBAT_SMALL),
                Global.getSettings().getDefaultEntriesForRole(ShipRoles.COMBAT_SMALL_FOR_SMALL_FLEET)
        };
        List[] phase =  {
                Global.getSettings().getDefaultEntriesForRole(ShipRoles.PHASE_CAPITAL),
                Global.getSettings().getDefaultEntriesForRole(ShipRoles.PHASE_LARGE),
                Global.getSettings().getDefaultEntriesForRole(ShipRoles.PHASE_MEDIUM),
                Global.getSettings().getDefaultEntriesForRole(ShipRoles.PHASE_SMALL)
        };
        List[] combatCiv =  {
                Global.getSettings().getDefaultEntriesForRole(ShipRoles.COMBAT_FREIGHTER_LARGE),
                Global.getSettings().getDefaultEntriesForRole(ShipRoles.COMBAT_FREIGHTER_MEDIUM),
                Global.getSettings().getDefaultEntriesForRole(ShipRoles.COMBAT_FREIGHTER_SMALL)
        };
        List[] tanker = {
                Global.getSettings().getDefaultEntriesForRole(ShipRoles.TANKER_LARGE),
                Global.getSettings().getDefaultEntriesForRole(ShipRoles.TANKER_MEDIUM),
                Global.getSettings().getDefaultEntriesForRole(ShipRoles.TANKER_SMALL)
        };
        List[] cargo = {
                Global.getSettings().getDefaultEntriesForRole(ShipRoles.FREIGHTER_SMALL),
                Global.getSettings().getDefaultEntriesForRole(ShipRoles.FREIGHTER_MEDIUM),
                Global.getSettings().getDefaultEntriesForRole(ShipRoles.FREIGHTER_LARGE)
        };
        List[] liner = {
                Global.getSettings().getDefaultEntriesForRole(ShipRoles.LINER_SMALL),
                Global.getSettings().getDefaultEntriesForRole(ShipRoles.LINER_MEDIUM),
                Global.getSettings().getDefaultEntriesForRole(ShipRoles.LINER_LARGE),
        };
        List[] personnel = {
                Global.getSettings().getDefaultEntriesForRole(ShipRoles.PERSONNEL_SMALL),
                Global.getSettings().getDefaultEntriesForRole(ShipRoles.PERSONNEL_MEDIUM),
                Global.getSettings().getDefaultEntriesForRole(ShipRoles.PERSONNEL_LARGE)
        };
        List[] tug = {
                Global.getSettings().getDefaultEntriesForRole(ShipRoles.TUG)
        };
        List[] utility = {
                Global.getSettings().getDefaultEntriesForRole(ShipRoles.UTILITY)
        };
        defaultShips = new AvailableShipData();
        defaultShips.addListShips(carriers,HULLTYPE_CARRIER);
        defaultShips.addListShips(warships,HULLTYPE_WARSHIP);
        defaultShips.addListShips(phase,HULLTYPE_PHASE);
        defaultShips.addListShips(combatCiv,HULLTYPE_COMBATCIV);
        defaultShips.addListShips(tanker,HULLTYPE_TANKER);
        defaultShips.addListShips(cargo,HULLTYPE_CARGO);
        defaultShips.addListShips(liner,HULLTYPE_LINER);
        defaultShips.addListShips(personnel,HULLTYPE_PERSONNEL);
        defaultShips.addListShips(tug,HULLTYPE_TUG);
        defaultShips.addListShips(utility,HULLTYPE_UTILITY);
    }
    private void addListShips(List[] list, String type){
        for (Object a : list){
            List b = (List)a;
            for(Object c : b) {
                RoleEntryAPI d = (RoleEntryAPI) c;
                addShip(d.getVariantId(), d.getWeight()/*,b.getFPCost()*/, type);
            }
        }
    }
    public void addShip(String vareantID,double weight,String type){
        String hull = Global.getSettings().getVariant(vareantID).getHullSpec().getHullId();
        String size = Global.getSettings().getVariant(vareantID).getHullSpec().getHullSize().name();
        if (size.equals(HULLSIZE_FIGHTER)) {
            size = HULLSIZE_FRIGATE;
        }
        if (hull == null){
            //double checking if the hullID actually exists???? how does this even happen!?!
            return;
        }
        organizedShips.get(size).get(type).put(vareantID,weight);
    }
    public ShipData getRandomShip(){
        Object[] a = this.unorganizedShips.values().toArray();
        if (a.length == 0) return null;
        return (ShipData) a[(int)(LordGenerator.getRandom().nextInt(a.length))];
    }
    public String getRandomShip(String type,String size){
        Object[] a = this.organizedShips.get(type).get(size).keySet().toArray();
        if (a.length == 0) return null;
        return (String) a[(Utils.rand.nextInt(a.length))];
    }
    private void mergeShipsWithDefalt(FactionAPI fac,ArrayList<String> varents,String type){
        //fac.getVariantWeightForRole(FactionAPI.ShipPickMode.PRIORITY_THEN_ALL);
        for (String a : varents) {
            String trueType = type;
            String size = Global.getSettings().getVariant(a).getHullSpec().getHullSize().name();
            if (size.equals(HULLSIZE_FIGHTER)) {
                size = HULLSIZE_FRIGATE;
            }
            if (!defaultShips.getOrganizedShips().get(size).get(type).containsKey(a)) {
                for (String b : defaultShips.getOrganizedShips().get(size).keySet()) {
                    if (defaultShips.getOrganizedShips().get(size).get(b).containsKey(a)) {
                        if (b.equals(type)) {
                            break;
                        } else {
                            trueType = b;
                            break;
                        }
                    }
                }
            }
            addShip(a,fac.getVariantWeightForRole(a,FactionAPI.ShipPickMode.PRIORITY_THEN_ALL),trueType);
        }

    }
    private void getFactionsBaseShips(FactionAPI fac){
        /*
            so, the plan:
            stage 1: get all ships available to the faction.
            stage 2: for each ship, see if they have an entry inside of the 'defaltShips'. if so, the varients should use that data.
            stage 3:


        */
        HashMap<String,ArrayList<String>> a = new HashMap<>();
        Map<String, Float> overrides = fac.getVariantOverrides();
        Map<String, Float> hullFreq = fac.getHullFrequency();
        ArrayList<String> b;
        b = new ArrayList<>();
        b.addAll(fac.getVariantsForRole(ShipRoles.CARRIER_LARGE));
        b.addAll(fac.getVariantsForRole(ShipRoles.CARRIER_MEDIUM));
        b.addAll(fac.getVariantsForRole(ShipRoles.CARRIER_SMALL));
        a.put(HULLTYPE_CARRIER,b);
        mergeShipsWithDefalt(fac,b,HULLTYPE_CARRIER);

        b = new ArrayList<>();
        b.addAll(fac.getVariantsForRole(ShipRoles.COMBAT_CAPITAL));
        b.addAll(fac.getVariantsForRole(ShipRoles.COMBAT_LARGE));
        b.addAll(fac.getVariantsForRole(ShipRoles.COMBAT_MEDIUM));
        b.addAll(fac.getVariantsForRole(ShipRoles.COMBAT_SMALL));
        b.addAll(fac.getVariantsForRole(ShipRoles.COMBAT_SMALL_FOR_SMALL_FLEET));
        a.put(HULLTYPE_WARSHIP,b);
        mergeShipsWithDefalt(fac,b,HULLTYPE_WARSHIP);

        b = new ArrayList<>();
        b.addAll(fac.getVariantsForRole(ShipRoles.PHASE_CAPITAL));
        b.addAll(fac.getVariantsForRole(ShipRoles.PHASE_LARGE));
        b.addAll(fac.getVariantsForRole(ShipRoles.PHASE_MEDIUM));
        b.addAll(fac.getVariantsForRole(ShipRoles.PHASE_SMALL));
        a.put(HULLTYPE_PHASE,b);
        mergeShipsWithDefalt(fac,b,HULLTYPE_PHASE);

        b = new ArrayList<>();
        b.addAll(fac.getVariantsForRole(ShipRoles.FREIGHTER_SMALL));
        b.addAll(fac.getVariantsForRole(ShipRoles.FREIGHTER_MEDIUM));
        b.addAll(fac.getVariantsForRole(ShipRoles.FREIGHTER_LARGE));
        a.put(HULLTYPE_CARGO,b);
        mergeShipsWithDefalt(fac,b,HULLTYPE_CARGO);

        b = new ArrayList<>();
        b.addAll(fac.getVariantsForRole(ShipRoles.PERSONNEL_SMALL));
        b.addAll(fac.getVariantsForRole(ShipRoles.PERSONNEL_MEDIUM));
        b.addAll(fac.getVariantsForRole(ShipRoles.PERSONNEL_LARGE));
        a.put(HULLTYPE_PERSONNEL,b);
        mergeShipsWithDefalt(fac,b,HULLTYPE_PERSONNEL);

        b = new ArrayList<>();
        b.addAll(fac.getVariantsForRole(ShipRoles.TANKER_SMALL));
        b.addAll(fac.getVariantsForRole(ShipRoles.TANKER_MEDIUM));
        b.addAll(fac.getVariantsForRole(ShipRoles.TANKER_LARGE));
        a.put(HULLTYPE_TANKER,b);
        mergeShipsWithDefalt(fac,b,HULLTYPE_TANKER);

        b = new ArrayList<>();
        b.addAll(fac.getVariantsForRole(ShipRoles.TUG));
        a.put(HULLTYPE_TUG,b);
        mergeShipsWithDefalt(fac,b,HULLTYPE_TUG);


        //all a sets path this ponit are not very usefull?
        b = new ArrayList<>();
        b.addAll(fac.getVariantsForRole(ShipRoles.UTILITY));
        a.put(HULLTYPE_UTILITY,b);
        mergeShipsWithDefalt(fac,b,HULLTYPE_UTILITY);

        b = new ArrayList<>();
        b.addAll(fac.getVariantsForRole(ShipRoles.LINER_SMALL));
        b.addAll(fac.getVariantsForRole(ShipRoles.LINER_MEDIUM));
        b.addAll(fac.getVariantsForRole(ShipRoles.LINER_LARGE));
        a.put(HULLTYPE_LINER,b);
        mergeShipsWithDefalt(fac,b,HULLTYPE_LINER);

        b = new ArrayList<>();
        b.addAll(fac.getVariantsForRole(ShipRoles.COMBAT_SMALL_FOR_SMALL_FLEET));
        b.addAll(fac.getVariantsForRole(ShipRoles.COMBAT_FREIGHTER_SMALL));
        b.addAll(fac.getVariantsForRole(ShipRoles.COMBAT_FREIGHTER_MEDIUM));
        b.addAll(fac.getVariantsForRole(ShipRoles.COMBAT_FREIGHTER_LARGE));
        a.put(HULLTYPE_COMBATCIV,b);
        mergeShipsWithDefalt(fac,b,HULLTYPE_COMBATCIV);
    }
    public static AvailableShipData getAvailableShips(String factionID, String... type){
        AvailableShipData out = new AvailableShipData();
        AvailableShipData data = getOrCreateFactionShips(factionID);
        for (String a : type){
            out.organizedShips.get(HULLSIZE_CAPITALSHIP).put(a,data.organizedShips.get(HULLSIZE_CAPITALSHIP).get(a));
            out.organizedShips.get(HULLSIZE_CRUISER).put(a,data.organizedShips.get(HULLSIZE_CRUISER).get(a));
            out.organizedShips.get(HULLSIZE_DESTROYER).put(a,data.organizedShips.get(HULLSIZE_DESTROYER).get(a));
            out.organizedShips.get(HULLSIZE_FRIGATE).put(a,data.organizedShips.get(HULLSIZE_FRIGATE).get(a));
        }
        return out;
    }
    public static AvailableShipData getOrCreateFactionShips(String factionID){
        AvailableShipData out = new AvailableShipData();
        FactionAPI fac = Global.getSector().getFaction(factionID);
        if (factionShips.containsKey(factionID)) return factionShips.get(factionID);
        out.getFactionsBaseShips(fac);
        factionShips.put(factionID,out);
        return out;
    }
}
