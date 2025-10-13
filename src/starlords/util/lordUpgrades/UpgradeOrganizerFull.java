package starlords.util.lordUpgrades;

import com.thoughtworks.xstream.mapper.Mapper;
import lombok.Getter;
import starlords.controllers.LordController;
import starlords.person.Lord;

import java.util.ArrayList;
import java.util.HashMap;

public class UpgradeOrganizerFull {
    /*this still has a few issues.
    * for one thing, I don't yet save the data here.
    * for another thing, I don't yet respect the starlords settings for what the values for each upgrade are.*/
    private static UpgradeOrganizerFull instance = null;
    public static UpgradeOrganizerFull getInstance(){
        if (instance == null) {

        }
        return instance;
    }
    @Getter
    private HashMap<String,Integer> map;
    @Getter
    private HashMap<String,UpgradeOrganizer> organizerMap;
    //@Getter
    //private HashMap<String,UpgradeBase> upgradeMap;
    private int size = 0;
    private static final String memoryKey = "$starlord_UpgradeOrganizerFull";
    public UpgradeOrganizerFull(){

        //need to make sure I am getting the memory of this, if at all possible.
    }
    public void save(){

    }
    public void checkMap(ArrayList<String> map){
        ArrayList<String> toAdd = new ArrayList<>();
        for (String a : map){
            if (!this.map.containsKey(a)){
                toAdd.add(a);
            }
        }
        fixMap(toAdd);

        for (String a : organizerMap.keySet()){
            organizerMap.get(a).calculateReorder(null,null);
        }
    }
    private void fixMap(ArrayList<String> map){
        //note: this leave the lord upgrade memory completely blank. I should probably fix that.
        for (String a : map){
            this.map.put(a,size);
            size++;
        }
        for (Lord lord: LordController.getLordsList()){
            LordFullUpgradeMemory fullMemory = ((LordFullUpgradeMemory)lord.getLordDataHolder().getObject(LordFullUpgradeMemory.memoryName));
            for (String a : map){
                int id = this.map.get(a);
                fullMemory.getMemory().put(id,new LordUpgradeMemory());
                //I need to set a new random set of values here.
                UpgradeOrganizer newMap = new UpgradeOrganizer(a);//for now the map is blank.
                organizerMap.put(a,newMap);
            }
        }
    }
    public HashMap<String, LordUpgradeMemory> getLordHashMap(Lord lord){
        LordFullUpgradeMemory fullMemory = ((LordFullUpgradeMemory)lord.getLordDataHolder().getObject(LordFullUpgradeMemory.memoryName));
        HashMap<String,LordUpgradeMemory> out = new HashMap<>();
        for (String a : map.keySet()){
            out.put(a,fullMemory.getMemory().get(map.get(a)));
        }
        return out;
    }
    public LordFullUpgradeMemory createNewLordMemory(Lord lord){
        //use the lord data to get all relevent data?
        LordFullUpgradeMemory memory = new LordFullUpgradeMemory();
        for (String a : map.keySet()){
            LordUpgradeMemory memory1 = new LordUpgradeMemory();
            UpgradeOrganizer map2 = organizerMap.get(a);
            UpgradeSettingRandomizer randomizer = UpgradeController.getRandomizer(a);
            for (String b : map2.getMapCost().keySet()){
                memory1.getCostUpgradeWeight().put(map2.getMapCost().get(b),randomizer.getUpgradeWeight(b));
            }
            for (String b : map2.getMapAI().keySet()){
                memory1.getAiUpgradeWeight().put(map2.getMapAI().get(b),randomizer.getAIWeight(b));

            }
            memory.getMemory().put(map.get(a),memory1);
        }
        return memory;
    }
}
