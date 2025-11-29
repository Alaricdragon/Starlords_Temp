package starlords.util.memoryUtils;

import lombok.AccessLevel;
import lombok.Getter;
import starlords.util.memoryUtils.Compressed.MemCompressedHolder;
import starlords.util.memoryUtils.Compressed.MemCompressedMasterList;

@Getter
public class GenericMemory {
    //note: If I am reading this, know this acts as the -core memory- to evey object in starsector. all nice and organized.
    //if I want any type of generic memory structure, put it here. its so mush easier when organized.

    @Getter(AccessLevel.NONE)
    private final MemCompressedHolder<MemCompressedHolder<?>> COMPRESSED_MEMORY;
    private final DataHolder DATA_HOLDER;
    public GenericMemory(String objectMemoryType, Object linkedObject){
        COMPRESSED_MEMORY = new MemCompressedHolder<>(MemCompressedMasterList.getMemory().get(objectMemoryType), linkedObject);
        DATA_HOLDER = new DataHolder();
    }
    public double getCompressedDouble(String id){
        return (double) COMPRESSED_MEMORY.getItem(MemCompressedMasterList.MTYPE_KEY_DOUBLE).getItem(id);
    }
    public boolean getCompressedBoolean(String id){
        return (boolean) COMPRESSED_MEMORY.getItem(MemCompressedMasterList.MTYPE_KEY_BOOLEAN).getItem(id);
    }
    public String getCompressedString(String id){
        return (String) COMPRESSED_MEMORY.getItem(MemCompressedMasterList.MTYPE_KEY_STRING).getItem(id);
    }
    public Object getCompressedOther(String id){
        return COMPRESSED_MEMORY.getItem(MemCompressedMasterList.MTYPE_KEY_NO_CUSTOM).getItem(id);
    }
    public MemCompressedHolder<MemCompressedHolder<?>> getMemForRepairOnly(){
        return COMPRESSED_MEMORY;
    }
}
