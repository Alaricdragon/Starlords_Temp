package starlords.util.factionUtils;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.FactionAPI;
import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.util.Constants;
import starlords.util.factionUtils.customTemplates.factionTemplate_backup;

import java.util.HashMap;
import java.util.Iterator;

public class FactionTemplateController {
    private static HashMap<String, FactionTemplate> templates = new HashMap<String, FactionTemplate>();
    public static void addTemplate(FactionTemplate template){
        templates.put(template.getFactionID(),template);
    }
    @SneakyThrows
    public static void init(){
        //todo: change this to run when settings change (and also reset the templates then, so they can be set to new defaults?)
        JSONObject jsonObject = Global.getSettings().getMergedJSONForMod("data/lords/faction.json", Constants.MOD_ID);
        templates = new HashMap<>();
        for (Iterator it2 = jsonObject.keys(); it2.hasNext();) {
            String key2 = (String) it2.next();
            JSONObject json2 = jsonObject.getJSONObject(key2);
            new FactionTemplate(key2,json2);
        }
    }
    public static FactionTemplate getTemplate(String factionID){
        if (templates.containsKey(factionID)) return templates.get(factionID);
        return new factionTemplate_backup(factionID);
    }
    public static FactionTemplate getTemplate(FactionAPI faction){
        return getTemplate(faction.getId());
    }
}
