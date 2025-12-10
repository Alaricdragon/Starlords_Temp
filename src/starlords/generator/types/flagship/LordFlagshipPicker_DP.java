package starlords.generator.types.flagship;

import com.fs.starfarer.api.Global;
import starlords.generator.support.ShipData;

import java.util.ArrayList;

public class LordFlagshipPicker_DP extends LordFlagshipPickerBase{
    public LordFlagshipPicker_DP(String name) {
        super(name);
    }
    @Override
    public String pickFlagship(ArrayList<String> ships) {
        /*todo: this is not presently added into the generator. need to do that.*/
        float max = -9999;
        String out = null;
        for (String a : ships){
            float value = Global.getSettings().getVariant(a).getHullSpec().getSuppliesToRecover();
            if (value > max){
                max = value;
                out = a;
            }
        }
        return out;
    }
}
