package starlords.util.memoryUtils.Compressed;

import java.util.ArrayList;

public class MemCompressedHolder<A> {
    private MemCompressedOrganizer master;
    protected ArrayList<A> map = new ArrayList<>();
    public MemCompressedHolder(MemCompressedOrganizer master,Object LinkedObject){
        this.master = master;
        master.repair(this,LinkedObject);
    }
    public void repair(){
        if (map.size() < master.map.size()) master.repair(this,null);
    }
    public void repair(Object linkedObject){
        if (map.size() < master.map.size()) master.repair(this,linkedObject);
    }

    public A getItem(String id){
        return map.get(master.getID(id));
    }
    public void putItem(String id,A item){
        map.set(master.getID(id),item);
    }
}
