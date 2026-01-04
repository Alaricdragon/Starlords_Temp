package starlords.util.ScriptedValues;

import java.util.ArrayList;

public class SV_D_Wrapper implements SV_Double{
    private ArrayList<SV_Double> values;
    private ArrayList<String> operators;
    public SV_D_Wrapper(ArrayList<SV_Double> values, ArrayList<String> booleans){
        this.values = values;
        this.operators = booleans;
    }
    @Override
    public void init(ScriptedValueController value) {

    }

    @Override
    public double getValue(Object linkedObject) {
        double[] bols = new double[values.size()];
        for (int a = 0; a < values.size(); a++) bols[a] = values.get(a).getValue(linkedObject);
        double out = bols[0];
        for (int a = 0; a < operators.size(); a++){
            out = switch (operators.get(a)) {
                case "+" -> out + bols[a + 1];
                case "-" -> out - bols[a + 1];
                case "/" -> out / bols[a + 1];
                case "*" -> out * bols[a + 1];
                case "^" -> Math.pow(out, bols[a + 1]);
                default -> out;
            };
        }
        return out;
    }
}
