package starlords.generator.types.fleet;

import com.fs.starfarer.api.Global;
import starlords.generator.LordGenerator;
import starlords.generator.support.AvailableShipData;
import starlords.generator.support.AvailableShipData_OUTDATED;
import starlords.generator.support.ShipData;

public class LordFleetGenerator_Hullmod extends LordFleetGeneratorBase{
    String target = null;

    public LordFleetGenerator_Hullmod(String name) {
        super(name);
    }

    @Override
    public AvailableShipData skimPossibleShips(AvailableShipData input, boolean withRemoval) {
        int maxLoops = 5;
        while(maxLoops > 0 && target == null) {
            String a = input.getRandomShip();
            Object[] b;
            b = Global.getSettings().getVariant(a).getHullMods().toArray();
            if (b.length == 0){
                maxLoops--;
                continue;
            }
            target = (String) b[(int) LordGenerator.getRandom().nextInt(b.length)];
            return super.skimPossibleShips(input,withRemoval);
            //target = Global.getSettings().getHullSpec(a.getHullID());
        }
        return super.skimPossibleShips(input,withRemoval);
    }

    @Override
    public boolean canUseShip(String ship) {
        if (target == null) return false;
        if (Global.getSettings().getVariant(ship).getHullMods().contains(target)) return true;
        return super.canUseShip(ship);
    }
}
