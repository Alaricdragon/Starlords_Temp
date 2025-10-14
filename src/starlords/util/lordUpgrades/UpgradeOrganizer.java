package starlords.util.lordUpgrades;

import lombok.Getter;
import starlords.controllers.LordController;
import starlords.person.Lord;
import starlords.util.memoryUtils.Compressed.reader.LordUpgradeMemory;

import java.util.ArrayList;
import java.util.HashMap;

public class UpgradeOrganizer {
    /*ok... so:
    * I built this on a wimb.
    * it is not ready at fucking all right now.
    * like, at all.
    * so what is the issues:
    * 1: this always forces random values onto a starlord, instead of letting a starlord get its own values from its files.
    *   -note: this only applies data when a map is changed and starlords are already created. should be a none issue.
    * 2: this does not yet call itself from memory. in theory, UpgradeOrganizerFull handles this. but we will see.*/
    private String name;
    int sizeAI =0;
    int sizeCost =0;
    @Getter
    private HashMap<String,Integer> mapAI = new HashMap<>();
    @Getter
    private HashMap<String,Integer> mapCost = new HashMap<>();
    UpgradeOrganizer(String name){
        this.name = name;
        //read if this has already been created then get its memory if possable.
        //for real though, how do I do this????
    }
    public void calculateReorder(ArrayList<String> mapAI,ArrayList<String> mapCost){
        if (requireReorder(this.mapAI,mapAI)) reorderAI(mapAI);
        if (requireReorder(this.mapCost,mapCost)) reorderCost(mapCost);
    }
    private boolean requireReorder(HashMap<String,Integer> oldMap,ArrayList<String> newMap){
        for (String a : newMap){
            if (!oldMap.containsKey(a)) return true;
        }
        return false;
    }
    public void reorderAI(ArrayList<String> newMap){
        ArrayList<String> newData = new ArrayList<>();
        for (String a : newMap){
            if (!mapAI.containsKey(a)){
                newData.add(a);
                mapAI.put(a, sizeAI);
                sizeAI++;
            }
        }
        UpgradeSettingRandomizer randomizer = UpgradeController.getRandomizer(name);

        for (Lord lord: LordController.getLordsList()){
            LordUpgradeMemory lordMemory = UpgradeOrganizerFull.getInstance().getLordHashMap(lord).get(name);
            for (String a : newData){
                double weight = randomizer.getAIWeight(a);
                int id = mapAI.get(a);
                lordMemory.getAiUpgradeWeight().put(id,weight);
                //apply the new data to the starlord here.
                //I need to get the LordUpgradeMemory for this lord and preform the upgrade to there memory here.
            }
        }
    }
    public void reorderCost(ArrayList<String> newMap){
        ArrayList<String> newData = new ArrayList<>();
        for (String a : newMap){
            if (!mapCost.containsKey(a)){
                newData.add(a);
                mapCost.put(a,sizeCost);
                sizeCost++;
            }
        }
        UpgradeSettingRandomizer randomizer = UpgradeController.getRandomizer(name);

        for (Lord lord: LordController.getLordsList()){
            LordUpgradeMemory lordMemory = UpgradeOrganizerFull.getInstance().getLordHashMap(lord).get(name);
            for (String a : newData){
                double weight = randomizer.getAIWeight(a);
                int id = mapCost.get(a);
                lordMemory.getCostUpgradeWeight().put(id,weight);
                //apply the new data to the starlord here.
                //I need to get the LordUpgradeMemory for this lord and preform the upgrade to there memory here.
            }
        }
    }

    public HashMap<String,Double> getLordHashMapAI(Lord lord){
        LordUpgradeMemory lordMemory = UpgradeOrganizerFull.getInstance().getLordHashMap(lord).get(name);
        HashMap<String,Double> out = new HashMap<>();
        for (String a : mapAI.keySet()){
            out.put(a,lordMemory.getAiUpgradeWeight().get(mapAI.get(a)));
        }
        return out;
    }
    public HashMap<String,Double> getLordHashMapCost(Lord lord){
        LordUpgradeMemory lordMemory = UpgradeOrganizerFull.getInstance().getLordHashMap(lord).get(name);
        HashMap<String,Double> out = new HashMap<>();
        for (String a : mapCost.keySet()){
            out.put(a,lordMemory.getCostUpgradeWeight().get(mapCost.get(a)));
        }
        return out;
    }
}
