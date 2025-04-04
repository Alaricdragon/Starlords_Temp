package starlords.util.dialogControler.dialogRull;

import lombok.SneakyThrows;
import org.json.JSONObject;
import starlords.controllers.LordController;
import starlords.person.Lord;

public class DialogRule_lordsCourted extends DialogRule_Base {
    int max = 2147483647;
    int min = -2147483647;
    @SneakyThrows
    public DialogRule_lordsCourted(JSONObject jsonObject){
        if (jsonObject.has("max")) max = jsonObject.getInt("max");
        if (jsonObject.has("min")) min = jsonObject.getInt("min");
    }

    @Override
    public boolean condition(Lord lord) {
        int numCourted = 0;
        for (Lord lord2 : LordController.getLordsList()) {
            if (lord2.isCourted()) numCourted += 1;
            if (numCourted > max) return false;
        }
        if (min <= numCourted && numCourted <= max) return true;
        return false;
    }
}
