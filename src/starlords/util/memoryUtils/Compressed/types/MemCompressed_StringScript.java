package starlords.util.memoryUtils.Compressed.types;

import starlords.util.memoryUtils.Compressed.MemCompressedOrganizer;
import starlords.util.memoryUtils.Compressed.hTypes.MemCompressed_R_String_Base;

public class MemCompressed_StringScript extends MemCompressedOrganizer<String, MemCompressed_R_String_Base> {
    @Override
    public String getDefaltData(int a, Object linkedObject) {
        return list.get(a).getRandom(linkedObject);
    }
}
