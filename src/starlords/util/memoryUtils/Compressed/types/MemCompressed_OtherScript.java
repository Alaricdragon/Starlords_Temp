package starlords.util.memoryUtils.Compressed.types;

import starlords.util.memoryUtils.Compressed.MemCompressedOrganizer;
import starlords.util.memoryUtils.Compressed.hTypes.MemCompressed_R_Object_Base;

public class MemCompressed_OtherScript extends MemCompressedOrganizer<Object, MemCompressed_R_Object_Base> {
    @Override
    public Object getDefaltData(int a, Object linkedObject) {
        return list.get(a).getRandom(linkedObject);
    }
}
