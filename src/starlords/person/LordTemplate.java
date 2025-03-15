package starlords.person;

import com.fs.starfarer.api.impl.campaign.ids.Factions;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;

// Contains all immutable traits of a lord, from lords.json
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
    public final String fief;
    public final String portrait;
    public final int level;
    public final String battlePersonality;
    public final int ranking;
    public final String preferredItemId;

    @SneakyThrows
    public LordTemplate(String name, JSONObject template){
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
        if (template.preferredItemId != null && !template.preferredItemId.equals("")) {
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
    }
}
