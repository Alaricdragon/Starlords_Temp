package starlords.util.memoryUtils.genaricLists;

import lombok.Getter;

public class SubStaticHashmap_Double {
    /*  so... what the hell is this?
        the 'CustomHashmap_' classes are hashmaps that are -not designed to be modified in size at any time.
        you -can- change add and remove items, but you should not. ever, as it has a massive overhead cost.
    */
    protected String[] keys;// = new a[];
    protected double[] data;
    public SubStaticHashmap_Double(int size){
        keys = new String[size];
        data = new double[size];
    }
    public boolean hasItem(String key){
        for (int a = 0; a < keys.length; a++) if (keys[a].equals(key)) return true;
        return false;
    }
    public double getItem(String key){
        for (int a = 0; a < keys.length; a++) if (keys[a].equals(key)) return data[a];
        return 0;
    }
    public void setItem(String key, double value){
        for (int a = 0; a < keys.length; a++) if (keys[a].equals(key)){
            data[a] = value;
            return;
        }
        resetItems(keys.length+1);
        data[data.length-1] = value;
        keys[data.length-1] = key;
    }
    public void removeItem(String key){
        for (int a = 0; a < keys.length; a++) if (keys[a].equals(key)){
            resetItems(keys.length-1,a);
            return;
        }
    }
    private void resetItems(int newSize){
        String[] tempA = new String[newSize];
        double[] tempB = new double[newSize];
        for (int a = 0; a < keys.length && a < newSize; a++){
            tempA[a] = keys[a];
            tempB[a] = data[a];
        }
        keys = tempA;
        data = tempB;
    }
    private void resetItems(int newSize,int removed){
        String[] tempA = new String[newSize];
        double[] tempB = new double[newSize];
        int change = 0;
        for (int a = 0; a < keys.length && a < newSize -change; a++){
            if (a == removed) {
                change = -1;
                continue;
            }
            tempA[a+change] = keys[a];
            tempB[a+change] = data[a];
        }
        keys = tempA;
        data = tempB;
    }

}
