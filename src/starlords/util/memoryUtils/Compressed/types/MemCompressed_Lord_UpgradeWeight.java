package starlords.util.memoryUtils.Compressed.types;

import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.controllers.LordController;
import starlords.person.Lord;
import starlords.util.memoryUtils.Compressed.MemCompressedOrganizer;

import java.util.ArrayList;
import java.util.Iterator;

public class MemCompressed_Lord_UpgradeWeight extends MemCompressedOrganizer<Double,Double> {
    ArrayList<Double> max = new ArrayList<>();
    //ArrayList<Double> min = new ArrayList<>();
    @SneakyThrows
    public void addStructure(JSONObject jsonObject){
        JSONObject min = jsonObject.getJSONObject("max");
        JSONObject max = jsonObject.getJSONObject("min");
        for (Iterator it = min.keys(); it.hasNext(); ) {
            String a = it.next().toString();
            double b = min.getDouble(a);
            if (map.containsKey(a)){
                this.max.set(this.map.get(a),max.getDouble(a));
            }else{
                this.max.add(max.getDouble(a));
            }
            setItem(a,b);
        }
    }
    @Override
    public Double getDefaltData(int a, Object linkedObject) {
        return (Math.random()* (max.get(a) - list.get(a)))+list.get(a);
    }

    @Override
    public void load() {
        //todo: find the location of the json for this file here.
        addStructure();
        for (Lord lord : LordController.getLordsList()) {
            this.repair(lord.getCOMPRESSED_MEMORY(),lord);
        }
        //todo: remove the saved json here. it is no longer required.
    }

    @Override
    public void save() {

    }
}
