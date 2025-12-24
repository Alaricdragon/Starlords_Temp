package starlords.util.memoryUtils;

import lombok.Getter;
import org.json.JSONObject;
import starlords.controllers.LordController;
import starlords.person.Lord;
import starlords.util.fleetCompasition.ShipCompositionData;
import starlords.util.memoryUtils.Stats.StatsHolder;
import starlords.util.memoryUtils.Stats.StatsRandomOrganizer;
import starlords.util.memoryUtils.genaricLists.*;
import starlords.util.overriders.ScriptOverrides_Memory;

@Getter
public class GenericMemory {
    public static final String TYPE_LORD = "LORD", TYPE_PMC = "PMC", TYPE_FACTION = "FACTION", TYPE_FLEET = "FLEET", TYPE_SHIP = "SHIP";
    //note: If I am reading this, know this acts as the -core memory- to evey object in starsector. all nice and organized.
    //if I want any type of generic memory structure, put it here. its so mush easier when organized.
    //todo: I need to make it so I have a specal 'generative data' memory that is -just for storing random bits of data for generation-.
    //      that, OR I could just check the memory one tick after it is created. it should be fine right?
    //
    //todo: optimization:
    //      there is a possibility that it mgiht be better to store this data in the main starsector memory. just have it accessed from here. something to keep in mind.
    //@Getter(AccessLevel.NONE)
    //private final MemCompressedHolder<MemCompressedHolder<?>> COMPRESSED_MEMORY;
    private final DataHolder DATA_HOLDER;
    private final DataHolder BACKUP;//this is for things that I might change, for example: in dev mode, but might also want to restore. unlike standed DataHolder, it needs not 'forget' data.
    private final SubStaticHashmap_String Strings;
    private final SubStaticHashmap_Double Doubles;
    private final SubStaticHashmap_Boolean Booleans;
    private final SubStaticHashmap_Object Objects;
    private final StatsHolder stats;

    public GenericMemory(String TYPE, JSONObject json, Object linkedObject){
        ScriptOverrides_Memory tempMemory = new ScriptOverrides_Memory(json);
        Booleans = SubStaticPreparationData.prepBoolean(TYPE,tempMemory.getSubStaticItems(),linkedObject);
        Strings = SubStaticPreparationData.prepString(TYPE,tempMemory.getSubStaticItems(),linkedObject);
        Doubles = SubStaticPreparationData.prepDouble(TYPE,tempMemory.getSubStaticItems(),linkedObject);
        Objects = SubStaticPreparationData.prepObject(TYPE,tempMemory.getSubStaticItems(),linkedObject);
        stats = StatsRandomOrganizer.prepStatsHolder(TYPE,tempMemory.getStatModOverrides(),linkedObject);//I need to have the overriding memory prepared.
        tempMemory.resetUnwantedDataAfterMemonyHasEaten();//num num.

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

        StatsRandomOrganizer.repair(TYPE,stats,linkedObject);
    }

    public static void beforeSaveAll(){
        for (Lord a : LordController.getLordsList()){
            saveLord(a);
        }
    }
    public static void afterLoadAll(){
        for (Lord a : LordController.getLordsList()){
            loadLord(a);
        }

    }
    private static void saveLord(Lord lord){
        lord.getMemory().beforeSave();
        lord.getFleetCompositionData().getCombat().getMemory().beforeSave();
        lord.getFleetCompositionData().getCargo().getMemory().beforeSave();
        lord.getFleetCompositionData().getPersonal().getMemory().beforeSave();
        lord.getFleetCompositionData().getFuel().getMemory().beforeSave();
        lord.getFleetCompositionData().getTug().getMemory().beforeSave();
        for (ShipCompositionData a : lord.getFleetCompositionData().getCombat().getData().values()) a.getMemory().beforeSave();
        for (ShipCompositionData a : lord.getFleetCompositionData().getCargo().getData().values()) a.getMemory().beforeSave();
        for (ShipCompositionData a : lord.getFleetCompositionData().getPersonal().getData().values()) a.getMemory().beforeSave();
        for (ShipCompositionData a : lord.getFleetCompositionData().getFuel().getData().values()) a.getMemory().beforeSave();
        for (ShipCompositionData a : lord.getFleetCompositionData().getTug().getData().values()) a.getMemory().beforeSave();

    }
    private static void loadLord(Lord lord){
        lord.getMemory().afterLoad(TYPE_LORD,lord);
        lord.getFleetCompositionData().getCombat().getMemory().afterLoad(TYPE_FLEET,lord.getFleetCompositionData().getCombat());
        lord.getFleetCompositionData().getCargo().getMemory().afterLoad(TYPE_FLEET,lord.getFleetCompositionData().getCargo());
        lord.getFleetCompositionData().getPersonal().getMemory().afterLoad(TYPE_FLEET,lord.getFleetCompositionData().getPersonal());
        lord.getFleetCompositionData().getFuel().getMemory().afterLoad(TYPE_FLEET,lord.getFleetCompositionData().getFuel());
        lord.getFleetCompositionData().getTug().getMemory().afterLoad(TYPE_FLEET,lord.getFleetCompositionData().getTug());
        for (ShipCompositionData a : lord.getFleetCompositionData().getCombat().getData().values()) a.getMemory().afterLoad(TYPE_SHIP,a);
        for (ShipCompositionData a : lord.getFleetCompositionData().getCargo().getData().values()) a.getMemory().afterLoad(TYPE_SHIP,a);
        for (ShipCompositionData a : lord.getFleetCompositionData().getPersonal().getData().values()) a.getMemory().afterLoad(TYPE_SHIP,a);
        for (ShipCompositionData a : lord.getFleetCompositionData().getFuel().getData().values()) a.getMemory().afterLoad(TYPE_SHIP,a);
        for (ShipCompositionData a : lord.getFleetCompositionData().getTug().getData().values()) a.getMemory().afterLoad(TYPE_SHIP,a);
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
