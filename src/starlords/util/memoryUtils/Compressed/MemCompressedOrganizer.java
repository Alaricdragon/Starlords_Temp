package starlords.util.memoryUtils.Compressed;

import java.util.HashMap;

public interface MemCompressedOrganizer<A> {
    /* ok... ok...
    * so what have I done:
    * 1) (done) I have made it so the memory is saved and loaded at the right times
    * 2) (done)I have made it so the memory repairs itself when lords are loaded (note: I need to set the memory to beable to set new default values.)
    *
    * 3) (done, as an interface) I have set it so the default data is decided by some type of information
    * */
    public HashMap<String,Integer> map = new HashMap<>();
    public default int getID(String key){
        return map.get(key);
    }
    public default void repair(MemCompressedHolder sur,Object linkedObject){
        while(map.size() > sur.map.size()) {
            sur.map.add(getDefaltData(sur.map.size(),linkedObject));
        }
    }
    public A getDefaltData(int i,Object linkedObject);

    public void load();
    public void save();
}
