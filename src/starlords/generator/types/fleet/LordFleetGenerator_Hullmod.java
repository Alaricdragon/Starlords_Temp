package starlords.generator.types.fleet;

import com.fs.starfarer.api.Global;
import starlords.generator.LordGenerator;
import starlords.generator.support.AvailableShipData;
import starlords.generator.support.AvailableShipData_OUTDATED;
import starlords.generator.support.ShipData;
import starlords.util.Utils;

public class LordFleetGenerator_Hullmod extends LordFleetGeneratorBase{
    String target = null;

    public LordFleetGenerator_Hullmod(String name) {
        super(name);
    }

    @Override
    public AvailableShipData skimPossibleShips(AvailableShipData input,Object possibleShipData, boolean withRemoval) {
        target = null;
        if (possibleShipData != null) target = (String)possibleShipData;
        return super.skimPossibleShips(input,possibleShipData,withRemoval);
    }

    @Override
    public Object setPossibleShipData(AvailableShipData input) {
        String target = null;
        int maxLoops = 5;
        while(maxLoops > 0 && target == null) {
            String a = input.getRandomShip();
            Object[] b;
            b = Global.getSettings().getVariant(a).getHullMods().toArray();
            if (b.length == 0){
                maxLoops--;
                continue;
            }
            target = (String) b[(int) Utils.rand.nextInt(b.length)];
        }
        return target;
    }

    @Override
    public boolean canUseShip(String ship) {
        if (target == null) return false;
        if (Global.getSettings().getVariant(ship).getHullMods().contains(target)) return true;
        return super.canUseShip(ship);
    }
}
