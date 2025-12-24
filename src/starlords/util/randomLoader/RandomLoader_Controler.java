package starlords.util.randomLoader;

import com.fs.starfarer.api.Global;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;
import starlords.util.CsvFilerReader;
import starlords.util.ScriptedValues.SV_Base;
import starlords.util.ScriptedValues.SV_Double_Code;
import starlords.util.ScriptedValues.ScriptedValueController;
import starlords.util.memoryUtils.Compressed_outdated.MemCompressedMasterList;
import starlords.util.memoryUtils.Compressed_outdated.MemCompressedOrganizer;
import starlords.util.memoryUtils.Stats.StatsRandomOrganizer;
import starlords.util.memoryUtils.genaricLists.SubStaticPreparationData;


import java.util.HashMap;

import static starlords.util.memoryUtils.Compressed_outdated.MemCompressedMasterList.*;

public class RandomLoader_Controler {
    @SneakyThrows
    public static void init(){
        //todo: make sure this works because omg does it?!? does it or does it not!?!!? I dont knowwwwwwwwww afas
        //this needs to be ran just after the MemCompressedMasterList is initialized.
        //as this will happen --before-- all lords are loaded, lord json data can just be loaded afterwords.
        String path = "data/lords/randoms.csv";
        JSONArray jsons = Global.getSettings().loadCSV(path,true);
        HashMap<String,HashMap<String,JSONObject>> list = CsvFilerReader.computeFile(jsons,"Type");
        for (String a : list.keySet()){
            for (String b : list.get(a).keySet()) {
                JSONObject json = list.get(a).get(b);
                String dataType = getDataType(json);
                SV_Base data = getDataFromJson(json, dataType);
                storeRandomInObject(json, dataType, data);
            }
        }
    }
    @SneakyThrows
    private static SV_Base getDataFromJson(JSONObject json, String dataType){
        //Random ran = new Random(1000000000);
        String valueS = "Value";
        ScriptedValueController calulater = new ScriptedValueController(json.getString(valueS));
        SV_Base out = switch (dataType) {
            case ScriptedValueController.TYPE_BOOLEAN -> calulater.getNextBoolean();
            case ScriptedValueController.TYPE_STRING -> calulater.getNextString();
            case ScriptedValueController.TYPE_OBJECT -> calulater.getNextObject();
            case ScriptedValueController.TYPE_DOUBLE -> calulater.getNextDouble();
            case StatsRandomOrganizer.TYPE_BASE -> calulater.getNextDouble();
            case StatsRandomOrganizer.TYPE_MULTI -> calulater.getNextDouble();
            case StatsRandomOrganizer.TYPE_FLAT -> calulater.getNextDouble();
            default -> calulater.getNextDouble();//this is for stat mod changes.
        };
        return out;
    }
    @SneakyThrows
    private static void storeRandomInObject(JSONObject json, String dataType, SV_Base data){
        String id = json.getString("id");
        String randomType = json.getString("Type");

        if (id.contains(":")) {
            storeAsStat(id, randomType, dataType, data);
            return;
        }
        SubStaticPreparationData.setData(randomType,id,data);
    }
    private static void storeAsStat(String id, String randomType, String dataType, SV_Base data){
        String[] temp = dataType.split(":");
        //note: type is were the data is going. (TYPE?)
        //note: dataType is the type os stat. (DATA_TYE)
        //note: SV_Base is the data.
        //note: temp[1] is the stat, temp[2] is the id of this mod.
        StatsRandomOrganizer.setStatData_Base(randomType,temp[1],temp[2], (SV_Double_Code) data);
    }
    @SneakyThrows
    private static String getDataType(JSONObject json){
        return json.getString("Data_Type");
    }
}
