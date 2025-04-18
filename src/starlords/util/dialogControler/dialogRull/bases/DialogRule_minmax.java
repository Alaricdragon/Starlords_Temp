package starlords.util.dialogControler.dialogRull.bases;

import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.person.Lord;
import starlords.util.dialogControler.dialogRull.DialogRule_Base;
import starlords.util.dialogControler.dialogValues.DialogValuesList;

public class DialogRule_minmax extends DialogRule_Base {
    private int max = 2147483647;
    private int min = -2147483647;
    private DialogValuesList maxList;
    private DialogValuesList minList;
    @SneakyThrows
    public DialogRule_minmax(JSONObject jsonObject,String key){
        if (!(jsonObject.get(key) instanceof JSONObject)){
            int value = jsonObject.getInt(key);
            max=value;
            min=value;
            return;
        }
        jsonObject.getJSONObject(key);
        if (jsonObject.has("max")){
            if (jsonObject.get("max") instanceof Integer) {
                max = jsonObject.getInt("max");
            }else{
                maxList = new DialogValuesList(jsonObject.getJSONObject("max"));
            }
        }
        if (jsonObject.has("min")){
            if (jsonObject.get("min") instanceof Integer) {
                min = jsonObject.getInt("min");
            }else{
                minList = new DialogValuesList(jsonObject.getJSONObject("min"));
            }
        }
        if ((!jsonObject.has("min") && !jsonObject.has("max"))){
            minList = new DialogValuesList(jsonObject);
            maxList = minList;
        }
    }
    @Override
    public boolean condition(Lord lord, Lord targetLord) {
        int rel = getValue(lord, targetLord);
        int max = this.max;
        if (maxList != null) max = maxList.getValue(lord, targetLord);
        int min = this.min;
        if (minList != null) min = minList.getValue(lord, targetLord);
        if (min <= rel && rel <= max) return true;
        return false;
    }

    protected int getValue(Lord lord, Lord targetLord){
        return 0;
    }
}
