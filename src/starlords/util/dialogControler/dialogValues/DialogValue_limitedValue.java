package starlords.util.dialogControler.dialogValues;

import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.person.Lord;

public class DialogValue_limitedValue extends DialogValue_base{
    private int max = 2147483647;
    private int min = -2147483647;
    private DialogValuesList maxList;
    private DialogValuesList minList;
    DialogValuesList value;
    public DialogValue_limitedValue(JSONObject json) {
        super(json);
        value = new DialogValuesList(json,"value");
        if (json.has("max")){
            maxList = new DialogValuesList(json,"max");
        }
        if (json.has("min")){
            minList = new DialogValuesList(json,"min");
        }
        if ((!json.has("min") && !json.has("max"))){
            minList = new DialogValuesList(json,"max");
            maxList = minList;
        }
    }
    @SneakyThrows
    public DialogValue_limitedValue(JSONObject json, String key) {
        super(json, key);
    }
    @Override
    public int value(Lord lord, Lord targetLord) {
        int value = this.value.getValue(lord, targetLord);
        int max = this.max;
        if (maxList != null) max = maxList.getValue(lord, targetLord);
        int min = this.min;
        if (minList != null) min = minList.getValue(lord, targetLord);
        value = Math.max(min,value);
        value = Math.min(max,value);
        return value;
    }
}
