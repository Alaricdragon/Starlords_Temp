package starlords.generator;

import com.fs.starfarer.api.Global;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class LordBaseDataController {
    /*todo: issue with data storge:
    *       do to the nature of data storge in this capacity, I */
    private static HashMap<String, LordBaseDataBuilder> map = new HashMap<>();
    private static HashMap<String,Double> priorityMap = new HashMap<>();
    @SneakyThrows
    public static void init(){
        String path = "data/lords/lordAttributes.csv";
        JSONArray jsons = Global.getSettings().loadCSV(path, true);
        ArrayList<Double> order = new ArrayList<>();
        ArrayList<LordBaseDataBuilder> data = new ArrayList<>();
        ArrayList<String> ids = new ArrayList<>();
        for (int a = 0; a < jsons.length(); a++){
            JSONObject json = jsons.getJSONObject(a);
            String id = json.getString("id");
            Double orderT = json.getDouble("order");
            String path2 = json.getString("script");
            data.add((LordBaseDataBuilder) Global.getSettings().getInstanceOfScript(path2));
            ids.add(id);
            order.add(orderT);
        }
        while(!data.isEmpty()){
            //organize all data into the correct order.
            int target = 0;
            double curOrder = order.get(0);
            for (int a = 1; a < data.size(); a++){
                if (order.get(a) < curOrder){
                    target = a;
                    curOrder = order.get(a);
                }
            }
            map.put(ids.get(target),data.get(target));
            priorityMap.put(ids.get(target),order.get(target));
            data.remove(target);
        }
        prepareDataOrganizers();
    }
    private static void prepareDataOrganizers(){
        //this is ran once, so prepare the memCompressedOrganizer.
        //I really hope this does not break if the order is changed? I hope???
        //It should not do that right!?!?! thats something I fixed right!??
        for (LordBaseDataBuilder a :getFormaters()){
            a.prepareStorgeInMemCompressedOrganizer();
        }
    }
    public static LordBaseDataBuilder[] getFormaters(){
        LordBaseDataBuilder[] output = new LordBaseDataBuilder[0];
        output = map.values().toArray(output);
        return output;
    }
}
