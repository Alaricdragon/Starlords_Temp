package starlords.util.memoryUtils.genaricLists;

import starlords.util.ScriptedValues.*;
import starlords.util.memoryUtils.GenericMemory;

import java.util.HashMap;

public class SubStaticPreparationData {
    //NOTE: type is stored in the GenericMemory.
    //public static final String LORD_KEY = "LORD", FACTION_KEY = "FACTION", PMC_KEY = "PMC";
    private static HashMap<String,SubStaticPreparationData> masterList = new HashMap<>();


    public static void setData(String KEY, String id, SV_Base data){
        if (data instanceof SV_Boolean_Code){
            setData(KEY,id,(SV_Boolean_Code)data);
            return;
        }
        if (data instanceof SV_Double_Code){
            setData(KEY,id,(SV_Double_Code)data);
            return;
        }
        if (data instanceof SV_String_Code){
            setData(KEY,id,(SV_String_Code)data);
            return;
        }
        if (data instanceof SV_Object_Code){
            setData(KEY,id,(SV_Object_Code)data);
            return;
        }
    }
    private static void setData(String KEY, String id, SV_String_Code data){
        masterList.get(KEY).strings.put(id,data);
    }
    private static void setData(String KEY, String id, SV_Double_Code data){
        masterList.get(KEY).doubles.put(id,data);
    }
    private static void setData(String KEY, String id, SV_Boolean_Code data){
        masterList.get(KEY).booleans.put(id,data);
    }
    private static void setData(String KEY, String id, SV_Object_Code data){
        masterList.get(KEY).objects.put(id,data);
    }
    public static SubStaticHashmap_Boolean prepBoolean(String KEY,HashMap<String,String> scripts,Object linkedObject){
        SubStaticPreparationData data = masterList.get(KEY);
        SubStaticHashmap_Boolean a = new SubStaticHashmap_Boolean(data.booleans.size());
        int c = 0;
        for (String b : data.booleans.keySet()){
            if (scripts.containsKey(b)) a.data[c] = new ScriptedValueController(scripts.get(b)).getNextBoolean().getValue(linkedObject);
            else a.data[c] = data.booleans.get(b).getValue(linkedObject);
            a.keys[c] = b;
            c++;
        }
        return a;
    }
    public static SubStaticHashmap_Double prepDouble(String KEY,HashMap<String,String> scripts,Object linkedObject){
        SubStaticPreparationData data = masterList.get(KEY);
        SubStaticHashmap_Double a = new SubStaticHashmap_Double(data.doubles.size());
        int c = 0;
        for (String b : data.doubles.keySet()){
            if (scripts.containsKey(b)) a.data[c] = new ScriptedValueController(scripts.get(b)).getNextDouble().getValue(linkedObject);
            else a.data[c] = data.doubles.get(b).getValue(linkedObject);
            a.keys[c] = b;
            c++;
        }
        return a;
    }
    public static SubStaticHashmap_String prepString(String KEY,HashMap<String,String> scripts,Object linkedObject){
        SubStaticPreparationData data = masterList.get(KEY);
        SubStaticHashmap_String a = new SubStaticHashmap_String(data.strings.size());
        int c = 0;
        for (String b : data.strings.keySet()){
            if (scripts.containsKey(b)) a.data[c] = new ScriptedValueController(scripts.get(b)).getNextString().getValue(linkedObject);
            else a.data[c] = data.strings.get(b).getValue(linkedObject);
            a.keys[c] = b;
            c++;
        }
        return a;
    }
    public static SubStaticHashmap_Object prepObject(String KEY,HashMap<String,String> scripts,Object linkedObject){
        SubStaticPreparationData data = masterList.get(KEY);
        SubStaticHashmap_Object a = new SubStaticHashmap_Object(data.objects.size());
        int c = 0;
        for (String b : data.objects.keySet()){
            if (scripts.containsKey(b)) a.data[c] = new ScriptedValueController(scripts.get(b)).getNextObject().getValue(linkedObject);
            else a.data[c] = data.objects.get(b).getValue(linkedObject);
            a.keys[c] = b;
            c++;
        }
        return a;
    }

    public static void repairBoolean(String KEY,Object linkedObject,SubStaticHashmap_Boolean hashMap){
        SubStaticPreparationData data = masterList.get(KEY);
        for (String a : data.booleans.keySet()){
            if (!hashMap.hasItem(a)){
                hashMap.setItem(a,data.booleans.get(a).getValue(linkedObject));
            }
        }
    }
    public static void repairDouble(String KEY,Object linkedObject,SubStaticHashmap_Double hashMap){
        SubStaticPreparationData data = masterList.get(KEY);
        for (String a : data.doubles.keySet()){
            if (!hashMap.hasItem(a)){
                hashMap.setItem(a,data.doubles.get(a).getValue(linkedObject));
            }
        }
    }
    public static void repairString(String KEY,Object linkedObject,SubStaticHashmap_String hashMap){
        SubStaticPreparationData data = masterList.get(KEY);
        for (String a : data.strings.keySet()){
            if (!hashMap.hasItem(a)){
                hashMap.setItem(a,data.strings.get(a).getValue(linkedObject));
            }
        }
    }
    public static void repairObject(String KEY,Object linkedObject,SubStaticHashmap_Object hashMap){
        SubStaticPreparationData data = masterList.get(KEY);
        for (String a : data.objects.keySet()){
            if (!hashMap.hasItem(a)){
                hashMap.setItem(a,data.objects.get(a).getValue(linkedObject));
            }
        }
    }

    //run on game load, very first.
    public static void insureIntergerty(){
        if (!masterList.containsKey(GenericMemory.TYPE_FACTION)) new SubStaticPreparationData(GenericMemory.TYPE_FACTION);
        if (!masterList.containsKey(GenericMemory.TYPE_LORD)) new SubStaticPreparationData(GenericMemory.TYPE_LORD);
        if (!masterList.containsKey(GenericMemory.TYPE_PMC)) new SubStaticPreparationData(GenericMemory.TYPE_PMC);
        if (!masterList.containsKey(GenericMemory.TYPE_FLEET)) new SubStaticPreparationData(GenericMemory.TYPE_FLEET);
        if (!masterList.containsKey(GenericMemory.TYPE_SHIP)) new SubStaticPreparationData(GenericMemory.TYPE_SHIP);
    }
    public HashMap<String, SV_Boolean_Code> booleans;
    public HashMap<String, SV_Double_Code> doubles;
    public HashMap<String, SV_String_Code> strings;
    public HashMap<String, SV_Object_Code> objects;
    public SubStaticPreparationData(String key){
        masterList.put(key,this);
    }


}
