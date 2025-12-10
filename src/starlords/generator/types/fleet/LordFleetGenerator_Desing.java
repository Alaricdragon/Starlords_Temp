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
    public AvailableShipData skimPossibleShips(AvailableShipData input,Object possibleShipData, boolean withRemoval) {
        String a = (String) possibleShipData;
        return super.skimPossibleShips(input,possibleShipData, withRemoval);
    }

    @Override
    public Object setPossibleShipData(AvailableShipData input) {
        String a = input.getRandomShip();
        String target = Global.getSettings().getVariant(a).getHullSpec().getManufacturer();
        return target;
    }

    @Override
    public boolean canUseShip(String ship) {
        if (!Global.getSettings().getVariant(ship).getHullSpec().getManufacturer().equals(target)) return false;
        return super.canUseShip(ship);
    }
}
