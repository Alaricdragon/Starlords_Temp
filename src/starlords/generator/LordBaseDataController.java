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
    /*
        todo: so... what do I need to do here?
              everything is mostly in order. But I do need to preform some upgrades to things I have already done, to compensate for the fact that 'memCompressedOrganizer' was a terrible idea.
              1 (done. just need to finish other things): I need to go into the 'dataBuilder' and make each and every one of them.... actually function under the new rules.
                 I am also going to attempt to avoid putting things inthe the GenericMemory. I built that system, and it is for things that cant, or should not be, put directly into the lord class.
                 example: the second in command data
                 -
                 please note, in regards to this: I still dont know for sure what 'form' my final bits of data will take.
             -
             2) so.... in regards to fleet composition, and other relent data:
                something I could do is make it so all ships variances, s-mods, and so forth are all scripted values.
                this would let people have increadable control over what is in a giving fleet.
                the real question would be: when is this recalculated? also, how would this effect ram useage?
                ...
                ok, so like.....
                for each bit of data:
                    for vareants: I should proboly reclaulate that each time I want to build a ship?
                    or I could reclalculate it when a thing is called? arg....
                    I dont know.....
                    its an option. to refactor that in such a way. it really is.
    */
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
