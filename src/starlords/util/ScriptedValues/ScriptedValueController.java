package starlords.util.ScriptedValues;

import com.fs.starfarer.api.Global;

import java.util.ArrayList;
import java.util.HashMap;

public class ScriptedValueController {
    //todo: If i ever find myself looking at this going WTF: this is for loading a single string value as many diffrent values, some of wish can be scritps.
    //      at present, there is a bug were string cannot accept having the : value inside of them without also having a ! before it. so !: -> :. and !!: = !:. also just : means it discards the rest of the fucking string.
    //      this is considered acceptable. at least for now....
    private ArrayList<String> activeString;
    public static final String TYPE_DOUBLE = "Double", TYPE_STRING = "String", TYPE_BOOLEAN = "Boolean", TYPE_OBJECT = "Object";
    public static final String TYPE_MUTABLE_STAT_BASE = "MutableStat", TYPE_MUTABLE_STAT_MULTI = "MutableStat_multi", TYPE_MUTABLE_STAT_FLAT = "MutableStat_flat";
    //private static final String TYPE_DOUBLE = "", TYPE_STRING = "", TYPE_BOOLEAN = "", TYPE_OBJECT = "";
    public ScriptedValueController(String input){
        String[] temp = input.split(":");
        ArrayList<String> newThing = new ArrayList<>();
        boolean shouldAdd = false;
        for (int c = 0; c < temp.length; c++){
            String a = temp[c];
            if (!shouldAdd) newThing.add(a);
            else a += temp[c-1];
            if (a.endsWith("!!")){
                //remove one !, as one was a marker and the other the reality.
                shouldAdd = false;
                temp[c] = removeLast(a);
                continue;
            }
            if (a.endsWith("!") && c != temp.length-1){
                //replace the ! with a :, before readding this string and the next together. because ! was a marker, : was removed previously
                shouldAdd = true;
                temp[c] = removeLast(a);
                temp[c] += ":";
                continue;
            }
            shouldAdd = false;
        }
        activeString = newThing;
    }
    private String removeLast(String a){
        StringBuilder nS = new StringBuilder();
        for (int b = 0; b < a.length()-1; b++){
            nS.append(a.charAt(b));
        }
        return nS.toString();
    }
    /*public SV_Boolean getNextBoolean(){
        return (SV_Boolean) getNextStringOrObject(TYPE_BOOLEAN);
    }
    public SV_Double getNextDouble(){
        return (SV_Double) getNextStringOrObject(TYPE_DOUBLE);
    }
    public SV_String getNextString(){
        return (SV_String) getNextStringOrObject(TYPE_STRING);
    }*/
    public SV_Object getNextObject(){
        return (SV_Object) getNextStringOrObject(TYPE_OBJECT);
    }
    private SV_Base getNextStringOrObject(String type){
        String got = activeString.get(0);
        activeString.remove(0);
        if (got.startsWith("!~")){
            return getBasicValue(got,type);
            //return getPrebuiltScript(got);
            //get this as though it is just a value.
        }
        if (got.startsWith("~~")){
            return getPrebuiltScript(got);
        }
        if (got.startsWith("~")){
            return getScript(got);
        }
        return getBasicValue(got,type);
    }
    private boolean isNextItem(String type){
        String got = activeString.get(0);
        switch (type){
            case TYPE_BOOLEAN:
                return switch (got) {
                    case "AND", "OR", "!AND", "!OR", "XOR" -> true;
                    default -> false;
                };
            case TYPE_STRING:
                return switch (got) {
                    case "+" -> true;
                    default -> false;
                };
            case TYPE_DOUBLE:
                return switch (got) {
                    case "+", "-", "*", "/", "^" -> true;
                    default -> false;
                };
            default:
                return false;
        }
    }
    private SV_Base getBasicValue(String script,String type){
        if (script.startsWith("!~")) script = script.replaceFirst("!~","~");
        else if (script.startsWith("!!~")) script = script.replaceFirst("!","");
        SV_Base out = null;
        switch (type){
            case TYPE_BOOLEAN:
                out = new SV_B_Static(Boolean.getBoolean(script));
                break;
            case TYPE_DOUBLE:
                out = new SV_D_Static(Double.parseDouble(script));
                break;
            case TYPE_STRING:
                out = new SV_S_Static(script);
                break;
            case TYPE_OBJECT:
                out = (SV_Object) Global.getSettings().getInstanceOfScript(script);
                break;
        }
        return out;
    }
    public SV_Base getPrebuiltScript(String script){
        String type = script.replaceFirst("~~","");
        SV_Base out = switch (type) {
            case "B_R" -> new SV_B_R();
            case "B_Faction" -> new SV_B_Faction();
            case "B_Culture" -> new SV_B_Culture();
            case "B_!" -> new SV_B_NOT();

            case "D_R" -> new SV_D_R();
            case "D_WR" -> new SV_D_WR();
            case "D_LR" -> new SV_D_LR();

            case "S_LR" -> new SV_S_LR();
            case "S_FactionPortrait" -> new SV_S_FactionPortrait();

            default -> null;
        };
        if (out == null){
            return null;
        }
        out.init(this);
        return out;
    }
    public SV_Base getScript(String script){
        SV_Base out = (SV_Base)Global.getSettings().getInstanceOfScript(script.replaceFirst("~",""));
        out.init(this);
        return out;
    }

    public SV_Boolean getNextBoolean(){
        SV_Boolean value = (SV_Boolean) getNextStringOrObject(TYPE_BOOLEAN);
        ArrayList<String> operators = new ArrayList<>();
        ArrayList<SV_Boolean> values = new ArrayList<>();
        //I am using recursion now. what has happened to me?
        while (isNextItem(TYPE_BOOLEAN)){
            String got = activeString.get(0);
            activeString.remove(0);
            operators.add(got);
            values.add(getNextBoolean());
        }
        if (!operators.isEmpty()){
            values.add(0,value);//really hope that works.
            return new SV_B_Wrapper(values,operators);
        }
        return value;
    }
    public SV_String getNextString(){
        SV_String value = (SV_String) getNextStringOrObject(TYPE_STRING);
        ArrayList<String> operators = new ArrayList<>();
        ArrayList<SV_String> values = new ArrayList<>();
        //I am using recursion now. what has happened to me?
        while (isNextItem(TYPE_BOOLEAN)){
            String got = activeString.get(0);
            activeString.remove(0);
            operators.add(got);
            values.add(getNextString());
        }
        if (!operators.isEmpty()){
            values.add(0,value);//really hope that works.
            return new SV_S_Wrapper(values,operators);
        }
        return value;
    }
    public SV_Double getNextDouble(){
        SV_Double value = (SV_Double) getNextStringOrObject(TYPE_DOUBLE);
        ArrayList<String> operators = new ArrayList<>();
        ArrayList<SV_Double> values = new ArrayList<>();
        //I am using recursion now. what has happened to me?
        while (isNextItem(TYPE_DOUBLE)){
            String got = activeString.get(0);
            activeString.remove(0);
            operators.add(got);
            values.add(getNextDouble());
        }
        if (!operators.isEmpty()){
            values.add(0,value);//really hope that works.
            return new SV_D_Wrapper(values,operators);
        }
        return value;
    }
}
