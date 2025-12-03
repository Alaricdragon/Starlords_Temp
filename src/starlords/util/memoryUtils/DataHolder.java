package starlords.util.memoryUtils;

import com.fs.starfarer.api.Global;

import java.util.ArrayList;
import java.util.HashMap;

public class DataHolder {
    protected HashMap<String,Long> sTimestamp = new HashMap<>();
    protected HashMap<String,Long> bTimestamp = new HashMap<>();
    protected HashMap<String,Long> dTimestamp = new HashMap<>();
    protected HashMap<String,Long> oTimestamp = new HashMap<>();
    //protected HashMap<String,Long> CMTimestamp = new HashMap<>();

    protected HashMap<String,Integer> sExpire = new HashMap<>();
    protected HashMap<String,Integer> bExpire = new HashMap<>();
    protected HashMap<String,Integer> dExpire = new HashMap<>();
    protected HashMap<String,Integer> oExpire = new HashMap<>();
    //protected HashMap<String,Integer> CMExpire = new HashMap<>();

    protected HashMap<String,String> strings = new HashMap<>();
    protected HashMap<String,Boolean> booleans = new HashMap<>();
    protected HashMap<String,Double> doubles = new HashMap<>();
    protected HashMap<String,Object> objects = new HashMap<>();
    //protected HashMap<String, MemCompressedHolder> connectedMemory = new HashMap<>();
    public boolean hasData(){
        if (!strings.isEmpty()) return true;
        if (!booleans.isEmpty()) return true;
        if (!doubles.isEmpty()) return true;
        if (!objects.isEmpty()) return true;
        //if (!connectedMemory.isEmpty()) return true;
        return false;
    }

    public void repair(){
        repairString();
        repairBoolean();
        repairDouble();
        repairObject();
    }
    private void repairString(){
        ArrayList<String> removes = new ArrayList<>();
        for (String key : strings.keySet()){
            if (sExpire.containsKey(key) && sExpire.get(key) > Global.getSector().getClock().getElapsedDaysSince(sTimestamp.get(key))){
                removes.add(key);
            }
        }
        for (String key : removes){
            sExpire.remove(key);
            sTimestamp.remove(key);
            strings.remove(key);
        }
    }
    private void repairBoolean(){
        ArrayList<String> removes = new ArrayList<>();
        for (String key : booleans.keySet()){
            if (bExpire.containsKey(key) && bExpire.get(key) > Global.getSector().getClock().getElapsedDaysSince(bTimestamp.get(key))){
                removes.add(key);
            }
        }
        for (String key : removes){
            bExpire.remove(key);
            bTimestamp.remove(key);
            booleans.remove(key);
        }
    }
    private void repairDouble(){
        ArrayList<String> removes = new ArrayList<>();
        for (String key : doubles.keySet()){
            if (dExpire.containsKey(key) && dExpire.get(key) > Global.getSector().getClock().getElapsedDaysSince(dTimestamp.get(key))){
                removes.add(key);
            }
        }
        for (String key : removes){
            dExpire.remove(key);
            dTimestamp.remove(key);
            doubles.remove(key);
        }
    }
    private void repairObject(){
        ArrayList<String> removes = new ArrayList<>();
        for (String key : objects.keySet()){
            if (oExpire.containsKey(key) && oExpire.get(key) > Global.getSector().getClock().getElapsedDaysSince(oTimestamp.get(key))){
                removes.add(key);
            }
        }
        for (String key : removes){
            oExpire.remove(key);
            oTimestamp.remove(key);
            objects.remove(key);
        }
    }

    public boolean hasString(String key){
        if (sExpire.containsKey(key) && sExpire.get(key) > Global.getSector().getClock().getElapsedDaysSince(sTimestamp.get(key))){
            sExpire.remove(key);
            sTimestamp.remove(key);
            strings.remove(key);
        }
        return strings.containsKey(key);
    }
    public boolean hasBoolean(String key){
        if (bExpire.containsKey(key) && bExpire.get(key) > Global.getSector().getClock().getElapsedDaysSince(bTimestamp.get(key))){
            bExpire.remove(key);
            bTimestamp.remove(key);
            booleans.remove(key);
        }
        return booleans.containsKey(key);
    }
    public boolean hasDouble(String key){
        if (dExpire.containsKey(key) && dExpire.get(key) > Global.getSector().getClock().getElapsedDaysSince(dTimestamp.get(key))){
            dExpire.remove(key);
            dTimestamp.remove(key);
            doubles.remove(key);
        }
        return doubles.containsKey(key);
    }
    public boolean hasObject(String key){
        if (oExpire.containsKey(key) && oExpire.get(key) > Global.getSector().getClock().getElapsedDaysSince(oTimestamp.get(key))){
            oExpire.remove(key);
            oTimestamp.remove(key);
            objects.remove(key);
        }
        return objects.containsKey(key);
    }

