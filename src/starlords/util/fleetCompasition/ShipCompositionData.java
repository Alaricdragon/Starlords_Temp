package starlords.util.fleetCompasition;

import lombok.Getter;
import org.json.JSONObject;
import starlords.util.memoryUtils.Compressed.MemCompressedMasterList;
import starlords.util.memoryUtils.GenericMemory;

import java.util.HashMap;

public class ShipCompositionData {
    //public String id;//most the time, same as varient. but it can be overriden.
    public String variant;
    protected HashMap<String,Double> max = new HashMap<>();
    protected HashMap<String,Double> min = new HashMap<>();

    @Getter
    private final GenericMemory Memory;
    //todo: put data here

    public ShipCompositionData(String variant){
        //this is for the generator to handle. I will have to think about this in time...
        Memory = new GenericMemory(MemCompressedMasterList.KEY_SHIP,this);
        this.variant = variant;
    }
    public double getPriorityToBuild(FleetCompositionData data){
        //todo: this gets the priority to build this ship.
        return 0;
    }
    public double getPriorityOverriderByMinMax(FleetCompositionData data,double priority,int num){
        //int num = data.getNumberOfShips(getID());
        for (String a : max.keySet()){
            if (num >= data.getNumberOfShips(a)*max.get(a)) return -1000;
        }
        for (String a : min.keySet()){
            if (num < data.getNumberOfShips(a)*min.get(a)) return 1000;
        }
        return priority;
    }
}
