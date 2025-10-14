package starlords.util.memoryUtils.Compressed;

import com.fs.starfarer.api.Global;
import lombok.Getter;

import java.util.HashMap;

public class MemCompressedMasterList {
    @Getter
    private static HashMap<String,MemCompressedOrganizer<?>> memory = new HashMap<>();


    private static final String memKey = "$STARLORDS_MEMORY_COMPRESSED_MEMORY_MASTER_LIST";
    public static void save(){
        for (String a : memory.keySet()){
            memory.get(a).save();
        }

        String key = memKey;
        HashMap<String,MemCompressedOrganizer<?>> temp;
        if (Global.getSector().getMemory().contains(key)){
            temp = (HashMap<String,MemCompressedOrganizer<?>>) Global.getSector().getMemory().get(key);
        }else{
            temp = new HashMap<String,MemCompressedOrganizer<?>>();
        }
        memory = temp;

    }
    public static void load(){
        String key = memKey;
        HashMap<String,MemCompressedOrganizer<?>> data = memory;
        Global.getSector().getMemory().set(key,data);

        for (String a : memory.keySet()){
            memory.get(a).load();
        }
        checkAllMemoryOrganizers();

    }

    private static void checkAllMemoryOrganizers(){
        //note: I might be doing this wrong (by addnig this like this, instead of like a hashmap).
        //I will check next time.
        LordMaster = new MemCompressedOrganizer<MemCompressedOrganizer<?>>() {
            @Override
            public MemCompressedOrganizer<?> getDefaltData(int i, Object linkedObject) {
                return null;
            }

            @Override
            public void load() {

            }

            @Override
            public void save() {

            }
        };
        //todo: this is the place were all memory organizers are checked for stability.
        //basicly, it is were I would load all data.
    }
    @Getter
    private static MemCompressedOrganizer<MemCompressedOrganizer<?>> LordMaster = new MemCompressedOrganizer<MemCompressedOrganizer<?>>() {
        @Override
        public MemCompressedOrganizer<?> getDefaltData(int i, Object linkedObject) {
            return null;
        }

        @Override
        public void load() {

        }

        @Override
        public void save() {

        }
    };
}
