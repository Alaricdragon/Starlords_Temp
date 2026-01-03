package starlords.util.randomStrings;

import com.fs.starfarer.api.Global;
import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.person.Lord;
import starlords.util.CsvFilerReader;
import starlords.util.ScriptedValues.SV_Boolean;
import starlords.util.ScriptedValues.SV_Double;
import starlords.util.ScriptedValues.SV_String;
import starlords.util.ScriptedValues.ScriptedValueController;
import starlords.util.Utils;
import starlords.util.dialogControler.DialogSet;

import java.util.ArrayList;
import java.util.HashMap;

import static starlords.util.Constants.STARLORD_STRING_FLEETNAMES_MEMORY_KEY;

public class FleetNameController {
    /*todo:
    *   so there is an issue here:
    *   I need to set data before the generator starts, and after it ends. so I need functions for that....
    *   the reason why I am not just keeping this data loaded is just a small thing to reduce ram useage. I am attempting to get into the habit.
    *   also this data is NOT reuqired all the time. the additional load time for the generator should be fine?*/
    private static HashMap<String,Integer> load(){
        HashMap<String,Integer> data;
        String key = STARLORD_STRING_FLEETNAMES_MEMORY_KEY;
        if (Global.getSector().getMemory().contains(key)){
            data = (HashMap<String,Integer>) Global.getSector().getMemory().get(key);
        }else{
            data = new HashMap<String,Integer>();
        }
        return data;
    }
    private static void save(HashMap<String,Integer> data){
        String key = STARLORD_STRING_FLEETNAMES_MEMORY_KEY;
        Global.getSector().getMemory().set(key,data);
    }
    @SneakyThrows
    private static HashMap<String,FleetNameController> loadCSV(){
        String path = "data/lords/strings/fleetNames.csv";
        HashMap<String, HashMap<String, JSONObject>> csv = CsvFilerReader.computeFile(Global.getSettings().loadCSV(path, true),"type");
        HashMap<String,FleetNameController> fleetNames = new HashMap<>();
        for (String a : csv.get("LORD").keySet()) fleetNames.put(a,new FleetNameController(csv.get("LORD").get(a)));
        return fleetNames;
    }
    public static String generateFleetName(Lord lord){
        HashMap<String,Integer> data = load();
        HashMap<String,FleetNameController> fleetNames = loadCSV();
        String gottenID="";
        double totalWeight = 0;
        HashMap<String,Double> weights = new HashMap<>();
        for (String a : fleetNames.keySet()){
            int used = data.getOrDefault(a,0);
            if (!fleetNames.get(a).canAdd(lord,used)) continue;
            double weight = fleetNames.get(a).getWeight(lord);
            totalWeight+=weight;
            weights.put(a,weight);
        }
        double random = totalWeight* Utils.rand.nextDouble();
        for (String a : weights.keySet()){
            random-=weights.get(a);
            if (random <= 0){
                gottenID = a;
                break;
            }
        }
        if (gottenID.isEmpty()){
            Utils.log.info("failed to get fleet name. will crash soon. please report to the maintainer of starlords....");
        }
        savedKey = gottenID;
        return fleetNames.get(gottenID).getName(lord);
    }
    private static String savedKey;
    public static void confirmName(){
        //todo: issue here: load is ran twice. it should be fine?
        HashMap<String,Integer> data = load();
        data.put(savedKey,data.get(savedKey)+1);
        save(data);
    }

    SV_String name;
    SV_Boolean canAdd;
    SV_Double weight;
    int maxUsages;
    @SneakyThrows
    private FleetNameController(JSONObject json){
        name = new ScriptedValueController(json.getString("name")).getNextString();
        canAdd = new ScriptedValueController(json.getString("condition")).getNextBoolean();
        weight = new ScriptedValueController(json.getString("weight")).getNextDouble();
        maxUsages = (int) new ScriptedValueController(json.getString("max uses")).getNextDouble().getValue(null);
    }
    public String getName(Lord lord){
        return DialogSet.insertDefaltData(name.getValue(lord),lord,null,null);
    }
    public boolean canAdd(Lord lord,int useages){
        if (useages >= maxUsages) return false;
        return canAdd.getValue(lord);
    }
    public double getWeight(Lord lord){
        return weight.getValue(lord);
    }
}
