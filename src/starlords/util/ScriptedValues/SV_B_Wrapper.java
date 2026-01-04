package starlords.util.ScriptedValues;

import java.util.ArrayList;

public class SV_B_Wrapper implements SV_Boolean{
    private ArrayList<SV_Boolean> values;
    private ArrayList<String> booleans;
    public SV_B_Wrapper(ArrayList<SV_Boolean> values, ArrayList<String> booleans){
        this.values = values;
        this.booleans = booleans;
    }
    @Override
    public void init(ScriptedValueController value) {
    }

    @Override
    public boolean getValue(Object linkedObject) {
        boolean[] bols = new boolean[values.size()];
        for (int a = 0; a < values.size(); a++) bols[a] = values.get(a).getValue(linkedObject);
        boolean out = bols[0];
        for (int a = 0; a < booleans.size(); a++){
            out = switch (booleans.get(a)) {
                case "AND" -> out && bols[a + 1];
                case "OR" -> out || bols[a + 1];
                case "!AND" -> !(out && bols[a + 1]);
                case "!OR" -> !(out || bols[a + 1]);
                case "XOR" -> (!out && bols[a + 1]) || (out && !bols[a + 1]);
                default -> out;
            };
        }
        return out;
    }
}
