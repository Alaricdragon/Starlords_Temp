package starlords.generator.types.fleet;

import com.fs.starfarer.api.Global;
import starlords.generator.support.AvailableShipData;
import starlords.generator.support.AvailableShipData_OUTDATED;
import starlords.generator.support.ShipData;

public class LordFleetGenerator_System extends LordFleetGeneratorBase{
    String target = "";

    public LordFleetGenerator_System(String name) {
        super(name);
    }

    @Override
    public AvailableShipData skimPossibleShips(AvailableShipData input, Object possibleShipData, boolean withRemoval) {
        target = (String) possibleShipData;
        return super.skimPossibleShips(input, possibleShipData ,withRemoval);
    }

    @Override
    public Object setPossibleShipData(AvailableShipData input) {
        String a = input.getRandomShip();
        String target = Global.getSettings().getVariant(a).getHullSpec().getShipSystemId();
        return super.setPossibleShipData(input);
    }

    @Override
    public boolean canUseShip(String ship) {
        if (!Global.getSettings().getVariant(ship).getHullSpec().getShipSystemId().equals(target)) return false;
        return super.canUseShip(ship);
    }
}
