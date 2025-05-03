package starlords.util.memoryUtils;

import com.fs.starfarer.api.Global;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

public class DataHolder {
    protected HashMap<String,Long> sTimestamp = new HashMap<>();
    protected HashMap<String,Long> bTimestamp = new HashMap<>();
    protected HashMap<String,Long> iTimestamp = new HashMap<>();
    protected HashMap<String,Integer> sExpire = new HashMap<>();
    protected HashMap<String,Integer> bExpire = new HashMap<>();
    protected HashMap<String,Integer> iExpire = new HashMap<>();

    protected HashMap<String,String> strings = new HashMap<>();
    protected HashMap<String,Boolean> booleans = new HashMap<>();
    protected HashMap<String,Integer> integers = new HashMap<>();
    private void setStringInternal(String key, String data){
        strings.put(key,data);
    }
    private void setBooleanInternal(String key, boolean data){
        booleans.put(key,data);
    }
    private void setIntegerInternal(String key, int data){
        integers.put(key,data);
    }
    public void setString(String key, String data){
        setStringInternal(key, data);
        sTimestamp.remove(key);
        sExpire.remove(key);
    }
    public void setBoolean(String key, boolean data){
        setBooleanInternal(key, data);
        bTimestamp.remove(key);
        bExpire.remove(key);
    }
    public void setInteger(String key, int data){
        setIntegerInternal(key, data);
        iTimestamp.remove(key);
        iExpire.remove(key);
    }
    public void setString(String key, String data,int time){
        setStringInternal(key, data);
        sTimestamp.put(key,Global.getSector().getClock().getTimestamp());
        sExpire.put(key,time);
    }
    public void setBoolean(String key, boolean data,int time){
        setBooleanInternal(key, data);
        bTimestamp.put(key,Global.getSector().getClock().getTimestamp());
        bExpire.put(key,time);
    }
    public void setInteger(String key, int data,int time){
        setIntegerInternal(key, data);
        iTimestamp.put(key,Global.getSector().getClock().getTimestamp());
        iExpire.put(key,time);
    }


    public String getString(String key){
        if (!strings.containsKey(key)) return "";
        if (sExpire.containsKey(key) && sExpire.get(key) > Global.getSector().getClock().getElapsedDaysSince(sTimestamp.get(key))){
            sExpire.remove(key);
            sTimestamp.remove(key);
            strings.remove(key);
            return "";
        }
        return strings.get(key);
    }
    public boolean getBoolean(String key){
        if (!booleans.containsKey(key)) return false;
        if (bExpire.containsKey(key) && bExpire.get(key) >= Global.getSector().getClock().getElapsedDaysSince(bTimestamp.get(key))){
            bExpire.remove(key);
            bTimestamp.remove(key);
            booleans.remove(key);
            return false;
        }
        return booleans.get(key);
    }
    public int getInteger(String key){
        if (!integers.containsKey(key)) return 0;
        if (iExpire.containsKey(key) && iExpire.get(key) >= Global.getSector().getClock().getElapsedDaysSince(iTimestamp.get(key))){
            iExpire.remove(key);
            iTimestamp.remove(key);
            integers.remove(key);
            return 0;
        }
        return integers.get(key);
    }
}
