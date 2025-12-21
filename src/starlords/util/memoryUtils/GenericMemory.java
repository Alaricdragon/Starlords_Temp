package starlords.util.memoryUtils;

import lombok.Getter;
import starlords.controllers.LordController;
import starlords.person.Lord;
import starlords.util.memoryUtils.Stats.StatsHolder;
import starlords.util.memoryUtils.genaricLists.*;

@Getter
public class GenericMemory {
    public static final String TYPE_LORD = "LORD", TYPE_PMC = "PMC", TYPE_FACTION = "FACTION";
    //note: If I am reading this, know this acts as the -core memory- to evey object in starsector. all nice and organized.
    //if I want any type of generic memory structure, put it here. its so mush easier when organized.
    //todo: I need a way to remove DATA_HOLDER data on a game save. (some data is timed. that was the ponit of that intier class)

    //@Getter(AccessLevel.NONE)
    //private final MemCompressedHolder<MemCompressedHolder<?>> COMPRESSED_MEMORY;
    private final DataHolder DATA_HOLDER;
    private final DataHolder BACKUP;//this is for things that I might change, for example: in dev mode, but might also want to restore. unlike standed DataHolder, it needs not 'forget' data.
    private final SubStaticHashmap_String Strings;
    private final SubStaticHashmap_Double Doubles;
    private final SubStaticHashmap_Boolean Booleans;
    private final SubStaticHashmap_Object Objects;
    private final StatsHolder stats;

    public GenericMemory(String TYPE, Object linkedObject){
        /*todo: so, issues with primary data storge:
            first of all: overriding scrips: this is a thing.
            I need to beable to get the overriding scripts (organized as 'Hashmap<String,String> (first value: id_of_override, second value: rar string data from json file) from a given object.)
            -
            secondly, I need to fix the stats. that will happen after I fix the StatsRandomOrganizer though.
        * */
        Booleans = SubStaticPreparationData.prepBoolean(TYPE,linkedObject);
        Strings = SubStaticPreparationData.prepString(TYPE,linkedObject);
        Doubles = SubStaticPreparationData.prepDouble(TYPE,linkedObject);
        Objects = SubStaticPreparationData.prepObject(TYPE,linkedObject);
        stats = new StatsHolder(TYPE);//I need to have the overriding memory prepared.
        //COMPRESSED_MEMORY = new MemCompressedHolder<>(MemCompressedMasterList.getMemory().get(objectMemoryType), linkedObject);
        DATA_HOLDER = new DataHolder();
        BACKUP = new DataHolder();
    }
    public void beforeSave(){
        BACKUP.repair();
        DATA_HOLDER.repair();
    }
    public void afterLoad(String TYPE, Object linkedObject){
        SubStaticPreparationData.repairBoolean(TYPE,linkedObject,Booleans);
        SubStaticPreparationData.repairString(TYPE,linkedObject,Strings);
        SubStaticPreparationData.repairDouble(TYPE,linkedObject,Doubles);
        SubStaticPreparationData.repairObject(TYPE,linkedObject,Objects);
    }

    public static void beforeSaveAll(){
        for (Lord a : LordController.getLordsList()){
            a.getMemory().beforeSave();
        }
    }
    public static void afterLoadAll(){
        for (Lord a : LordController.getLordsList()){
            a.getMemory().afterLoad(TYPE_LORD,a);
        }

    }
    /*public double getCompressed_Double(String id){
        return (double) COMPRESSED_MEMORY.getItem(MemCompressedMasterList.MTYPE_KEY_DOUBLE).getItem(id);
    }
    public boolean getCompressed_Boolean(String id){
        return (boolean) COMPRESSED_MEMORY.getItem(MemCompressedMasterList.MTYPE_KEY_BOOLEAN).getItem(id);
    }
    public String getCompressed_String(String id){
        return (String) COMPRESSED_MEMORY.getItem(MemCompressedMasterList.MTYPE_KEY_STRING).getItem(id);
    }
    public MutableStat getCompressed_MutableStat(String id){
        return (MutableStat) COMPRESSED_MEMORY.getItem(MemCompressedMasterList.MTYPE_KEY_MUTABLE_STAT).getItem(id);
    }
    public Object getCompressed_Object(String id){
        return COMPRESSED_MEMORY.getItem(MemCompressedMasterList.MTYPE_KEY_NO_CUSTOM).getItem(id);
    }
    public void setCompressed_Double(String id, double data){
        ((MemCompressedHolder<Double>) COMPRESSED_MEMORY.getItem(MemCompressedMasterList.MTYPE_KEY_DOUBLE)).putItem(id,data);
    }
    public void setCompressed_Boolean(String id, boolean data){
        ((MemCompressedHolder<Boolean>) COMPRESSED_MEMORY.getItem(MemCompressedMasterList.MTYPE_KEY_BOOLEAN)).putItem(id,data);
    }
    public void setCompressed_String(String id, String data){
        ((MemCompressedHolder<String>) COMPRESSED_MEMORY.getItem(MemCompressedMasterList.MTYPE_KEY_STRING)).putItem(id,data);
    }
    public void setCompressed_MutableStat(String id, MutableStat data){
        ((MemCompressedHolder<MutableStat>) COMPRESSED_MEMORY.getItem(MemCompressedMasterList.MTYPE_KEY_MUTABLE_STAT)).putItem(id,data);
    }
    public void setCompressed_Object(String id, Object data){
        ((MemCompressedHolder<Object>) COMPRESSED_MEMORY.getItem(MemCompressedMasterList.MTYPE_KEY_NO_CUSTOM)).putItem(id,data);
    }
    public MemCompressedHolder<MemCompressedHolder<?>> getMemForRepairOnly(){
        return COMPRESSED_MEMORY;
    }


    public double getBackupOrCurrent_Double(String id){
        if (BACKUP.hasDouble(id)) return BACKUP.getDouble(id);
        return getCompressed_Double(id);
    }
    public boolean getBackupOrCurrent_Boolean(String id){
        if (BACKUP.hasBoolean(id)) return BACKUP.getBoolean(id);
        return getCompressed_Boolean(id);
    }
    public String getBackupOrCurrent_String(String id){
        if (BACKUP.hasString(id)) return BACKUP.getString(id);
        return getCompressed_String(id);
    }
    public MutableStat getBackupOrCurrent_MutableStat(String id){
        if (BACKUP.hasMutableStat(id)) return BACKUP.getMutableStat(id);
        return getCompressed_MutableStat(id);
    }
    public Object getBackupOrCurrent_Object(String id){
        if (BACKUP.hasObject(id)) return BACKUP.getObject(id);
        return getCompressed_Object(id);
    }


    public void setBackupOrCurrent_Double(String id,double data){
        if (BACKUP.hasDouble(id)) BACKUP.setDouble(id,data);
        setCompressed_Double(id,data);
    }
    public void setBackupOrCurrent_Boolean(String id,boolean data){
        if (BACKUP.hasBoolean(id)) BACKUP.setBoolean(id,data);
        setCompressed_Boolean(id,data);
    }
    public void setBackupOrCurrent_String(String id,String data){
        if (BACKUP.hasString(id)) BACKUP.setString(id,data);
        setCompressed_String(id,data);
    }
    public void setBackupOrCurrent_String(String id,MutableStat data){
        if (BACKUP.hasMutableStat(id)) BACKUP.setMutableStat(id,data);
        setCompressed_MutableStat(id,data);
    }
    public void setBackupOrCurrent_Object(String id, Object data){
        if (BACKUP.hasObject(id)) BACKUP.setObject(id,data);
        setCompressed_Object(id,data);
    }*/
}
