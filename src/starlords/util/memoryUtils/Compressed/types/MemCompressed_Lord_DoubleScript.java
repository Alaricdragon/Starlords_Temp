package starlords.util.memoryUtils.Compressed.types;

import starlords.controllers.LordController;
import starlords.person.Lord;
import starlords.util.memoryUtils.Compressed.MemCompressedMasterList;
import starlords.util.memoryUtils.Compressed.MemCompressedOrganizer;
import starlords.util.memoryUtils.Compressed.hTypes.MemCompressed_Random_Double_Base;

public class MemCompressed_Lord_DoubleScript extends MemCompressedOrganizer<Double, MemCompressed_Random_Double_Base> {
    @Override
    public Double getDefaltData(int a, Object linkedObject) {
        return list.get(a).getRandom(linkedObject);
    }
    @Override
    public void load() {
        //note: json data will be loaded after this (or before this, does not matter) and will just not be effected because of how repair works
        for (Lord lord : LordController.getLordsList()) {
            //note: this repairs all starlords perfectly. just like that. they all have there default data's set. and nothing else needs to happen.
            this.repair(lord.getCOMPRESSED_MEMORY().getItem(MemCompressedMasterList.DOUBLE_KEY),lord);
        }
    }
}
