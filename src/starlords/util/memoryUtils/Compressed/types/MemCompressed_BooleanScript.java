package starlords.util.memoryUtils.Compressed.types;

import starlords.util.ScriptedValues.SV_Boolean_Code;
import starlords.util.memoryUtils.Compressed.MemCompressedOrganizer;

public class MemCompressed_BooleanScript extends MemCompressedOrganizer<Boolean, SV_Boolean_Code> {
    @Override
    public Boolean getDefaltData(int a, Object linkedObject) {
        return list.get(a).getValue(linkedObject);
    }
}
