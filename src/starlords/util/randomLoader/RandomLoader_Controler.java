package starlords.util.randomLoader;

import com.fs.starfarer.api.Global;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;
import starlords.util.CsvFilerReader;
import starlords.util.ScriptedValues.SV_Base;
import starlords.util.ScriptedValues.ScriptedValueController;
import starlords.util.Utils;
import starlords.util.memoryUtils.Compressed.MemCompressedMasterList;
import starlords.util.memoryUtils.Compressed.MemCompressedOrganizer;


import java.util.HashMap;

import static starlords.util.memoryUtils.Compressed.MemCompressedMasterList.*;

public class RandomLoader_Controler {
    //this will load everything from randoms.
    //HashMap<String> arg;
    @SneakyThrows
    public static void init(){
        //todo: make sure this works because omg does it?!? does it or does it not!?!!? I dont knowwwwwwwwww afas
        //this needs to be ran just after the MemCompressedMasterList is initialized.
        //as this will happen --before-- all lords are loaded, lord json data can just be loaded afterwords.
        String path = "data/lords/randoms.csv";
        JSONArray jsons = Global.getSettings().loadCSV(path,true);
        HashMap<String,HashMap<String,JSONObject>> list = CsvFilerReader.computeFile(jsons,"Type");
        //MemCompressed_Lord lordmemory = (MemCompressed_Lord) MemCompressedMasterList.getMemory().get(LORD_KEY);
        for (String a : list.keySet()){
            for (String b : list.get(a).keySet()) {
                JSONObject json = list.get(a).get(b);
                String dataType = getDataType(json);
                SV_Base data = getDataFromJson(json, dataType);
                storeRandomInObject(json, dataType, data);
            }
        }



        //for lord upgrades:
        //if (lordmemory.hasItem(TYPE_UPGRADE_KEY)){
        //    lordmemory.setItem(TYPE_UPGRADE_KEY,new MemCompressed_Lord_WeightedRandom_Double());
        //}
        //MemCompressedOrganizer<Double, WeightedRandom> organizer = (MemCompressedOrganizer<Double, WeightedRandom>) lordmemory.getItem(TYPE_UPGRADE_KEY);
        //addUpgradeOrganizer(organizer,jsons);

    }
    @SneakyThrows
    private static SV_Base getDataFromJson(JSONObject json, String type){
        String valueS = "Value";
        ScriptedValueController calulater = new ScriptedValueController(json.getString(valueS));
        SV_Base out = switch (type) {
            case ScriptedValueController.TYPE_BOOLEAN -> calulater.getNextBoolean();
            case ScriptedValueController.TYPE_DOUBLE -> calulater.getNextDouble();
            case ScriptedValueController.TYPE_STRING -> calulater.getNextString();
            case ScriptedValueController.TYPE_OBJECT -> calulater.getNextObject();
            default -> null;
        };
        return out;
    }
    /*
    @SneakyThrows
    private static Object getDataFromJson(JSONObject json, String type){
        //this should output whatever thing is required here. be it an int, or something else.
        //but it really should be outputed as a _ object, but not yet inputted into anything.
        Object output = null;
        String[] vars;
        String[] vars1;
        String[] vars2;
        ArrayList<String> sList;
        ArrayList<String> sList1;
        ArrayList<Object> oList;
        ArrayList<Object> oList1;
        switch (type){
            case TYPE_DOUBLE:
                output = getData_Double(json, type);
                break;
            case TYPE_WR_DOUBLE:
                output = getData_WR_Double(json,type);
                break;
            case TYPE_PATH_DOUBLE:
                output = getData_Path_Double(json, type);
                break;
            case TYPE_RANDOM_LIST_DOUBLE:
                output = getData_List_Double(json, type);
                break;
            case TYPE_RANDOM_DOUBLE:
                output = getData_Random_Double(json, type);
                break;
            default:
                break;
        }
        return output;
    }
    private static final String valueS = "Value";
    @SneakyThrows
    private static Object getData_Double(JSONObject json, String type){
        double temp_0 = json.getDouble(valueS);
        return new MemCompressed_R_Double_Static(temp_0);
    }
    @SneakyThrows
    private static Object getData_WR_Double(JSONObject json,String type){
        String[] vars;
        vars = json.getString(valueS).split(":");
        //log.info("  name:"+vars[0]+", vars:"+vars[1]+","+vars[2]+","+vars[4]+","+vars[3]);//please dont ask me why 4 and 3 are mixed around.
        //WeightedRandomScripted temp_1 = new WeightedRandomScripted(Double.parseDouble(vars[0]),Double.parseDouble(vars[1]),Double.parseDouble(vars[3]),Double.parseDouble(vars[2]));
        Object[] outs = getDoubleAsScriptOrNot(vars);
        WeightedRandomScripted temp_1 = new WeightedRandomScripted(outs[0],outs[1],outs[3],outs[2]);
        return new MemCompressed_R_Double_WeightedRandom(temp_1);
    }
    @SneakyThrows
    private static Object getData_Path_Double(JSONObject json,String type){
        return Global.getSettings().getInstanceOfScript(json.getString(valueS));
    }
    @SneakyThrows
    private static Object getData_List_Double(JSONObject json,String type){
        //todo: this might cause crashes. If it does, I just need to see how to split the main 'Values'.
        String[] vars;
        String[] vars1;
        ArrayList<String> sList;
        ArrayList<String> sList1;
        ArrayList<Object> oList;
        ArrayList<Object> oList1;
        vars = json.getString(valueS).split('\n'+"");//all diffrent lines
        sList = new ArrayList<>();
        sList1 = new ArrayList<>();
        for (String a : vars){
            vars1 = a.split(":");
            sList.add(vars1[0]);
            sList.add(vars1[1]);
        }
        oList = getDoubleAsScriptOrNot(sList);
        oList1 = getDoubleAsScriptOrNot(sList1);
        return new MemCompressed_R_Double_RandomList(oList,oList1);


    }
    @SneakyThrows
    private static Object getData_Random_Double(JSONObject json,String type){
        String[] vars;
        vars = json.getString(valueS).split(':');
        //todo: please remember what I was doing: just making sure the 'randoms' have the currect inputs. its hard work.
    }
    private static ArrayList<Object> getDoubleAsScriptOrNot(ArrayList<String> data){
        ArrayList<Object> outs = new ArrayList<>();
        for (int a = 0; a < data.size(); a++){
            if (Utils.isScript(data.get(a)) != null){
                outs.add(data.get(a));
                continue;
            }
            outs.add(Double.parseDouble(data.get(a)));
        }
        return outs;
    }
    private static Object[] getDoubleAsScriptOrNot(String[] data){
        Object[] outs = new Object[data.length];
        for (int a = 0; a < outs.length; a++){
            if (Utils.isScript(data[a]) != null){
                outs[a] = data[a];
                continue;
            }
            outs[a] = Double.parseDouble(data[a]);
        }
        return outs;
    }*/
    @SneakyThrows
    private static void storeRandomInObject(JSONObject json, String type, SV_Base data){
        String id = json.getString("id");
        String randomType = json.getString("Type");
        MemCompressedOrganizer<?, ?> memory = getMemoryFromKey(randomType);
        if (memory == null){
            Utils.log.info("ERROR: failed to find memory object for random data of ID: "+id);
            return;
        }
        MemCompressedOrganizer<?, SV_Base> out = switch (type) {
            case ScriptedValueController.TYPE_BOOLEAN -> out = (MemCompressedOrganizer<?, SV_Base>) memory.getItem(MTYPE_KEY_BOOLEAN);
            case ScriptedValueController.TYPE_DOUBLE -> out = (MemCompressedOrganizer<?, SV_Base>) memory.getItem(MTYPE_KEY_DOUBLE);
            case ScriptedValueController.TYPE_STRING -> out = (MemCompressedOrganizer<?, SV_Base>) memory.getItem(MTYPE_KEY_STRING);
            case ScriptedValueController.TYPE_OBJECT -> out = (MemCompressedOrganizer<?, SV_Base>) memory.getItem(MTYPE_KEY_NO_CUSTOM);
            default -> null;
        };
        if (out == null){
            Utils.log.info("ERROR: failed to load random data of ID: "+id);
            return;
        }
        out.setItem(id,data);
    }
    /*@SneakyThrows
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
                MemCompressedOrganizer<Double, MemCompressed_R_Double_Base> temp_2 = (MemCompressedOrganizer<Double, MemCompressed_R_Double_Base>) memory.getItem(DOUBLE_KEY);
                temp_2.setItem(memoryID, (MemCompressed_R_Double_Base) data);
        }
    }*/
    private static MemCompressedOrganizer<?,?> getMemoryFromKey(String key){
        return switch (key) {
            case "LORD" -> MemCompressedMasterList.getMemory().get(KEY_LORD);
            case "FACTION" -> MemCompressedMasterList.getMemory().get(KEY_FACTION);
            case "PMC" -> MemCompressedMasterList.getMemory().get(KEY_PMC);
            default -> null;
        };
    }