    private void setStringInternal(String key, String data){
        strings.put(key,data);
    }
    private void setBooleanInternal(String key, boolean data){
        booleans.put(key,data);
    }
    private void setDoubleInternal(String key, double data){
        doubles.put(key,data);
    }
    private void setObjectInternal(String key, Object data){
        objects.put(key,data);
    }
    /*private void setCMInternal(String key, MemCompressedHolder data){
        connectedMemory.put(key,data);
    }*/
    /*public void set(String key, Object data){
        if (data instanceof Integer){
            setInteger(key,(int)data);
            return;
        }
        if (data instanceof Boolean) {
            setBoolean(key,(boolean)data);
            return;
        }
        if (data instanceof String){
            setString(key,(String) data);
            return;
        }
        if (data instanceof MemCompressedHolder<?>){
            setCM(key, (MemCompressedHolder<?>) data);
            return;
        }
    }*/

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
    public void setDouble(String key, double data){
        setDoubleInternal(key, data);
        dTimestamp.remove(key);
        dExpire.remove(key);
    }
    public void setObject(String key, Object data){
        setObjectInternal(key, data);
        oTimestamp.remove(key);
        oExpire.remove(key);
    }
    /*public void setCM(String key, MemCompressedHolder<?> data){
        setCMInternal(key, data);
        CMTimestamp.remove(key);
        CMExpire.remove(key);
    }*/
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
    public void setDouble(String key, double data, int time){
        setDoubleInternal(key, data);
        dTimestamp.put(key,Global.getSector().getClock().getTimestamp());
        dExpire.put(key,time);
    }
    public void setObject(String key, Object data,int time){
        setObjectInternal(key, data);
        oTimestamp.put(key,Global.getSector().getClock().getTimestamp());
        oExpire.put(key,time);
    }
    /*public void setCM(String key, MemCompressedHolder<?> data, int time){
        setCMInternal(key, data);
        CMTimestamp.put(key,Global.getSector().getClock().getTimestamp());
        CMExpire.put(key,time);
    }*/


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
    public double getDouble(String key){
        /*Logger log = Global.getLogger(DialogValuesList.class);
        String exspire = "null";
        String timePassed = "null";
        String value = "null";
        if (iExpire.containsKey(key))exspire=iExpire.get(key)+"";
        if (iTimestamp.containsKey(key))timePassed=Global.getSector().getClock().getElapsedDaysSince(iTimestamp.get(key))+"";
        if (integers.containsKey(key))value = integers.get(key)+"";
        log.info("getting data holder integer data from key: "+key+" as: expire: "+exspire+", timePassed: "+timePassed+", and value of: "+value);*/
        if (!doubles.containsKey(key)) return 0;
        if (dExpire.containsKey(key) && dExpire.get(key) <= Global.getSector().getClock().getElapsedDaysSince(dTimestamp.get(key))){
            dExpire.remove(key);
            dTimestamp.remove(key);
            doubles.remove(key);
            return 0;
        }
        return doubles.get(key);
    }
    public Object getObject(String key){
        /*Logger log = Global.getLogger(DialogValuesList.class);
        String exspire = "null";
        String timePassed = "null";
        String value = "null";
        if (iExpire.containsKey(key))exspire=iExpire.get(key)+"";
        if (iTimestamp.containsKey(key))timePassed=Global.getSector().getClock().getElapsedDaysSince(iTimestamp.get(key))+"";
        if (integers.containsKey(key))value = integers.get(key)+"";
        log.info("getting data holder integer data from key: "+key+" as: expire: "+exspire+", timePassed: "+timePassed+", and value of: "+value);*/
        if (!objects.containsKey(key)) return null;
        if (oExpire.containsKey(key) && oExpire.get(key) <= Global.getSector().getClock().getElapsedDaysSince(oTimestamp.get(key))){
            oExpire.remove(key);
            oTimestamp.remove(key);
            objects.remove(key);
            return null;
        }
        return objects.get(key);
    }
    /*public MemCompressedHolder<?> getCM(String key){
        if (!connectedMemory.containsKey(key)) return null;
        if (CMExpire.containsKey(key) && CMExpire.get(key) <= Global.getSector().getClock().getElapsedDaysSince(CMTimestamp.get(key))){
            CMExpire.remove(key);
            CMTimestamp.remove(key);
            connectedMemory.remove(key);
            return null;
        }
        return connectedMemory.get(key);
    }*/
}
