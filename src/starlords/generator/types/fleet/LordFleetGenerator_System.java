package starlords.generator.types.fleet;

import com.fs.starfarer.api.Global;
import starlords.generator.support.AvailableShipData_OUTDATED;
import starlords.generator.support.ShipData;

public class LordFleetGenerator_System extends LordFleetGeneratorBase{
    String target = "";

    public LordFleetGenerator_System(String name) {
        super(name);
    }

    @Override
    public AvailableShipData_OUTDATED skimPossibleShips(AvailableShipData_OUTDATED input) {
        ShipData a = input.getRandomShip();
        target = Global.getSettings().getHullSpec(a.getHullID()).getShipSystemId();
        return super.skimPossibleShips(input);
    }
    @Override
    public ShipData filterShipData(ShipData data) {
        if (!Global.getSettings().getHullSpec(data.getHullID()).getShipSystemId().equals(target)) return null;
        return super.filterShipData(data);
    }
}
