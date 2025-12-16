package starlords.util.memoryUtils.Compressed_outdated.types;

import starlords.controllers.LordController;
import starlords.person.Lord;
import starlords.util.fleetCompasition.FleetCompositionData;
import starlords.util.fleetCompasition.ShipCompositionData;
import starlords.util.memoryUtils.Compressed_outdated.MemCompressedHolder;
import starlords.util.memoryUtils.Compressed_outdated.MemCompressedMasterList;
import starlords.util.memoryUtils.Compressed_outdated.MemCompressedOrganizer;
import starlords.util.memoryUtils.GenericMemory;

import static starlords.util.memoryUtils.Compressed_outdated.MemCompressedMasterList.*;

@Deprecated
public class MemCompressed_FleetComp extends MemCompressedOrganizer<MemCompressedHolder<?>, MemCompressedOrganizer<?,?>>{
    @Override
    public void load() {
        for (Lord lord : LordController.getLordsList()) {
            loadAllFleetCompsForMemory(lord.getMemory());
        }
    }
    private void loadAllFleetCompsForMemory(GenericMemory Memory){
        FleetCompositionData data = (FleetCompositionData)Memory.getCompressed_Object(FLEETCOMP_COMBAT);
        loadSingleFleetComp(data);

        data = (FleetCompositionData)Memory.getCompressed_Object(FLEETCOMP_CARGO);
        loadSingleFleetComp(data);

        data = (FleetCompositionData)Memory.getCompressed_Object(FLEETCOMP_FUEL);
        loadSingleFleetComp(data);

        data = (FleetCompositionData)Memory.getCompressed_Object(FLEETCOMP_PERSONAL);
        loadSingleFleetComp(data);

        data = (FleetCompositionData)Memory.getCompressed_Object(FLEETCOMP_TUG);
        loadSingleFleetComp(data);
    }
    private void loadSingleFleetComp(FleetCompositionData data){
        for (String a : data.getData().keySet()){
            //this is running the 'load' for the ShipCompositionData.
            MemCompressedOrganizer<?, ?> comp = MemCompressedMasterList.getMemory().get(MemCompressedMasterList.KEY_SHIP);
            ShipCompositionData ship = data.getData().get(a);
            comp.repair(ship.getMemory().getMemForRepairOnly(),ship);
            comp.repairStructuresOfMasterCompressedOrganizer(ship.getMemory().getMemForRepairOnly(),ship);
        }
        this.repair(data.getMemory().getMemForRepairOnly(),data);
        this.repairStructuresOfMasterCompressedOrganizer(data.getMemory().getMemForRepairOnly(),data);
    }
}
