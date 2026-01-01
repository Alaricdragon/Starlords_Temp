package starlords.util.fleetCompasition;

import com.fs.starfarer.api.characters.FullName;
import lombok.Getter;
import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.person.Lord;
import starlords.util.ScriptedValues.SV_Double;
import starlords.util.ScriptedValues.SV_String;
import starlords.util.ScriptedValues.ScriptedValueController;
import starlords.util.memoryUtils.Compressed_outdated.MemCompressedMasterList;
import starlords.util.memoryUtils.GenericMemory;

import java.util.HashMap;
import java.util.Iterator;

public class ShipCompositionData {
    //public String id;//most the time, same as varient. but it can be overriden.
    public String variant;
    protected HashMap<String,Double> max = new HashMap<>();
    protected HashMap<String,Double> min = new HashMap<>();

    @Getter
    private final GenericMemory Memory;
    private double weight=1;
    private double priority=0;

    private SV_String portraitDefault;
    private SV_String nameDefault;
    private SV_Double genderDefault;

    //todo: put data here
    public ShipCompositionData(){
        Memory = new GenericMemory(MemCompressedMasterList.KEY_SHIP,null,this);
    }
    @Deprecated
    public void init(FleetCompositionData data, String variant,double weight){
        //this is for the old lord json file. nothing else
        this.variant = variant;
        this.weight = weight;
        data.addShip(variant,this);
    }
    public void init(Lord lord,String fleet){
        //this is for the scripted structure. You must place certen values within
        //data.addShip(id,this);
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
    @SneakyThrows
    public static void addShipToFleetCompFromJson(FleetCompositionData data, JSONObject json, Lord lord){
        String id;
        ShipCompositionData ship = new ShipCompositionData();
        ship.variant = new ScriptedValueController(json.getString("variant")).getNextString().getValue(lord);

        if (json.has("idOverride")){
            id = new ScriptedValueController(json.getString("idOverride")).getNextString().getValue(lord);
        }else{
            id = ship.variant;
        }
        if (json.has("weight")) ship.weight = new ScriptedValueController(json.getString("weight")).getNextDouble().getValue(lord);
        if (json.has("priority")) ship.priority = new ScriptedValueController(json.getString("priority")).getNextDouble().getValue(lord);
        if (json.has("min")){
            //I nearly lost my mind trying to think of all the possabilitys with this being a script so....
            //its not happing. fuck you.
            JSONObject array = json.getJSONObject("min");
            for (Iterator it = array.keys(); it.hasNext(); ) {
                String key = (String) it.next();
                String shipt = new ScriptedValueController(key).getNextString().getValue(lord);
                double wieghtt = new ScriptedValueController(array.getString(key)).getNextDouble().getValue(lord);
                ship.min.put(shipt, wieghtt);
            }
        }
        if (json.has("max")){
            JSONObject array = json.getJSONObject("max");
            for (Iterator it = array.keys(); it.hasNext(); ) {
                String key = (String) it.next();
                String shipt = new ScriptedValueController(key).getNextString().getValue(lord);
                double wieghtt = new ScriptedValueController(array.getString(key)).getNextDouble().getValue(lord);
                ship.max.put(shipt, wieghtt);
            }
        }
        ship.Memory.getDATA_HOLDER().setObject("json",json,1);
        data.addShip(id,ship);
    }
    public static void addShipToFleetCompFromGenerator(FleetCompositionData data, String variantID, double weight){
        ShipCompositionData ship = new ShipCompositionData();
        ship.variant = variantID;
        ship.weight = weight;
        data.addShip(variantID,ship);
    }

    public String getOfficerPortrait(FullFleetCompositionData fullData, FleetCompositionData data,Object linkedObject){
        //this will return null if the officer is not getting its own portrait. so if null, do whatever is the defalt data.
        if (portraitDefault != null) return portraitDefault.getValue(linkedObject);
        if (data != null) return data.getPortraitDefault().getValue(linkedObject);
        if (fullData.getPortraitDefault() != null) return fullData.getPortraitDefault().getValue(linkedObject);
        return null;
    }
    public String getOfficerName(FullFleetCompositionData fullData, FleetCompositionData data,Object linkedObject){
        //this will return null if the officer is not getting its own portrait. so if null, do whatever is the defalt data.
        if (nameDefault != null) return nameDefault.getValue(linkedObject);
        if (data != null) return data.getNameDefault().getValue(linkedObject);
        if (fullData.getNameDefault() != null) return fullData.getNameDefault().getValue(linkedObject);
        return null;
    }
    public FullName.Gender getGender(FullFleetCompositionData fullData, FleetCompositionData data,Object linkedObject){
        int value = genderValue(fullData, data, linkedObject);
        if (value == -1) return null;
        return switch (value){
            case 0 -> FullName.Gender.ANY;
            case 1 -> FullName.Gender.MALE;
            case 2 -> FullName.Gender.FEMALE;
            default -> null;
        };
    }
    private int genderValue(FullFleetCompositionData fullData, FleetCompositionData data,Object linkedObject){
        if (genderDefault != null) return (int) genderDefault.getValue(linkedObject);
        if (data != null) return (int) data.getGenderDefault().getValue(linkedObject);
        if (fullData.getGenderDefault() != null) return (int) fullData.getGenderDefault().getValue(linkedObject);
        return -1;
    }
}
