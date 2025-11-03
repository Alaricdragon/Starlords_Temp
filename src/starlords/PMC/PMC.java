package starlords.PMC;

import com.fs.starfarer.api.Global;
import lombok.Getter;
import starlords.person.Lord;
import starlords.util.memoryUtils.Compressed.MemCompressedHolder;
import starlords.util.memoryUtils.DataHolder;

import java.util.ArrayList;

import static starlords.util.Constants.*;

public class PMC {
    @Getter
    private String id;
    private ArrayList<Lord> lords = new ArrayList<>();
    private ArrayList<PMC> underPMCs = new ArrayList<>();
    private Lord commander;//I can get the PMC the 'commander' is in form the commander itself.
    public PMC(String id){
        this.id = id;
    }

    @Getter
    private MemCompressedHolder<MemCompressedHolder<?>> COMPRESSED_MEMORY;// = (MemCompressedHolder<MemCompressedHolder<?>>) MemCompressedMasterList.getMemory().get(COMPRESSED_ORGANIZER_LORD_KEY).getHolderStructure(this);
    public void loadConnectedMemory(){
        String key = PMC_COMPRESSED_MEMORY_KEY+id;
        MemCompressedHolder<MemCompressedHolder<?>> temp;
        if (Global.getSector().getMemory().contains(key)){
            temp = (MemCompressedHolder<MemCompressedHolder<?>>) Global.getSector().getMemory().get(key);
        }else{
            temp = COMPRESSED_MEMORY;
        }
        COMPRESSED_MEMORY = temp;
    }
    public void saveCompressedMemory(){
        String key = PMC_COMPRESSED_MEMORY_KEY+id;
        MemCompressedHolder<MemCompressedHolder<?>> data = COMPRESSED_MEMORY;
        Global.getSector().getMemory().set(key,data);
    }
    private DataHolder DATA_HOLDER;
    public DataHolder getDataHolder(){
        DataHolder data_holder = DATA_HOLDER;
        if (DATA_HOLDER != null) return data_holder;
        String key = PMC_ADDITIONAL_MEMORY_KEY+id;
        if (Global.getSector().getMemory().contains(key)){
            data_holder = (DataHolder) Global.getSector().getMemory().get(key);
        }else{
            data_holder = new DataHolder();
        }
        DATA_HOLDER = data_holder;
        return data_holder;
    }
    public void saveDataHolder(){
        String key = PMC_ADDITIONAL_MEMORY_KEY+id;
        DataHolder data_holder = DATA_HOLDER;
        Global.getSector().getMemory().set(key,data_holder);
    }
}
