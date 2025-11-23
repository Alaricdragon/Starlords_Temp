package starlords.util.fleetCompasition;

import org.json.JSONObject;

import java.util.HashMap;

public class ShipCompositionData {
    /*
    todo: so... once more I am here.
          at the stage were making it so lord compressed organizer would be really nice if it was a single class for fuck sakes.

    */
    public String variant;
    protected HashMap<String,Double> max = new HashMap<>();
    protected HashMap<String,Double> min = new HashMap<>();
    //todo: put data here

    public ShipCompositionData(String variant){
        //this is for the generator to handle. I will have to think about this in time...
    }
    public ShipCompositionData(JSONObject json){

    }
    public String getID(){
        //todo: this gets the ID of a given ship.
        return null;
    }
    public double getPriorityToBuild(FleetCompositionData data){
        //todo: this gets the priority to build this ship.
        return 0;
    }
    public double getPriorityOverriderByMinMax(FleetCompositionData data,double priority){
        int num = data.getNumberOfShips(getID());
        for (String a : max.keySet()){
            if (num >= data.getNumberOfShips(a)*max.get(a)) return -1000;
        }
        for (String a : min.keySet()){
            if (num < data.getNumberOfShips(a)*min.get(a)) return 1000;
        }
        return priority;
    }
}
