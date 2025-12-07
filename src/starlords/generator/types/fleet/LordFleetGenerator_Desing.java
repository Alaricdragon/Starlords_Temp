package starlords.generator.types.fleet;

import com.fs.starfarer.api.Global;
import starlords.generator.support.AvailableShipData;
import starlords.generator.support.AvailableShipData_OUTDATED;
import starlords.generator.support.ShipData;

public class LordFleetGenerator_Desing extends LordFleetGeneratorBase{
    private String target = "";

    public LordFleetGenerator_Desing(String name) {
        super(name);
    }

    @Override
    public AvailableShipData skimPossibleShips(AvailableShipData input, boolean withRemoval) {
        String a = input.getRandomShip();
        target = Global.getSettings().getVariant(a).getHullSpec().getManufacturer();
        return super.skimPossibleShips(input, withRemoval);
    }

    @Override
    public boolean canUseShip(String ship) {
        if (!Global.getSettings().getVariant(ship).getHullSpec().getManufacturer().equals(target)) return false;
        return super.canUseShip(ship);
    }
}
