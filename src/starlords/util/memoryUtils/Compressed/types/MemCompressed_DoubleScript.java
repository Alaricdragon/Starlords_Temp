package starlords.util.memoryUtils.Compressed.types;

import starlords.util.memoryUtils.Compressed.MemCompressedOrganizer;
import starlords.util.memoryUtils.Compressed.hTypes.MemCompressed_R_Double_Base;

public class MemCompressed_DoubleScript extends MemCompressedOrganizer<Double, MemCompressed_R_Double_Base> {
    @Override
    public Double getDefaltData(int a, Object linkedObject) {
        return list.get(a).getRandom(linkedObject);
    }
}
