package starlords.generator.types.flagship;

import com.fs.starfarer.api.Global;
import starlords.generator.LordGenerator;
import starlords.generator.support.ShipData;

import java.util.ArrayList;

public class LordFlagshipPicker_Cost extends LordFlagshipPickerBase{
    public LordFlagshipPicker_Cost(String name) {
        super(name);
    }
    @Override
    public String pickFlagship(ArrayList<String> ships) {
        //Object[] a = (ships.get((int)(Math.random()*ships.size()))).getSpawnWeight().keySet().toArray();
        float max = -9999;
        String out = null;
        for (String a : ships){
            float value = Global.getSettings().getVariant(a).getHullSpec().getBaseValue();
            if (value > max){
                max = value;
                out = a;
            }
        }
        return out;
    }
}
