package starlords.util.randomLoader;

import com.fs.starfarer.api.Global;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;
import starlords.util.WeightedRandom;
import starlords.util.lordUpgrades.UpgradeController;
import starlords.util.memoryUtils.Compressed.MemCompressedMasterList;
import starlords.util.memoryUtils.Compressed.MemCompressedOrganizer;
import starlords.util.memoryUtils.Compressed.hTypes.MemCompressed_Random_Double_Base;
import starlords.util.memoryUtils.Compressed.hTypes.MemCompressed_Random_Double_Static;
import starlords.util.memoryUtils.Compressed.hTypes.MemCompressed_Random_Double_WeightedRandom;
import starlords.util.memoryUtils.Compressed.types.MemCompressed_Lord;

import static starlords.util.memoryUtils.Compressed.MemCompressedMasterList.*;

public class RandomLoader_Controler {
    //this will load everything from randoms.
    //HashMap<String> arg;
    @SneakyThrows
    public static void init(){
        //todo: make sure this works because omg does it?!? does it or does it not!?!!? I dont knowwwwwwwwww afas
        //this needs to be ran just after the MemCompressedMasterList is initialized.
        //as this will happen --before-- all lords are loaded, lord json data can just be loaded afterwords.
        String path = "data/lords/upgrades.csv";
        JSONArray jsons = Global.getSettings().loadCSV(path,true);
        MemCompressed_Lord lordmemory = (MemCompressed_Lord) MemCompressedMasterList.getMemory().get(LORD_KEY);
        for (int a = 0; a < jsons.length(); a++){
            JSONObject json = jsons.getJSONObject(a);
            //determine how the data type will be organized here.
            String dataType = getDataType(json);
            Object data = getDataFromJson(json,dataType);
            storeRandomInObject(json,dataType,data);
        }



        //for lord upgrades:
        //if (lordmemory.hasItem(TYPE_UPGRADE_KEY)){
        //    lordmemory.setItem(TYPE_UPGRADE_KEY,new MemCompressed_Lord_WeightedRandom_Double());
        //}
        //MemCompressedOrganizer<Double, WeightedRandom> organizer = (MemCompressedOrganizer<Double, WeightedRandom>) lordmemory.getItem(TYPE_UPGRADE_KEY);
        //addUpgradeOrganizer(organizer,jsons);

    }
    @SneakyThrows
    private static Object getDataFromJson(JSONObject json, String type){
        //this should output whatever thing is required here. be it an int, or something else.
        //but it really should be outputed as a _ object, but not yet inputted into anything.
        Object output = null;
        switch (type){
            case TYPE_DOUBLE:
                double temp_0 = json.getDouble("Value");
                output = new MemCompressed_Random_Double_Static(temp_0);
                break;
            case TYPE_WR_DOUBLE:
                String[] vars = json.getString("Value").split(":");
                //log.info("  name:"+vars[0]+", vars:"+vars[1]+","+vars[2]+","+vars[4]+","+vars[3]);//please dont ask me why 4 and 3 are mixed around.
                WeightedRandom temp_1 = new WeightedRandom(Double.parseDouble(vars[0]),Double.parseDouble(vars[1]),Double.parseDouble(vars[3]),Double.parseDouble(vars[2]));
                output = new MemCompressed_Random_Double_WeightedRandom(temp_1);
                break;
            case TYPE_PATH_DOUBLE:
                output = Global.getSettings().getInstanceOfScript(json.getString("Value"));
                break;
            default:
                break;
        }
        return output;
    }
    @SneakyThrows
    private static void storeRandomInObject(JSONObject json, String type, Object data){
        //this should place the object wereever it should go. I think it needs to be a MemCompressedHolder???? (or whatever item goes within)
        String randomType = json.getString("Type");
        String[] location = json.getString("Section").split(":");
        MemCompressedOrganizer<?, ?> memory = getMemoryFromKey(randomType);
        String memoryID = "";
        switch (location[0]){
            case "UPGRADE":
                memoryID = UpgradeController.getRandomDataID(location,json.getString("id"));
                //put a function that gets the required 'key' for this to work.
                //note: this is excluding the 'type' key. that is stored sepritly
                //yes, I know this kinda makes this inter thing a bit of a pain to use, but it is fine. I hope.
                break;
            default:
                memoryID = NO_CUSTOM_KEY+json.getString("id");
        }
        switch (type){
            case TYPE_WR_DOUBLE:
            case TYPE_DOUBLE:
            case TYPE_PATH_DOUBLE:
                MemCompressedOrganizer<Double, MemCompressed_Random_Double_Base> temp_2 = (MemCompressedOrganizer<Double, MemCompressed_Random_Double_Base>) memory.getItem(DOUBLE_KEY);
                temp_2.setItem(memoryID, (MemCompressed_Random_Double_Base) data);
        }
    }
    private static MemCompressedOrganizer<?,?> getMemoryFromKey(String key){
        switch (key){
            case "LORD":
                return MemCompressedMasterList.getMemory().get(LORD_KEY);
            case "FACTION":
                return MemCompressedMasterList.getMemory().get(FACTION_KEY);
            case "PMC":
                return MemCompressedMasterList.getMemory().get(PMC_KEY);
        }
        return null;//this will cause a crash, and for good reason. if it is triggering, something went wrong.
    }
    public static final String TYPE_WR_DOUBLE = "WEIGHTED_RANDOM_DOUBLE";
    public static final String TYPE_DOUBLE = "VALUE_DOUBLE";
    public static final String TYPE_PATH_DOUBLE = "PATH_DOUBLE";
    @SneakyThrows
    private static String getDataType(JSONObject json){
        switch (json.getString("DATA_TYPE")){
            case "WEIGHTED_RANDOM:DOUBLE":
                return TYPE_WR_DOUBLE;
            case "DOUBLE":
                return TYPE_DOUBLE;
            case "STRING:DOUBLE":
                return TYPE_PATH_DOUBLE;
        }
        return null;
    }
}
