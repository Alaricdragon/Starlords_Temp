package starlords.util.ScriptedValues;

import java.util.ArrayList;

public class SV_S_Wrapper implements SV_String{
    private ArrayList<SV_String> values;
    private ArrayList<String> operators;
    public SV_S_Wrapper(ArrayList<SV_String> values, ArrayList<String> booleans){
        this.values = values;
        this.operators = booleans;
    }
    @Override
    public void init(ScriptedValueController value) {

    }

    @Override
    public String getValue(Object linkedObject) {
        String[] bols = new String[values.size()];
        for (int a = 0; a < values.size(); a++) bols[a] = values.get(a).getValue(linkedObject);
        String out = bols[0];
        for (int a = 0; a < operators.size(); a++){
            out = switch (operators.get(a)) {
                case "+" -> out + bols[a + 1];
                default -> out;
            };
        }
        return out;
    }
}
