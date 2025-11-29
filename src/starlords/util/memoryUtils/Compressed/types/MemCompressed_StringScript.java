package starlords.util.memoryUtils.Compressed.types;

import starlords.util.ScriptedValues.SV_String_Code;
import starlords.util.memoryUtils.Compressed.MemCompressedOrganizer;

public class MemCompressed_StringScript extends MemCompressedOrganizer<String, SV_String_Code> {
    @Override
    public String getDefaltData(int a, Object linkedObject) {
        return list.get(a).getValue(linkedObject);
    }
}
