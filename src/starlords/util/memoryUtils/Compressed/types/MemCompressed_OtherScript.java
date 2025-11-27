package starlords.util.memoryUtils.Compressed.types;

import starlords.util.ScriptedValues.SV_Object;
import starlords.util.memoryUtils.Compressed.MemCompressedOrganizer;

public class MemCompressed_OtherScript extends MemCompressedOrganizer<Object, SV_Object> {
    @Override
    public Object getDefaltData(int a, Object linkedObject) {
        return list.get(a).getValue(linkedObject);
    }
}
