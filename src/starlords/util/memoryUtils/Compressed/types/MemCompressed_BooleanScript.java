package starlords.util.memoryUtils.Compressed.types;

import starlords.util.memoryUtils.Compressed.MemCompressedOrganizer;
import starlords.util.memoryUtils.Compressed.hTypes.MemCompressed_R_Boolean_Base;

public class MemCompressed_BooleanScript extends MemCompressedOrganizer<Boolean, MemCompressed_R_Boolean_Base> {
    @Override
    public Boolean getDefaltData(int a, Object linkedObject) {
        return list.get(a).getRandom(linkedObject);
    }
}
