package starlords.generator.types.flagship;

import lombok.Getter;
import starlords.generator.LordGenerator;
import starlords.generator.support.ShipData;

import java.util.ArrayList;

public class LordFlagshipPickerBase {
    @Getter
    private String name = "";
    public LordFlagshipPickerBase(String name){
        this.name = name;
    }
    @Deprecated
    public String pickFlagship(ArrayList<ShipData> ships,boolean old){
        Object[] a = (ships.get((int)(LordGenerator.getRandom().nextInt(ships.size())))).getSpawnWeight().keySet().toArray();
        return (String)a[(int) (LordGenerator.getRandom().nextInt(a.length))];
    }
    public String pickFlagship(ArrayList<String> ships){
        return  ships.get(((LordGenerator.getRandom().nextInt(ships.size()))));
    }
}
