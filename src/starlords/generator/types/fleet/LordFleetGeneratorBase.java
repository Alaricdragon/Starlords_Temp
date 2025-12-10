package starlords.generator.types.fleet;

import lombok.Getter;
import starlords.generator.support.AvailableShipData;
import starlords.generator.support.AvailableShipData_OUTDATED;
import starlords.generator.support.ShipData;

import static starlords.generator.support.AvailableShipData.*;

public class LordFleetGeneratorBase {
    @Getter
    private String name;
    public LordFleetGeneratorBase(String name){
        this.name = name;
    }

    @Deprecated
    public AvailableShipData_OUTDATED skimPossibleShips_OUTDATED(AvailableShipData_OUTDATED input){
        AvailableShipData_OUTDATED output = new AvailableShipData_OUTDATED();
        for(ShipData a : input.getUnorganizedShips().values()){
            ShipData b = filterShipData_OUTDATED(a);
            if (b != null){
                output.addShip(b);
            }
        }
        return output;
    }
    public boolean canUseShip(String ship){
        return true;
    }
    public AvailableShipData skimPossibleShips(AvailableShipData input,Object possibleShipData,boolean withRemoval){
        //holy fuck this is outdated.
        AvailableShipData output = new AvailableShipData();
        AvailableShipData out = new AvailableShipData();
        String[] types = {
                HULLTYPE_CARRIER,
                HULLTYPE_PHASE,
                HULLTYPE_WARSHIP,
                HULLTYPE_CARGO,
                HULLTYPE_LINER,
                HULLTYPE_COMBATCIV,
                HULLTYPE_TANKER,
                HULLTYPE_PERSONNEL,
                HULLTYPE_TUG,
                HULLTYPE_UTILITY
        };
        String[] sizes = {
                HULLSIZE_FRIGATE,
                HULLSIZE_DESTROYER,
                HULLSIZE_CRUISER,
                HULLSIZE_CAPITALSHIP
        };
        for (String type : types){
            for (String size : sizes){
                for (String a : input.getOrganizedShips().get(type).get(size).keySet()){
                    if (canUseShip(a)){
                        out.addShip(a,input.getOrganizedShips().get(type).get(size).get(a),type);
                        if (withRemoval) input.removeShip(a,type);
                    }
                }
            }
        }
        return output;
    }
    public Object setPossibleShipData(AvailableShipData input){
        //this sets the data for the possible ships. is only ran once on creation of this data
        //this wont work. a single instance of each class is available.
        return null;
    }
    @Deprecated
    public ShipData filterShipData_OUTDATED(ShipData data){
        return data;
    }
}
