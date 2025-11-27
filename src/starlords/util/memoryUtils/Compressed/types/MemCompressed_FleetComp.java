package starlords.util.memoryUtils.Compressed.types;

import starlords.controllers.LordController;
import starlords.person.Lord;
import starlords.util.memoryUtils.Compressed.MemCompressedHolder;
import starlords.util.memoryUtils.Compressed.MemCompressedOrganizer;

public class MemCompressed_FleetComp extends MemCompressedOrganizer<MemCompressedHolder<?>, MemCompressedOrganizer<?,?>>{
    /*todo: the 'load' phase of this needs a link into all ship compositions.
            how it will work:
                1) in LordCompressedMemory.other memory, I will store 2 fleet compositions MemCompressedHolders. One for active fleet, and one for 'backup' fleet.
                    -note: backup fleet can be null because if it is it means the fleet is using its 'starting' fleet.
                    -note: for backup data, it might be wize to have a hashmap that stores links to all the relevent 'backup' data in memory. (or even just holding it in the data holder, not everything needs backup data)
                2) I will get said memory in the load function.
                3) I will also run MemCompressed_ShipComp .load / .repair in all of said data points. (so that class can be free).


     */
    @Override
    public void load() {
        for (Lord lord : LordController.getLordsList()) {
            //this.repair(lord.getCOMPRESSED_MEMORY(),lord);
            //this.repairStructuresOfMasterCompressedOrganizer(lord.getCOMPRESSED_MEMORY(),lord);
        }
    }
}
