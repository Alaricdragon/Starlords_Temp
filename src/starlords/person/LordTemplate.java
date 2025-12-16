package starlords.person;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import lombok.SneakyThrows;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import starlords.lunaSettings.StoredSettings;
import starlords.util.Utils;
import starlords.util.dialogControler.LordDialogController;
import starlords.util.overriders.OverridersDouble;
import starlords.util.overriders.OverridersString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

// Contains all immutable traits of a lord, from lords.json
@Deprecated
public final class LordTemplate {

    public final String name;
    public final String factionId;
    public final String fleetName;
    public final boolean isMale;
    public final LordPersonality personality;
    public final String flagShip;
    public final String lore;
    public final HashMap<String, Integer> shipPrefs;
    public final HashMap<String, Integer> customSkills;

    public final HashMap<String, Integer> customLordSMods;
    public final HashMap<String, Integer> customFleetSMods;
    public final boolean forceLordSMods;
    public final boolean forceFleetSMods;

    public final HashMap<String, List<String>> executiveOfficers;

    public final String fief;
    public final String portrait;
    public final int level;
    public final String battlePersonality;
    public final int ranking;
    public final String preferredItemId;

    public final ArrayList<String> dialogOverride;
    /*
    *      todo: shit to do here:
    *           -note: the CSV file will just hold links to jsons for single starlords. the reason for this is because starlords files are going to be massive for now on.
    *           -this requires a way to link repacement scripts for upgrade dataTypes (for fun TM)
    *           -this requires a way to make it so ships can spawn in very pasific raitios.
    *           -this requires a way for officers to spawn on certen ships with custom personalitys (create a script getter for getting what skills, looks and so on a person should have)
    *           -this requires a way for custom Smods to be handled. (maybe also a script just for it.)
    *           -this requires a way for custom 'action scripts' to be present. (command AI and flagship AI).
    *           -add custom tags to starlord.
    *           --maybe things like music as well.
    *           --no stratigic AI before the AI upgrade.
    *
    *      todo: stuff to do elsewere:
    *      todo: additional notes:
    *            -for modability reasons, it should be possable to have scripts for most things. such as, but not limited to:
    *            -see the LORD_JSON.md file for details.
    *
    *       todo: additional notes:
    *           by its nature, the lordTemplate file, and the starlord generator are linked in a way that is impossable to untie.
    *           theory:
    *               1) make it so creating a new starlord 'generates' a starlord for all stats that are not used.
    *                   -for this, do the following:
    *                       1.2) remove the LordTemplate file -in its intierity-. instead, replace it with some type of 'presistant data' class. this class will hold:
    *                           - all lord memory (dataHolder, Compressed_outdated, so on so forth)
    *                           - lord personality.
    *                           - lord fleet composition data
    *                           - lord action data and so forth.
    *                           - have a way to change this data that resalts in a backup being created (so there are only 2 copys when required, this will be helpfull in changing the starlord data later)
    *                           ! note: in regard to this, it turns out that the Lord itself is saved in intil. that's right! there is 100% no fucking reason to be putting anything inside of it into memory. its already saved, this just causes issues.
    *                                   how the fuck did I miss that up?!? there is nothing to save?!?! how how HOW??? aaaa.....
    *                       1.4) don't forget to add in a way to override the scrips for this value type. (on a lord, and faction basis.)
    *               2) remove the current implementation of saving lord templates in lord control, for something that instead saves lord data.
     *
     *
     *
     *
     *  todo:
     *        1) remove
     *
     * */

    @SneakyThrows
    private static boolean isScript(JSONObject object, String id){
        String thing = object.getString(id);
        if (thing.length() <= 1) return false;
        return thing.startsWith("~");
    }
    @SneakyThrows
    private static String getStringFromScript(JSONObject object, String id){
        String link = object.getString(id);
        OverridersString thing = (OverridersString) Global.getSettings().getInstanceOfScript(link);
        return thing.getValue();
    }
    @SneakyThrows
    private static double getDoubleFromScript(JSONObject object, String id){
        String link = object.getString(id);
        OverridersDouble thing = (OverridersDouble) Global.getSettings().getInstanceOfScript(link);
        return thing.getValue();
    }

