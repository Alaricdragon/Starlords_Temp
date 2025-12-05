package starlords.generator.types.fleet;

import lombok.Getter;
import starlords.generator.support.AvailableShipData_OUTDATED;
import starlords.generator.support.ShipData;

public class LordFleetGeneratorBase {
    @Getter
    private String name;
    public LordFleetGeneratorBase(String name){
        this.name = name;
    }

    public AvailableShipData_OUTDATED skimPossibleShips(AvailableShipData_OUTDATED input){
        AvailableShipData_OUTDATED output = new AvailableShipData_OUTDATED();
        for(ShipData a : input.getUnorganizedShips().values()){
            ShipData b = filterShipData(a);
            if (b != null){
                output.addShip(b);
            }
        }
        return output;
    }
    public ShipData filterShipData(ShipData data){
        return data;
    }
}
