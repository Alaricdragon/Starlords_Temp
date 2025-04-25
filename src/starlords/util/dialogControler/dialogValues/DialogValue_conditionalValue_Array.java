package starlords.util.dialogControler.dialogValues;

import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;
import starlords.person.Lord;

import java.util.ArrayList;

public class DialogValue_conditionalValue_Array extends DialogValue_base{
    ArrayList<DialogValue_conditionalValue> values = new ArrayList<>();
    @SneakyThrows
    public DialogValue_conditionalValue_Array(JSONObject json, String key){
        super(json,key);
        JSONArray json2 = json.getJSONArray(key);
        for (int a = 0; a < json2.length(); a++){
            values.add(new DialogValue_conditionalValue(json2.getJSONObject(a)));
        }
    }

    @Override
    public int value(Lord lord, Lord targetLord) {
        int a = 0;
        for (DialogValue_conditionalValue b : values){
            a +=b.computeValue(lord,targetLord);
        }
        return a;
    }
}