    @SneakyThrows
    private static String getDataType(JSONObject json){
        return json.getString("Data_Type");
    }
    /*
    public static final String TYPE_WR_DOUBLE = "WEIGHTED_RANDOM:DOUBLE";
    public static final String TYPE_DOUBLE = "DOUBLE";
    public static final String TYPE_RANDOM_LIST_DOUBLE = "RANDOM_LIST:DOUBLE";
    public static final String TYPE_RANDOM_DOUBLE = "RANDOM:DOUBLE";
    public static final String TYPE_PATH_DOUBLE = "PATH:DOUBLE";

    public static final String TYPE_RANDOM_LIST_STRING = "RANDOM_LIST:STRING";
    public static final String TYPE_STRING = "STRING";
    public static final String TYPE_PATH_STRING = "PATH:STRING";

    public static final String TYPE_RANDOM_BOOLEAN = "RANDOM:BOOLEAN";
    public static final String TYPE_BOOLEAN = "BOOLEAN";
    public static final String TYPE_PATH_BOOLEAN = "PATH:BOOLEAN";

    public static final String TYPE_OTHER = "OTHER";
    @SneakyThrows
    private static String getDataType(JSONObject json){
        String[] allTypes = {
                TYPE_WR_DOUBLE,
                TYPE_DOUBLE,
                TYPE_RANDOM_LIST_DOUBLE,
                TYPE_RANDOM_DOUBLE,
                //TYPE_PATH_BOOLEAN

                TYPE_RANDOM_LIST_STRING,
                TYPE_STRING,
                //TYPE_PATH_STRING,

                TYPE_RANDOM_BOOLEAN,
                TYPE_BOOLEAN//,
                //TYPE_PATH_BOOLEAN
        };
        //note: all instances of 'path' can be checked by the  Utils.isScript() thing

        String data = json.getString("Data_Type");
        switch (data){
            case TYPE_DOUBLE:
                if (Utils.isScript(json.getString("Value")) != null) return TYPE_PATH_DOUBLE;
                return data;
            case TYPE_BOOLEAN:
                if (Utils.isScript(json.getString("Value")) != null) return TYPE_PATH_BOOLEAN;
                return data;
            case TYPE_STRING:
                if (Utils.isScript(json.getString("Value")) != null) return TYPE_PATH_STRING;
                return data;
            default:
                for (String a : allTypes){
                    if (a.equals(data)) return data;
                }
        }
        return null;
    }*/
}
