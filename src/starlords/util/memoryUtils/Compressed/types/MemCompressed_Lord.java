package starlords.util.memoryUtils.Compressed.types;

import starlords.controllers.LordController;
import starlords.person.Lord;
import starlords.util.memoryUtils.Compressed.MemCompressedHolder;
import starlords.util.memoryUtils.Compressed.MemCompressedOrganizer;

public class MemCompressed_Lord extends MemCompressedOrganizer<MemCompressedHolder<?>,MemCompressedOrganizer<?,?>> {
    @Override
    public MemCompressedHolder<?> getDefaltData(int a, Object linkedObject) {
        //note: disorganized data is unrequired here. just organize it as required.
        return list.get(a).getHolderStructure(linkedObject);
    }

    @Override
    public void load() {
        for (Lord lord : LordController.getLordsList()) {
            this.repair(lord.getCOMPRESSED_MEMORY(),lord);
            /*for (String a : map.keySet()) {
                //note that it is repaired, not loaded. this could be an issue. alturation...?
                MemCompressedOrganizer<?, ?> b  = list.get(map.get(a));
                b.repair(lord.getCOMPRESSED_MEMORY().getItem(a),lord);
            }*/
            this.repairStructuresOfMasterCompressedOrganizer(lord.getCOMPRESSED_MEMORY(),lord);
            //for (String a : map.keySet()){
            //    MemCompressedOrganizer<?, ?> b  = list.get(map.get(a));
            //    b.repair(lord.getCOMPRESSED_MEMORY().getItem(),lord);
            //}
        }
        /*for (String a : map.keySet()){
            MemCompressedOrganizer<?, ?> b  = list.get(map.get(a));
            //I will need to run the repair inside of each of the load functions for each item?
            b.load();
        }*/
    }

    @Override
    public void save() {
        //this is saved elsewere.
    }
}
