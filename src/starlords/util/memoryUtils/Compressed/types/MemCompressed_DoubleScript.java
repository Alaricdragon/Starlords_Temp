package starlords.util.memoryUtils.Compressed.types;

import starlords.controllers.LordController;
import starlords.person.Lord;
import starlords.util.memoryUtils.Compressed.MemCompressedMasterList;
import starlords.util.memoryUtils.Compressed.MemCompressedOrganizer;
import starlords.util.randomLoader.RandomLoader_CustomRandom_Double;

public class MemCompressed_DoubleScript extends MemCompressedOrganizer<Double, RandomLoader_CustomRandom_Double> {
    @Override
    public Double getDefaltData(int a, Object linkedObject) {
        return list.get(a).getRandom(linkedObject);
    }

    @Override
    public void load() {
        //note: json data will be loaded after this (or before this, does not matter) and will just not be effected because of how repair works
        for (Lord lord : LordController.getLordsList()) {
            //note: this repairs all starlords perfectly. just like that. they all have there default data's set. and nothing else needs to happen.
            this.repair(lord.getCOMPRESSED_MEMORY().getItem(MemCompressedMasterList.STRING_DOUBLE_KEY),lord);
        }
    }
}
