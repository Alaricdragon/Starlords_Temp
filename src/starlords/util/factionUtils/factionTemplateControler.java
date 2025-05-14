package starlords.util.factionUtils;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.FactionAPI;
import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.util.Constants;
import starlords.util.factionUtils.customTemplates.factionTemplate_backup;

import java.util.HashMap;
import java.util.Iterator;

public class factionTemplateControler {
    private static HashMap<String,factionTemplate> templates = new HashMap<String,factionTemplate>();
    public static void addTemplate(factionTemplate template){
        templates.put(template.getFactionID(),template);
    }
    @SneakyThrows
    public static void init(){
        JSONObject jsonObject = Global.getSettings().getMergedJSONForMod("data/lords/faction.json", Constants.MOD_ID);
        templates = new HashMap<>();
        for (Iterator it2 = jsonObject.keys(); it2.hasNext();) {
            String key2 = (String) it2.next();
            JSONObject json2 = jsonObject.getJSONObject(key2);
            new factionTemplate(key2,json2);
        }
    }
    public static factionTemplate getTemplate(String factionID){
        if (templates.containsKey(factionID)) return templates.get(factionID);
        return new factionTemplate_backup(factionID);
    }
    public static factionTemplate getTemplate(FactionAPI faction){
        return getTemplate(faction.getId());
    }
}
