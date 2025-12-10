package starlords.generator.types.flagship;

import com.fs.starfarer.api.Global;
import starlords.generator.LordGenerator;
import starlords.generator.support.ShipData;

import java.util.ArrayList;

public class LordFlagshipPicker_HP extends LordFlagshipPickerBase{
    public LordFlagshipPicker_HP(String name) {
        super(name);
    }

    @Override
    public String pickFlagship(ArrayList<String> ships) {
        //Object[] a = (ships.get((int)(Math.random()*ships.size()))).getSpawnWeight().keySet().toArray();
        float max = 0;
        String out = null;
        for (String a : ships){
            float hp = Global.getSettings().getVariant(a).getHullSpec().getHitpoints();
            if (hp > max){
                max = hp;
                out = a;
            }
        }
        //Object[] a = ship.getSpawnWeight().keySet().toArray();
        return out;
    }
}
