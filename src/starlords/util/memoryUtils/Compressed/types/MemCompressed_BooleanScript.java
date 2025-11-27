package starlords.util.memoryUtils.Compressed.types;

import starlords.util.ScriptedValues.SV_Boolean;
import starlords.util.memoryUtils.Compressed.MemCompressedOrganizer;

public class MemCompressed_BooleanScript extends MemCompressedOrganizer<Boolean, SV_Boolean> {
    @Override
    public Boolean getDefaltData(int a, Object linkedObject) {
        return list.get(a).getValue(linkedObject);
    }
}
