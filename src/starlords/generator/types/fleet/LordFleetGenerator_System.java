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
    public AvailableShipData skimPossibleShips(AvailableShipData input, boolean withRemoval) {
        String a = input.getRandomShip();
        target = Global.getSettings().getVariant(a).getHullSpec().getShipSystemId();
        return super.skimPossibleShips(input, withRemoval);
    }

    @Override
    public boolean canUseShip(String ship) {
        if (!Global.getSettings().getVariant(ship).getHullSpec().getShipSystemId().equals(target)) return false;
        return super.canUseShip(ship);
    }
}