    @SneakyThrows
    public LordTemplate(String name, JSONObject template){
        //note: person data has been compleatly revamped. I will use a diffrent methood of applying it.
        //note: the 'template' might no longer be required, in a sense. The template right now is being saved, as important static data. this is nearly redundant.
        //note: I need to swap out the template with something else. or rather, completely redo the template.
        //some template data will still need to be saved. this will require a diffrent system to handle the saved data on templates.
        //todo: I need to completely change lord formation. The template should be reserved for static data that will never change ever.















        this.name = name;
        switch (template.getString("faction").toLowerCase()) {
            case "hegemony":
                factionId = Factions.HEGEMONY;
                break;
            case "sindrian_diktat":
                factionId = Factions.DIKTAT;
                break;
            case "tritachyon":
                factionId = Factions.TRITACHYON;
                break;
            case "persean":
                factionId = Factions.PERSEAN;
                break;
            case "luddic_church":
                factionId = Factions.LUDDIC_CHURCH;
                break;
            case "pirates":
                factionId = Factions.PIRATES;
                break;
            case "luddic_path":
                factionId = Factions.LUDDIC_PATH;
                break;
            default:
                factionId = template.getString("faction");
        }
        fleetName = template.getString("fleetName");
        isMale = template.getBoolean("isMale");
        personality = LordPersonality.valueOf(template.getString("personality").toUpperCase());
        flagShip = template.getString("flagship");
        lore = template.getString("lore");
        portrait = template.getString("portrait");
        if (template.has("preferredItem")) {
            preferredItemId = template.getString("preferredItem");
        } else {  // everyone likes butter by default
            preferredItemId = "food";
        }
        // What kind of parser maps null to the string null???
        String fief = template.getString("fief").toLowerCase();  // TODO this could be case-sensitive
        this.fief = fief.equals("null") ? null : fief;
        battlePersonality = template.getString("battle_personality").toLowerCase();
        level = template.getInt("level");
        ranking = template.getInt("ranking");
        shipPrefs = new HashMap<>();
        JSONObject prefJson = template.getJSONObject("shipPref");
        for (Iterator it = prefJson.keys(); it.hasNext(); ) {
            String key = (String) it.next();
            shipPrefs.put(key, prefJson.getInt(key));
        }
        customSkills = new HashMap<>();
        if (template.has("customSkills")) {
            JSONObject skillJson = template.getJSONObject("customSkills");
            for (Iterator it = skillJson.keys(); it.hasNext();) {
                String key = (String) it.next();
                customSkills.put(key, skillJson.getInt(key));
            }
        }

        executiveOfficers = new HashMap<>();
        if (template.has("executiveOfficers") && Utils.secondInCommandEnabled()) {
            JSONObject officerJson = template.getJSONObject("executiveOfficers");
            for (Iterator it = officerJson.keys(); it.hasNext();) {
                String key = (String) it.next();
                if (!officerJson.isNull(key)) {
                    JSONArray aptitudeSkillList = officerJson.getJSONArray(key);
                    List<String> executiveOfficerSkills = new ArrayList<>();
                    for (int i = 0; i < aptitudeSkillList.length(); i++) {
                        executiveOfficerSkills.add(aptitudeSkillList.getString(i));
                    }
                    executiveOfficers.put(key, executiveOfficerSkills);
                }
            }
        }

        customLordSMods = new HashMap<String,Integer>();
        customFleetSMods = new HashMap<String,Integer>();
        if (template.has("customFleetSMods")) {
            JSONObject customSModsInTemplate = template.getJSONObject("customFleetSMods");
            for (Iterator it = customSModsInTemplate.keys(); it.hasNext();) {
                String key = (String) it.next();
                customFleetSMods.put(key,customSModsInTemplate.getInt(key));
            }
        }
        if (template.has("customLordSMods")) {
            JSONObject customSModsInTemplate = template.getJSONObject("customLordSMods");
            for (Iterator it = customSModsInTemplate.keys(); it.hasNext();) {
                String key = (String) it.next();
                customLordSMods.put(key,customSModsInTemplate.getInt(key));
            }
        }
        forceFleetSMods = !(template.has("fleetForceCustomSMods") && !template.getBoolean("fleetForceCustomSMods"));
        forceLordSMods = !(template.has("flagshipForceCustomSMods") && !template.getBoolean("flagshipForceCustomSMods"));
        dialogOverride = new ArrayList<>();
        if (template.has("dialogOverride")){
            JSONArray dialogConditions = template.getJSONArray("dialogOverride");
            for (int a = 0; a < dialogConditions.length(); a++){
                dialogOverride.add(dialogConditions.getString(a));
            }
        }
    }
    @SneakyThrows
    public LordTemplate(String name, JSONObject template,boolean isold){
        this.name = name;
        switch (template.getString("faction").toLowerCase()) {
            case "hegemony":
                factionId = Factions.HEGEMONY;
                break;
            case "sindrian_diktat":
                factionId = Factions.DIKTAT;
                break;
            case "tritachyon":
                factionId = Factions.TRITACHYON;
                break;
            case "persean":
                factionId = Factions.PERSEAN;
                break;
            case "luddic_church":
                factionId = Factions.LUDDIC_CHURCH;
                break;
            case "pirates":
                factionId = Factions.PIRATES;
                break;
            case "luddic_path":
                factionId = Factions.LUDDIC_PATH;
                break;
            default:
                factionId = template.getString("faction");
        }
        fleetName = template.getString("fleetName");
        isMale = template.getBoolean("isMale");
        personality = LordPersonality.valueOf(template.getString("personality").toUpperCase());
        flagShip = template.getString("flagship");
        lore = template.getString("lore");
        portrait = template.getString("portrait");
        if (template.has("preferredItem")) {
            preferredItemId = template.getString("preferredItem");
        } else {  // everyone likes butter by default
            preferredItemId = "food";
        }
        // What kind of parser maps null to the string null???
        String fief = template.getString("fief").toLowerCase();  // TODO this could be case-sensitive
        this.fief = fief.equals("null") ? null : fief;
        battlePersonality = template.getString("battle_personality").toLowerCase();
        level = template.getInt("level");
        ranking = template.getInt("ranking");
        shipPrefs = new HashMap<>();
        JSONObject prefJson = template.getJSONObject("shipPref");
        for (Iterator it = prefJson.keys(); it.hasNext(); ) {
            String key = (String) it.next();
            shipPrefs.put(key, prefJson.getInt(key));
        }
        customSkills = new HashMap<>();
        if (template.has("customSkills")) {
            JSONObject skillJson = template.getJSONObject("customSkills");
            for (Iterator it = skillJson.keys(); it.hasNext();) {
                String key = (String) it.next();
                customSkills.put(key, skillJson.getInt(key));
            }
        }

        executiveOfficers = new HashMap<>();
        if (template.has("executiveOfficers") && Utils.secondInCommandEnabled()) {
            JSONObject officerJson = template.getJSONObject("executiveOfficers");
            for (Iterator it = officerJson.keys(); it.hasNext();) {
                String key = (String) it.next();
                if (!officerJson.isNull(key)) {
                    JSONArray aptitudeSkillList = officerJson.getJSONArray(key);
                    List<String> executiveOfficerSkills = new ArrayList<>();
                    for (int i = 0; i < aptitudeSkillList.length(); i++) {
                        executiveOfficerSkills.add(aptitudeSkillList.getString(i));
                    }
                    executiveOfficers.put(key, executiveOfficerSkills);
                }
            }
        }

        customLordSMods = new HashMap<String,Integer>();
        customFleetSMods = new HashMap<String,Integer>();
        if (template.has("customFleetSMods")) {
            JSONObject customSModsInTemplate = template.getJSONObject("customFleetSMods");
            for (Iterator it = customSModsInTemplate.keys(); it.hasNext();) {
                String key = (String) it.next();
                customFleetSMods.put(key,customSModsInTemplate.getInt(key));
            }
        }
        if (template.has("customLordSMods")) {
            JSONObject customSModsInTemplate = template.getJSONObject("customLordSMods");
            for (Iterator it = customSModsInTemplate.keys(); it.hasNext();) {
                String key = (String) it.next();
                customLordSMods.put(key,customSModsInTemplate.getInt(key));
            }
        }
        forceFleetSMods = !(template.has("fleetForceCustomSMods") && !template.getBoolean("fleetForceCustomSMods"));
        forceLordSMods = !(template.has("flagshipForceCustomSMods") && !template.getBoolean("flagshipForceCustomSMods"));
        dialogOverride = new ArrayList<>();
        if (template.has("dialogOverride")){
            JSONArray dialogConditions = template.getJSONArray("dialogOverride");
            for (int a = 0; a < dialogConditions.length(); a++){
                dialogOverride.add(dialogConditions.getString(a));
            }
        }
    }
    @SneakyThrows
    public LordTemplate(PosdoLordTemplate template) {
        this.name = template.name;
        switch (template.factionId.toLowerCase()) {
            case "hegemony":
                factionId = Factions.HEGEMONY;
                break;
            case "sindrian_diktat":
                factionId = Factions.DIKTAT;
                break;
            case "tritachyon":
                factionId = Factions.TRITACHYON;
                break;
            case "persean":
                factionId = Factions.PERSEAN;
                break;
            case "luddic_church":
                factionId = Factions.LUDDIC_CHURCH;
                break;
            case "pirates":
                factionId = Factions.PIRATES;
                break;
            case "luddic_path":
                factionId = Factions.LUDDIC_PATH;
                break;
            default:
                factionId = template.factionId;
        }
        fleetName = template.fleetName;
        isMale = template.isMale;
        personality = LordPersonality.valueOf(template.personality.toUpperCase());
        flagShip = template.flagShip;
        lore = template.lore;
        portrait = template.portrait;
        if (template.preferredItemId != null && !template.preferredItemId.isEmpty()) {
            preferredItemId = template.preferredItemId;
        } else {  // everyone likes butter by default
            preferredItemId = "food";
        }
        // What kind of parser maps null to the string null???
        String fief = template.fief.toLowerCase();  // TODO this could be case-sensitive
        this.fief = fief.equals("null") ? null : fief;
        battlePersonality = template.battlePersonality.toLowerCase();
        level = template.level;
        ranking = template.ranking;
        shipPrefs = template.shipPrefs;
        customSkills = new HashMap<>();
        executiveOfficers = new HashMap<>();
        customLordSMods = new HashMap<String,Integer>();
        customFleetSMods = new HashMap<String,Integer>();
        forceFleetSMods = true;
        forceLordSMods = true;
        dialogOverride = new ArrayList<>();
    }
}
