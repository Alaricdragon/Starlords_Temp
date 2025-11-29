package starlords.util.memoryUtils.Compressed.types;

import starlords.util.ScriptedValues.SV_Object_Code;
import starlords.util.memoryUtils.Compressed.MemCompressedOrganizer;

public class MemCompressed_OtherScript extends MemCompressedOrganizer<Object, SV_Object_Code> {
    @Override
    public Object getDefaltData(int a, Object linkedObject) {
        return list.get(a).getValue(linkedObject);
    }
}
