package starlords.util.memoryUtils.Compressed;

import java.util.ArrayList;
import java.util.HashMap;

public class MemCompressedOrganizer<A,B> {
    /* ok... ok...
    * so what have I done:
    * 1) (done) I have made it so the memory is saved and loaded at the right times
    * 2) (done)I have made it so the memory repairs itself when lords are loaded (note: I need to set the memory to beable to set new default values.)
    *
    * 3) (done, as an interface) I have set it so the default data is decided by some type of information
    *
    * ISSUE:
    * so, this holds a list that links to another list (by strings -> intigers)
    * issue: the list this links to DOES NOT EXSIST.
    * I need to add the list this links to in some way.
    * ...
    * so um
    * I need to do that somehow....
    * ok... so looking at it closer, it looks like I cannot use this as a interface. I need to use this as a class.
    *
    * so, looking at this: the 'list' is different from the 'get default item'.
    * this is because the list holds the internal data (example: a MemCompressedOrganizer) and the defalt data can be the linked item instead of what is in the list. (example" MemCompressedHolder)
    *
    * ok... so first of all:
    * fuck you.
    *
    *
    *
    * ok, so what have I dont so far:
    * 1) I have made it so each organizer can output its 'defalt' data onto a new holder (for Lord that is a blank holder of the same size as its organizer.)
    * 3) make it so lords organizer is stored in the master list.
    * 4) make it so lords data repairs itself on startup (as the final step, all organizers should repair themselves.)
    *   -note: this will only work if all possible organizers are already fully initialized by this step
    *
    *
    *
    *
    * what do I still need to do:
    * 2) make it so lords data links to the relevant upgrade data (structure should be prepared hopefully)
    *   -status: structure partly prepared.
    *   -requirements:
    *       1) make it so I store the 'odds' jsonObject. (AKA the cost and weight of upgrade conditions)
    *       2) make it so I add said data on the 'load' stage.
    *           -(done hopefully) note: this needs to keep in mind changing settings. so I need to replace old data at this stage.
    *       3) make it so I remove said data afterwords (the basic version of said data.)
    *
    * issues:
    *   although I can insure the structure of the masterlist is prepared early,
    * */
    protected HashMap<String,Integer> map = new HashMap<>();
    protected ArrayList<B> list = new ArrayList<>();

    public boolean hasItem(String id){
        for (String a : map.keySet()){
            if (a.equals(id)) return true;
        }
        return false;
    }
    public void setItem(String id,B thing){
        if (map.containsKey(id)){
            list.set(map.get(id),thing);
            return;
        }
        list.add(thing);
        map.put(id,map.size());
    }
    public B getItem(String id){
        if (!map.containsKey(id)) return null;
        return list.get(map.get(id));
    }
    public int getID(String key){
        return map.get(key);
    }
    public void repair(MemCompressedHolder sur,Object linkedObject){
        while(map.size() > sur.map.size()) {
            sur.map.add(getDefaltData(sur.map.size(),linkedObject));
        }
    }
    public A getDefaltData(int a,Object linkedObject){return null;}
    public MemCompressedHolder<A> getHolderStructure(Object linkedObject){
        MemCompressedHolder<A> a = new MemCompressedHolder<>(this,linkedObject);
        a.map = new ArrayList<>(this.map.size());
        return a;
    }
    public void repairStructuresOfMasterCompressedOrganizer(MemCompressedHolder<?> memory,Object linkedObject){
        for (String a : MemCompressedMasterList.standerdDataAsArray){
            //MemCompressedHolder<?> b = (MemCompressedHolder<?>)memory.getItem(a);
            MemCompressedOrganizer<?,?> c = (MemCompressedOrganizer<?,?>)this.getItem(a);
            c.repair((MemCompressedHolder) memory.getItem(a),linkedObject);
            //MemCompressedOrganizer<?,?> c = this.map.get(a);
            //b.repair(linkedObject);
        }
        //MemCompressedMasterList.standerdDataAsArray
            /*for (String a : map.keySet()) {
                //note that it is repaired, not loaded. this could be an issue. alturation...?
                MemCompressedOrganizer<?, ?> b  = list.get(map.get(a));
                b.repair(lord.getCOMPRESSED_MEMORY().getItem(a),lord);
            }*/
        //for (String a : map.keySet()){
        //    MemCompressedOrganizer<?, ?> b  = list.get(map.get(a));
        //    b.repair(lord.getCOMPRESSED_MEMORY().getItem(),lord);
        //}
    }
    public void load(){}
    public void save(){}
}
