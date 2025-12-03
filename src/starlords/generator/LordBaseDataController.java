package starlords.generator;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.util.Pair;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;
import starlords.person.Lord;
import starlords.util.CsvFilerReader;

import java.util.ArrayList;
import java.util.HashMap;

public class LordBaseDataController {
    /* this handles forming the basic data structures that all lords follow. it also handles lord generation. please dont forget this again for fucks sakes.*/
    private static HashMap<String, LordBaseDataBuilder> map = new HashMap<>();
    private static HashMap<String,Double> priorityMap = new HashMap<>();
    @SneakyThrows
    public static void init(){
        String path = "data/lords/lordGenerator.csv";
        JSONArray jsons = Global.getSettings().loadCSV(path, true);
        HashMap<String,JSONObject> list = CsvFilerReader.computeFile(jsons);
        ArrayList<Double> order = new ArrayList<>();
        ArrayList<LordBaseDataBuilder> data = new ArrayList<>();
        ArrayList<String> ids = new ArrayList<>();
        for (String a : list.keySet()){
            JSONObject json = list.get(a);
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
        for (Pair<String,LordBaseDataBuilder> a : getFormaters()){
            a.two.prepareStorgeInMemCompressedOrganizer();
        }
    }
    public static ArrayList<Pair<String,LordBaseDataBuilder>> getFormaters(){
        ArrayList<Pair<String,LordBaseDataBuilder>> output = new ArrayList<>();
        for (String a : map.keySet()){
            Pair<String,LordBaseDataBuilder> b = new Pair<>();
            b.one = a;
            b.two = map.get(a);
            output.add(b);
        }
        return output;
    }
    public static final String generatorScripKey = "lordGenerator";
    public static void save(Lord lord){
        for (Pair<String,LordBaseDataBuilder> a : getFormaters()){
            LordBaseDataBuilder b = a.two;
            String c = lord.getScrips().getScript(generatorScripKey,a.one);
            if (c != null){
                b = (LordBaseDataBuilder) Global.getSettings().getInstanceOfScript(c);
            }
            b.saveLord(lord);
        }
    }
    public static void load(Lord lord){
        for (Pair<String,LordBaseDataBuilder> a : getFormaters()){
            LordBaseDataBuilder b = a.two;
            String c = lord.getScrips().getScript(generatorScripKey,a.one);
            if (c != null){
                b = (LordBaseDataBuilder) Global.getSettings().getInstanceOfScript(c);
            }
            b.loadLord(lord);
        }
    }

    @Deprecated
    public static void repairIfRequired(Lord lord,JSONObject json){
        //this is handled inside the lord class.
    }
}
