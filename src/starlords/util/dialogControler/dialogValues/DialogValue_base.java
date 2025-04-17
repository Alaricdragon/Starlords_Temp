package starlords.util.dialogControler.dialogValues;

import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.person.Lord;

public class DialogValue_base {
    int base = 0;
    double multi = 1;
    @SneakyThrows
    public DialogValue_base(JSONObject json){
        if (json.has("multi")) multi = json.getDouble("multi");
        if (json.has("base")) base = json.getInt("base");
    }
    @SneakyThrows
    public DialogValue_base(JSONObject json, String key){
        if (json.get(key) instanceof JSONObject){
            JSONObject json2 = json.getJSONObject(key);
            if (json2.has("multi")) multi = json.getDouble("multi");
            if (json2.has("base")) base = json.getInt("base");
        }else if (json.get(key) instanceof Double){
            multi = json.getDouble(key);
        }
    }
    public int computeValue(Lord lord, Lord targetLord){
        return (int) ((base+value(lord, targetLord))*multi);
    }
    public int value(Lord lord, Lord targetLord){
        return 0;
    }
}
