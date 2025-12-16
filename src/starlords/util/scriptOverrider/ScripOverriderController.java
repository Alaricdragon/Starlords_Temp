package starlords.util.scriptOverrider;

import java.util.HashMap;

public class ScripOverriderController {
    //this is just a list to beable to tell if a object has any 'overriding' scripts, without relying on 'Compressed_outdated' memory (because that would resalt in countless copy's being created. no good.)
    private HashMap<String,HashMap<String,String>> data = new HashMap<>();
    public String getScript(String category,String id){
        if (!data.containsKey(category) || !data.get(category).containsKey(id)) return null;
        return data.get(category).get(id);
    }
    public void addScript(String category,String id,String path){
        if (!data.containsKey(category)){
            data.put(category,new HashMap<>());
        }
        data.get(category).put(id,path);
    }
}
